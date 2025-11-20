package org.example.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WeatherResponseDto {

    public static final WeatherResponseDto EMPTY = new WeatherResponseDto();

    private String city;
    private String country;

    WeatherDataDto currentWeatherDataDto;
    List<ForeCastDto> foreCastDtos = new ArrayList<>();

    public String getCity() {
        return city;
    }

    public WeatherResponseDto setCity(String city) {
        this.city = city;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public WeatherResponseDto setCountry(String country) {
        this.country = country;
        return this;
    }

    public List<ForeCastDto> getForeCastDtos() {
        return foreCastDtos;
    }

    public WeatherResponseDto setForeCastDtos(List<ForeCastDto> foreCastDtos) {
        this.foreCastDtos = foreCastDtos;
        return this;
    }

    public WeatherDataDto getCurrentWeatherDataDto() {
        return currentWeatherDataDto;
    }

    public WeatherResponseDto setCurrentWeatherDataDto(WeatherDataDto currentWeatherDataDto) {
        this.currentWeatherDataDto = currentWeatherDataDto;
        return this;
    }
}
