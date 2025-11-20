package org.example.dto;

public class WeatherDataDto {

    private Double tempC;
    private String humidity;
    private Double feelsLikeC;
    private String sunset;
    private String sunrise;
    private double windMetersPerSecond;

    public Double getTempC() {
        return tempC;
    }

    public WeatherDataDto setTempC(Double tempC) {
        this.tempC = tempC;
        return this;
    }

    public String getHumidity() {
        return humidity;
    }

    public WeatherDataDto setHumidity(String humidity) {
        this.humidity = humidity;
        return this;
    }

    public Double getFeelsLikeC() {
        return feelsLikeC;
    }

    public WeatherDataDto setFeelsLikeC(Double feelsLikeC) {
        this.feelsLikeC = feelsLikeC;
        return this;
    }

    public String getSunset() {
        return sunset;
    }

    public WeatherDataDto setSunset(String sunset) {
        this.sunset = sunset;
        return this;
    }

    public String getSunrise() {
        return sunrise;
    }

    public WeatherDataDto setSunrise(String sunrise) {
        this.sunrise = sunrise;
        return this;
    }

    public double getWindMetersPerSecond() {
        return windMetersPerSecond;
    }

    public WeatherDataDto setWindMetersPerSecond(double windMetersPerSecond) {
        this.windMetersPerSecond = windMetersPerSecond;
        return this;
    }
}
