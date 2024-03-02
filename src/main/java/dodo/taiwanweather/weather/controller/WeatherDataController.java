package dodo.taiwanweather.weather.controller;

import java.util.List;
import java.util.Map;

import dodo.taiwanweather.weather.service.WeatherDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WeatherDataController {

    @Autowired
    private WeatherDateService weatherDateService;



    @GetMapping("/home")
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
}
