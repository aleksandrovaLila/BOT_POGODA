package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ForeCastDto;
import org.example.dto.WeatherDataDto;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class Test001 {

    public static void main0(String[] args) throws URISyntaxException, IOException, InterruptedException {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            HttpRequest httpRequest = HttpRequest.newBuilder(
                            new URI("https://api.weatherapi.com/v1/current.json?key=9f4c58d3620b4372bc5180935251510&q=Moscow&aqi=yes"))
                    .GET().build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            var jsonBody = response.body();
            System.out.println(jsonBody);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonBody);
            double tempC = jsonNode.get("current").get("temp_c").asDouble();
            String humidity = jsonNode.get("current").get("humidity").asText();
            double feelslikeC = jsonNode.get("current").get("feelslike_c").asDouble();


            System.out.println("tempC=" + tempC);
            System.out.println("humidity=" + humidity);
            System.out.println("feelslikeC=" + feelslikeC);


        }

    }

    public static void main1(String[] args) {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
//            HttpRequest httpRequest = HttpRequest.newBuilder(
//                            new URI("https://api.weatherapi.com/v1/current.json?key=9f4c58d3620b4372bc5180935251510&q=Moscow&aqi=yes"))
//                    .GET().build();
            String city = "Moscow";
            var url = "https://api.weatherapi.com/v1/forecast.json?key=9f4c58d3620b4372bc5180935251510&q=" +
                    city + "&days=1&hourly=1";
            HttpRequest httpRequest = HttpRequest.newBuilder(
                    new URI(url)).GET().build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            var jsonBody = response.body();
            System.out.println(jsonBody);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonBody);
//            double tempC = jsonNode.get("current").get("temp_c").asDouble();
//            String humidity = jsonNode.get("current").get("humidity").asText();
//            double feelslikeC = jsonNode.get("current").get("feelslike_c").asDouble();
//
//            System.out.println("tempC=" + tempC);
//            System.out.println("humidity=" + humidity);
//            System.out.println("feelslikeC=" + feelslikeC);

            var foreCastFromJson = getForeCastFromJson(jsonNode);



        } catch (URISyntaxException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static List<ForeCastDto> getForeCastFromJson(JsonNode jsonNode) {

        List<ForeCastDto> foreCastDtos = new ArrayList<>();
        for (int i = 0; i <= 23; i++) {
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


    public static void main(String[] args) {
        System.out.println(PogodaBot.getTimeForDisplay("2025-11-20 00:00"));
    }


}
