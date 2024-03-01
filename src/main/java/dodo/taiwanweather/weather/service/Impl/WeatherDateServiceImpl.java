package dodo.taiwanweather.weather.service.Impl;

import dodo.taiwanweather.weather.service.WeatherDateService;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherDateServiceImpl implements WeatherDateService {

    @Override
    public List<Map<String, Object>> fetchStationData() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://opendata.cwa.gov.tw/api/v1/rest/datastore/C-B0027-001?Authorization=CWA-2FD4BAFB-A6F7-4127-9D46-F2A699F51C10&format=JSON&weatherElement=AirPressure&Month=1"))
                .header("accept", "application/json")
                .build();

        List<Map<String, Object>> weatherData = new ArrayList<>();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray locations = jsonObject.getJSONObject("records").getJSONObject("data").getJSONObject("surfaceObs").getJSONArray("location");

            for (int i = 0; i < locations.length(); i++) {
                JSONObject location = locations.getJSONObject(i);
                JSONObject stationObject = location.getJSONObject("station");
                String stationName = stationObject.getString("StationName");
                String stationID = stationObject.getString("StationID");

                Map<String, Object> weatherMap = new HashMap<>();
                weatherMap.put("StationName", stationName);
                weatherMap.put("StationID", stationID);
                weatherData.add(weatherMap);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return weatherData;
    }


    @Override
    public List<Map<String, Object>> fetchWeatherData() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://opendata.cwa.gov.tw/api/v1/rest/datastore/C-B0027-001?Authorization=CWA-2FD4BAFB-A6F7-4127-9D46-F2A699F51C10&limit=1&format=JSON"))
                .header("accept", "application/json")
                .build();

        List<Map<String, Object>> weatherData = new ArrayList<>();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            JSONObject jsonObject = new JSONObject(responseBody);
            JSONArray locations = jsonObject.getJSONObject("records").getJSONObject("data").getJSONObject("surfaceObs").getJSONArray("location");

            for (int i = 0; i < locations.length(); i++) {
                JSONObject location = locations.getJSONObject(i);
                JSONObject station = location.getJSONObject("station");
                String stationName = station.getString("StationName");
                String stationID = station.getString("StationID");

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
                JSONArray monthlyWindSpeed = windSpeed.getJSONArray("monthly");
                JSONArray monthlyAirPressure = airPressure.getJSONArray("monthly");
                JSONArray monthlySunshineDuration = sunshineDuration.getJSONArray("monthly");

                for (int monthCount = 0; monthCount < 12; monthCount++) {
                    JSONObject firstMonthTemp = monthlyTemp.getJSONObject(monthCount);
                    JSONObject firstMonthHumidity = monthlyHumidity.getJSONObject(monthCount);
                    JSONObject firstMonthPrecipitation = monthlyPrecipitation.getJSONObject(monthCount);
                    JSONObject firstMonthWindSpeed = monthlyWindSpeed.getJSONObject(monthCount);
                    JSONObject firstMonthAirPressure = monthlyAirPressure.getJSONObject(monthCount);
                    JSONObject firstMonthSunshineDuration = monthlySunshineDuration.getJSONObject(monthCount);

                    int stationStartYear = airTemperature.getInt("StationStartYear");
                    int stationEndYear = airTemperature.getInt("StationEndYear");
                    Integer month = firstMonthTemp.has("Month") ? firstMonthTemp.getInt("Month") : null;
                    double tempMean = firstMonthTemp.has("Mean") ? firstMonthTemp.getDouble("Mean") : Double.NaN;
                    double humidityMean = firstMonthHumidity.has("Mean") ? firstMonthHumidity.getDouble("Mean") : Double.NaN;
                    double precipitationAmount = firstMonthPrecipitation.has("Accumulation") ? firstMonthPrecipitation.getDouble("Accumulation") : Double.NaN;
                    double windSpeedMean = firstMonthWindSpeed.has("Mean") ? firstMonthWindSpeed.getDouble("Mean") : Double.NaN;
                    double airPressureMean = firstMonthAirPressure.has("Mean") ? firstMonthAirPressure.getDouble("Mean") : Double.NaN;
                    double sunshineDurationTotal = firstMonthSunshineDuration.has("Total") ? firstMonthSunshineDuration.getDouble("Total") : Double.NaN;

                    Map<String, Object> weatherMap = new HashMap<>();
                    if (monthCount == 0) {
                        weatherMap.put("StationName", stationName);
                        weatherMap.put("StationID", stationID);
                    } else {
                        weatherMap.put("StationName", "");
                        weatherMap.put("StationID", "");
                    }

                    weatherMap.put("StationStartYear", stationStartYear);
                    weatherMap.put("StationEndYear", stationEndYear);
                    weatherMap.put("Month", month);
                    weatherMap.put("TemperatureMean", tempMean);
                    weatherMap.put("HumidityMean", humidityMean);
                    weatherMap.put("Precipitation", precipitationAmount);
                    weatherMap.put("WindSpeed", windSpeedMean);
                    weatherMap.put("AirPressure", airPressureMean);
                    weatherMap.put("SunshineDuration", sunshineDurationTotal);

                    weatherData.add(weatherMap);
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("執行失敗", e);
        }

        return weatherData;
    }
}
