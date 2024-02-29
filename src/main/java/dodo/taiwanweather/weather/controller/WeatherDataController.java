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
                .uri(URI.create("https://opendata.cwa.gov.tw/api/v1/rest/datastore/C-B0027-001?Authorization=CWA-2FD4BAFB-A6F7-4127-9D46-F2A699F51C10"))
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
                JSONObject precipitation = stats.getJSONObject("Precipitation");

                // 安全提取氣溫數據
                double tempMax = airTemperature.has("Maximum") ? airTemperature.getDouble("Maximum") : Double.NaN;
                double tempMin = airTemperature.has("Minimum") ? airTemperature.getDouble("Minimum") : Double.NaN;
                double tempMean = airTemperature.has("Mean") ? airTemperature.getDouble("Mean") : Double.NaN;

                // 安全提取濕度數據
                double humidityMean = relativeHumidity.has("Mean") ? relativeHumidity.getDouble("Mean") : Double.NaN;

                // 安全提取雨量數據
                double precipitationAccumulation = precipitation.has("Accumulation") ? precipitation.getDouble("Accumulation") : Double.NaN;
                double precipitationDays = precipitation.has("GE01Days") ? precipitation.getDouble("GE01Days") : Double.NaN;

                // 封裝數據
                Map<String, Object> stationData = new HashMap<>();
                stationData.put("StationName", stationName);


                weatherData.add(stationData);
            }

            System.out.println(weatherData);

            model.addAttribute("weatherData", weatherData);



        } catch (IOException | InterruptedException e) {
            e.printStackTrace();

        }

        return "index";
    }
}
