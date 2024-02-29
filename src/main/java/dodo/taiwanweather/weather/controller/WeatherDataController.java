package dodo.taiwanweather.weather.controller;

import dodo.taiwanweather.weather.service.Impl.WeatherDateServiceImpl;
import dodo.taiwanweather.weather.service.WeatherDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@Controller
public class WeatherDataController {

    @Autowired
    private WeatherDateService weatherDateService;


    @GetMapping("/home")
    public String home(Model model){
//      取得all縣市資訊
        String jsonUrl = "C:\\Users\\apple\\OneDrive\\桌面\\taiwanHistoricalWeatherData-10Year4\\TaiwanHistoricalWeatherData-10Year\\src\\main\\java\\dodo\\taiwanweather\\weather\\jsonTest\\response_1709197604777.json";

        List<String> AllStationNames =  weatherDateService.getStationNames(jsonUrl);

//        System.out.println(AllStationNames);
        model.addAttribute("AllStationNames" , AllStationNames);

        return "index";
    }
}
