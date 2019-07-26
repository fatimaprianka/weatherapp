package com.example.shamim.weather;


import com.example.shamim.weather.model.CurrentWeatherCity;
import com.example.shamim.weather.model.CurrentWeatherResponse;
import com.example.shamim.weather.model.ForecastWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WeatherServiceAPI {
    @GET()
    Call<CurrentWeatherResponse>getCurrentWeather(@Url String urlString);
    @GET()
    Call<ForecastWeatherResponse>getForecastWeather(@Url String urlString);
    @GET
    Call<CurrentWeatherCity> getCurrentWeatherCity(@Url String urlString);

}
