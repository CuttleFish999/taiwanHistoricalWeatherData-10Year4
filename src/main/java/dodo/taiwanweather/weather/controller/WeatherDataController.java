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
import org.springframework.web.client.RestTemplate;

@Controller
public class WeatherDataController {

    @Autowired
    private WeatherDateService weatherDateService;

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/home")
    public String home(Model model){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://opendata.cwa.gov.tw/api/v1/rest/datastore/C-B0027-001?Authorization=CWA-2FD4BAFB-A6F7-4127-9D46-F2A699F51C10&limit=1&weatherElement=&Month=1"))
                .header("accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            // 解析JSON
            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray locations = jsonObject.getJSONObject("records").getJSONObject("data").getJSONObject("surfaceObs").getJSONArray("location");

            List<Map<String, Object>> weatherData = new ArrayList<>();
            for (int i = 0; i < locations.length(); i++) {
                JSONObject location = locations.getJSONObject(i);
                JSONObject station = location.getJSONObject("station");
                String stationName = station.getString("StationName");

                JSONObject stats = location.getJSONObject("stationObsStatistics");

                JSONObject airTemperature = stats.getJSONObject("AirTemperature");
                JSONObject relativeHumidity = stats.getJSONObject("RelativeHumidity");

                JSONArray monthlyTemp = airTemperature.getJSONArray("monthly");
                JSONArray monthlyHumidity = relativeHumidity.getJSONArray("monthly");

                JSONObject firstMonthTemp = monthlyTemp.getJSONObject(0);
                JSONObject firstMonthHumidity = monthlyHumidity.getJSONObject(0);

                Integer month = firstMonthTemp.has("Month") ? Integer.valueOf(firstMonthTemp.getInt("Month")) : null; // 月
                double tempMean = firstMonthTemp.has("Mean") ? firstMonthTemp.getDouble("Mean") : Double.NaN; // 溫度
                double humidityMean = firstMonthHumidity.has("Mean") ? firstMonthHumidity.getDouble("Mean") : Double.NaN; // 濕度

//                double meanGE25Days = firstMonth.has("meanGE25Days") ? firstMonth.getDouble("meanGE25Days") : Double.NaN; //一個月內最低
//                double minLE10Days = firstMonth.has("minLE10Days") ? firstMonth.getDouble("minLE10Days") : Double.NaN;  //一個月內平均

                // 封装
                Map<String, Object> stationData = new HashMap<>();
                stationData.put("StationName", stationName);
                stationData.put("monthly", month);
                stationData.put("TemperatureMean", tempMean); //平均溫度
                stationData.put("RelativeHumidity_A", humidityMean); //相對溼度

                weatherData.add(stationData);
            }

            if (!weatherData.isEmpty()) {
                System.out.println(weatherData.get(0).get("RelativeHumidity_A"));
            }

            model.addAttribute("weatherData", weatherData);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return "index";
    }

}
