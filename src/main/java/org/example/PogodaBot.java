package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PogodaBot extends TelegramLongPollingBot {


    @Override
    public String getBotUsername() {
        return "Pogoda Bot";
    }

    @Override
    public String getBotToken() {
        return "8481929275:AAHBEnnmRq9rnlHf56q3a2caJSciI8cbtvo";
    }

    @Override
    public void onUpdateReceived(Update update) {
        // System.out.println(update);

        var msg = update.getMessage();
        var user = msg.getFrom();
        var userId = user.getId();

        System.out.println(user.getFirstName() + " wrote " + msg.getText());

        if (msg.isCommand() && msg.getText().equals("/start")) {
            String greetingsMessage = "ЙООООООООООООу бро! Я БОТ, я пока не расскажу тебе про погоду. Чтобы узнать погоду, напиши название города)";
            sendText(userId, greetingsMessage);
        } else {
            var answer = getWeatherInfo(msg.getText());

            if (answer == WeatherResponseDto.EMPTY) {
                sendText(userId, "Нет данных по такому городу!");
            } else {
                String answerToUser =
                        "Город: " + answer.getCity() + "\n" +
                        "Страна: " + answer.getCountry() + "\n" +
                                "Температура (Цельсиях): " + answer.getTempC() + "\n" +
                                "Температура по ощущениям (Цельсиях): " + answer.getFeelsLikeC() + "\n" +
                                "Влажность: " + answer.getHumidity();
                sendText(userId, answerToUser);
            }
        }
    }

    private WeatherResponseDto getWeatherInfo(String city) {
        // Делаем запрос в сервис

        try (HttpClient httpClient = HttpClient.newHttpClient()) {

            var url = "https://api.weatherapi.com/v1/current.json?key=9f4c58d3620b4372bc5180935251510&q=" +
                    city +
                    "&aqi=yes";
            System.out.println("url=" + url);
            HttpRequest httpRequest = HttpRequest.newBuilder(
                    new URI(url)).GET().build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            var jsonBody = response.body();
            System.out.println(jsonBody);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonBody);
            var current = jsonNode.get("current");
            if (current == null) {
                return WeatherResponseDto.EMPTY;
            }

            var location = jsonNode.get("location");

            double tempC = current.get("temp_c").asDouble();
            String cityName = location.get("name").asText();
            String country = location.get("country").asText();
            String humidity = current.get("humidity").asText();
            double feelslikeC = current.get("feelslike_c").asDouble();

            return new WeatherResponseDto()
                    .setCity(cityName)
                    .setCountry(country)
                    .setTempC(tempC)
                    .setHumidity(humidity)
                    .setFeelsLikeC(feelslikeC);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }
}
