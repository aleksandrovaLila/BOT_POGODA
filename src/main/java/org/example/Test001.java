package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Test001 {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
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


}
