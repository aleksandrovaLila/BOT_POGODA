package org.example;

public class WeatherResponseDto {


    public static final WeatherResponseDto EMPTY = new WeatherResponseDto();

    private String city;
    private String country;
    private Double tempC;
    private String humidity;
    private Double feelsLikeC;

    public String getCity() {
        return city;
    }

    public WeatherResponseDto setCity(String city) {
        this.city = city;
        return this;
    }

    public Double getTempC() {
        return tempC;
    }

    public WeatherResponseDto setTempC(double tempC) {
        this.tempC = tempC;
        return this;
    }

    public String getHumidity() {
        return humidity;
    }

    public WeatherResponseDto setHumidity(String humidity) {
        this.humidity = humidity;
        return this;
    }

    public Double getFeelsLikeC() {
        return feelsLikeC;
    }

    public WeatherResponseDto setFeelsLikeC(double feelsLikeC) {
        this.feelsLikeC = feelsLikeC;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public WeatherResponseDto setCountry(String country) {
        this.country = country;
        return this;
    }
}
