package org.example.dto;

public class ForeCastDto {

    // 2025-11-20 00:00
    private String time;

    private WeatherDataDto weatherDataDto;

    public String getTime() {
        return time;
    }

    public ForeCastDto setTime(String time) {
        this.time = time;
        return this;
    }

    public WeatherDataDto getWeatherDataDto() {
        return weatherDataDto;
    }

    public ForeCastDto setWeatherDataDto(WeatherDataDto weatherDataDto) {
        this.weatherDataDto = weatherDataDto;
        return this;
    }
}
