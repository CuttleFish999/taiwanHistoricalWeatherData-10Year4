package dodo.taiwanweather.weather.controller;

import java.util.List;
import java.util.Map;

import dodo.taiwanweather.weather.dto.WeatherForeacst;
import dodo.taiwanweather.weather.service.WeatherDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class WeatherDataController {

    @Autowired
    private WeatherDateService weatherDateService;



    @GetMapping("/")
    public String home(Model model) {

        List<Map<String, Object>> weatherData = weatherDateService.fetchStationData();
        model.addAttribute("weatherData", weatherData);

        return "index";
    }

    @GetMapping("/getSingleMonthData/{stationId}")
    @ResponseBody
    public List<Map<String, Object>> getSingleMonthData(@PathVariable String stationId){
        List<Map<String, Object>> singleMonthData = weatherDateService.fetchWeatherData(stationId);
        return singleMonthData;

    }

    @GetMapping("/getWeatherTemperature")
    public String getWeatherTemperature() {


        String url = "https://archive-api.open-meteo.com/v1/archive?latitude=24&longitude=121&start_date=2014-03-05&end_date=2024-03-06&hourly=temperature_2m";

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<WeatherForeacst> response = restTemplate.getForEntity(url, WeatherForeacst.class);


        return "index2";

    }

}
