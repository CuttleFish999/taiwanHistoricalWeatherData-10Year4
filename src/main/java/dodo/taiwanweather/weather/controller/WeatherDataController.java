package dodo.taiwanweather.weather.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dodo.taiwanweather.weather.service.WeatherDateService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class WeatherDataController {

    @Autowired
    private WeatherDateService weatherDateService;

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/home")
    public String home(Model model) {

        List<Map<String, Object>> weatherData = weatherDateService.fetchWeatherData();
        model.addAttribute("weatherData", weatherData);

        return "index2";
    }

    @GetMapping("/getSingleMonthData")
    @ResponseBody
    public List<Map<String, Object>> getSingleMonthData(){
        List<Map<String, Object>> singleMonthData = weatherDateService.fetchWeatherData();
        return singleMonthData;

    }
}
