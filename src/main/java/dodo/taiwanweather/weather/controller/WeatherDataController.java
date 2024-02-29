package dodo.taiwanweather.weather.controller;

import dodo.taiwanweather.weather.service.WeatherDateService;

public class WeatherDataController {

    private WeatherDateService weatherDateService;

    public String home(){


        return "index";
    }
}
