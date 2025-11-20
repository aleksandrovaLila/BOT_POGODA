package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ForeCastDto;
import org.example.dto.WeatherDataDto;
import org.example.dto.WeatherResponseDto;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;                //Внешние библиотеки и классы
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PogodaBot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "Господин Погодин";
    }

    @Override
    public String getBotToken() {
        return "8481929275:AAHBEnnmRq9rnlHf56q3a2caJSciI8cbtvo";
    } // для доступа к API Telegram

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();              //получение данных (сообщение, от кого и уникальный номер пользователя)
        var userId = user.getId();

        System.out.println(user.getFirstName() + " wrote " + msg.getText()); //проверка на работу бота

        if (msg.isCommand() && msg.getText().equals("/start")) {
            String greetingsMessage = "Привет, я Господин Погодин. Чтобы узнать погоду, напиши название города)";
            sendText(userId, greetingsMessage); //
        } else {

            var answer = getWeatherInfo(msg.getText());
            if (answer == WeatherResponseDto.EMPTY) {
                sendText(userId, "Нет данных по такому городу, введите ещё раз");
            } else {
                //String answerToUser =
//                    "Погода " + answer.getCity() + "," + "cтрана" + answer.getCountry() + "." + "\n" +
//                            "Температура(сейчас): " + answer.getTempC() + "°C" + "\n" +
//                                "🤔 Oщущается как " + answer.getFeelsLikeC() + "°C" + "\n" + "\n" +
//                                "💧 Влажность " + answer.getHumidity() + "\n" + "Восход  " + answer.getSunrise() + "," + "Закат" + answer.getSunset();
//                sendText(userId, answerToUser);.append("🕒 ").append(shortTime)


                var sb = new StringBuilder();
                sb.append("Погода " + answer.getCity() + "," + "cтрана" + answer.getCountry() + "." + "\n");

                var current = answer.getCurrentWeatherDataDto();
                sb.append("Сейчас: | 🌡 ").append(current.getTempC()).append("°C")
                        .append(" | 💧 ").append(current.getHumidity()).append("%")
                        .append(" | 💨 ").append(current.getWindMetersPerSecond()).append(" м/с")
                        .append("\n");

                sb.append("Прогноз:");


                for (ForeCastDto hourWeatherDataDto : answer.getForeCastDtos()) {
                    sb.append(getTimeForDisplay(hourWeatherDataDto.getTime()));

                    var forecastDataDto = hourWeatherDataDto.getWeatherDataDto();
                    sb.append(" | 🌡 ").append(forecastDataDto.getTempC()).append("°C")
                            .append(" | 💧 ").append(forecastDataDto.getHumidity()).append("%")
                            .append(" | 💨 ").append(forecastDataDto.getWindMetersPerSecond()).append(" м/с")
                            .append("\n");
                }

                sendText(userId, sb.toString());


            }
        }
    }


    private WeatherResponseDto getWeatherInfo(String city) {
        // Делаем запрос в сервис (для любого города)
        try (HttpClient httpClient = HttpClient.newHttpClient()) { //HTTP клиент для запросов

            var url = "https://api.weatherapi.com/v1/current.json?key=9f4c58d3620b4372bc5180935251510&q=" +
                    city + "&aqi=yes";
            System.out.println("url=" + url);
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(url)).GET().build(); //GET запрос к API погоды
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()); //Получение ответа в виде строки
            var jsonBody = response.body(); //Парсинг JSON ответа
            System.out.println(jsonBody);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonBody);
            var current = jsonNode.get("current");
            if (current == null) {
                return WeatherResponseDto.EMPTY; //ничего нет
            }

            var location = jsonNode.get("location");

            WeatherResponseDto weatherResponseDto = new WeatherResponseDto();

            String cityName = location.get("name").asText();
            weatherResponseDto.setCity(cityName);

            String country = location.get("country").asText();
            weatherResponseDto.setCity(country);


            WeatherDataDto currentWeatherDataDto = new WeatherDataDto();

            double tempC = current.get("temp_c").asDouble();
            currentWeatherDataDto.setTempC(tempC);

            String humidity = current.get("humidity").asText();
            currentWeatherDataDto.setHumidity(humidity);

            double feelslikeC = current.get("feelslike_c").asDouble();
            currentWeatherDataDto.setFeelsLikeC(feelslikeC);

            weatherResponseDto.setCurrentWeatherDataDto(currentWeatherDataDto);

//            String sunset = current.get("sunset").asText();
//            String sunrise = current.get("sunset").asText();

            var weatherForecast = getWeatherForecast(cityName);
            weatherResponseDto.setForeCastDtos(weatherForecast);


            return weatherResponseDto;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())  //отправитель
                .text(what).build();    //содержимое сообщения
        try {
            execute(sm);                        //отправка
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //ошибки
        }
    }

    private List<ForeCastDto> getWeatherForecast(String city) throws URISyntaxException, IOException, InterruptedException {

        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            var url1 = "https://api.weatherapi.com/v1/forecast.json?key=9f4c58d3620b4372bc5180935251510&q=" +
                    city + "&days=1&hourly=1";  // прогноз на 1 день с почасовыми данными

            System.out.println("url=" + url1);
            HttpRequest httpRequest = HttpRequest.newBuilder(new URI(url1)).GET().build(); //GET запрос к API погоды
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString()); //Получение ответа в виде строки
            var jsonBody = response.body(); //Парсинг JSON ответа
            System.out.println(jsonBody);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonBody);

            return getForeCastFromJson(jsonNode);

        }
    }

    public static List<ForeCastDto> getForeCastFromJson(JsonNode jsonNode) {

        List<ForeCastDto> foreCastDtos = new ArrayList<>();
        for (int i = 7; i <= 22; i += 3) {
            var jsonNode1 = jsonNode.get("forecast").get("forecastday").get(0).get("hour").get(i);

            ForeCastDto foreCastDto = new ForeCastDto();
            foreCastDto.setTime(jsonNode1.get("time").asText());

            WeatherDataDto weatherDataDto =
                    new WeatherDataDto()
                            .setTempC(jsonNode1.get("temp_c").asDouble())
                            .setHumidity(jsonNode1.get("humidity").asText())
                            .setFeelsLikeC(jsonNode1.get("feelslike_c").asDouble())
                            .setWindMetersPerSecond(jsonNode1.get("wind_kph").asDouble() * 1000 / 3600);

            foreCastDto.setWeatherDataDto(weatherDataDto);

            foreCastDtos.add(foreCastDto);


        }
        return foreCastDtos;
    }

    public static String getTimeForDisplay(String timeFromJson) {
//        2025-11-20 00:00
        return timeFromJson.split(" ")[1];
    }
}
