package dodo.taiwanweather.weather.controller;

import dodo.taiwanweather.weather.service.Impl.WeatherDateServiceImpl;
import dodo.taiwanweather.weather.service.WeatherDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@Controller
public class WeatherDataController {

    @Autowired
    private WeatherDateService weatherDateService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/home")
    public String home(Model model){
////      取得all縣市資訊
        String jsonUrl = "C:\\Users\\apple\\OneDrive\\桌面\\taiwanHistoricalWeatherData-10Year4\\TaiwanHistoricalWeatherData-10Year\\src\\main\\java\\dodo\\taiwanweather\\weather\\jsonTest\\response_1709197604777.json";
        List<String> AllStationNames =  weatherDateService.getStationNames(jsonUrl);
        model.addAttribute("AllStationNames" , AllStationNames);


//        String apiUrl = "https://opendata.cwa.gov.tw/api/v1/rest/datastore/C-B0027-001?Authorization=CWA-2FD4BAFB-A6F7-4127-9D46-F2A699F51C10&limit=1";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "CWA-2FD4BAFB-A6F7-4127-9D46-F2A699F51C10");
////        List<String> AllStationNames = weatherDateService.getStationNames();
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        System.out.println(entity);
//
//
//        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
        return "index";
    }
}
