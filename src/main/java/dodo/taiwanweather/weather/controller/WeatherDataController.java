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
    public String home(Model model) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://opendata.cwa.gov.tw/api/v1/rest/datastore/C-B0027-001?Authorization=CWA-2FD4BAFB-A6F7-4127-9D46-F2A699F51C10&limit=1&weatherElement=&Month=1,2,3,4,5,6,7,8,9,10,11,12"))
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
                JSONObject windSpeed = stats.getJSONObject("WindSpeed");
                JSONObject airPressure = stats.getJSONObject("AirPressure");
                JSONObject sunshineDuration = stats.getJSONObject("SunshineDuration");


                JSONArray monthlyTemp = airTemperature.getJSONArray("monthly");
                JSONArray monthlyHumidity = relativeHumidity.getJSONArray("monthly");
                JSONArray monthlyPrecipitation = precipitation.getJSONArray("monthly");
                JSONArray monthlywindSpeed = windSpeed.getJSONArray("monthly");
                JSONArray monthlyAirPressure = airPressure.getJSONArray("monthly");
                JSONArray monthlyAirSunshineDuration = sunshineDuration.getJSONArray("monthly");

                for (int monthCount = 0; monthCount < 12; monthCount++) {
                    JSONObject firstMonthTemp = monthlyTemp.getJSONObject(monthCount);
                    JSONObject firstMonthHumidity = monthlyHumidity.getJSONObject(monthCount);
                    JSONObject firstMonthPrecipitation = monthlyPrecipitation.getJSONObject(monthCount);
                    JSONObject firstMonthWindSpeed = monthlywindSpeed.getJSONObject(monthCount);
                    JSONObject firstMonthlyAirPressure = monthlyAirPressure.getJSONObject(monthCount);
                    JSONObject firstMonthlyAirSunshineDuration = monthlyAirSunshineDuration.getJSONObject(monthCount);


                    int stationStartYear = airTemperature.getInt("StationStartYear"); //開始年
                    int stationEndYear = airTemperature.getInt("StationEndYear"); //結束年
                    Integer month = firstMonthTemp.has("Month") ? Integer.valueOf(firstMonthTemp.getInt("Month")) : null; // 月份
                    double tempMean = firstMonthTemp.has("Mean") ? firstMonthTemp.getDouble("Mean") : Double.NaN; // 平均温度
                    double humidityMean = firstMonthHumidity.has("Mean") ? firstMonthHumidity.getDouble("Mean") : Double.NaN; // 平均湿度
                    double Precipitation = firstMonthPrecipitation.has("Accumulation") ? firstMonthPrecipitation.getDouble("Accumulation") : Double.NaN; // 累積雨量
                    double WindSpeed = firstMonthWindSpeed.has("Mean") ? firstMonthWindSpeed.getDouble("Mean") : Double.NaN; // 平均風速
                    double AirPressure = firstMonthlyAirPressure.has("Mean") ? firstMonthlyAirPressure.getDouble("Mean") : Double.NaN; // 平均氣壓
                    double SunshineDuration = firstMonthlyAirSunshineDuration.has("Total") ? firstMonthlyAirSunshineDuration.getDouble("Total") : Double.NaN; // 平均氣壓


                    Map<String, Object> weatherMap = new HashMap<>();
                    System.out.println( (monthCount == 0));
                    if (monthCount == 0) {
                        weatherMap.put("StationName", stationName);
                    }else{
                        weatherMap.put("StationName", "");
                    }
                    weatherMap.put("StationStartYear", stationStartYear);
                    weatherMap.put("StationEndYear", stationEndYear);
                    weatherMap.put("Month", month);
                    weatherMap.put("TemperatureMean", tempMean);
                    weatherMap.put("HumidityMean", humidityMean);
                    weatherMap.put("Precipitation", Precipitation);
                    weatherMap.put("WindSpeed", WindSpeed);
                    weatherMap.put("AirPressure", AirPressure);
                    weatherMap.put("SunshineDuration", SunshineDuration);


                    weatherData.add(weatherMap);
                }
            }

            if (!weatherData.isEmpty()) {
                System.out.println(weatherData.get(0).get("StationName"));
            }

            model.addAttribute("weatherData", weatherData);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return "index";
    }

}
