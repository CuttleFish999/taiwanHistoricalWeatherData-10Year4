package dodo.taiwanweather.weather.service.Impl;
import dodo.taiwanweather.weather.service.WeatherDateService;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class WeatherDateServiceImpl implements WeatherDateService {

    public List<String> getStationNames(String filePath) {
        List<String> stationNames = new ArrayList<>();

        StringBuilder jsonData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return stationNames;
        }

        JSONObject jsonObject = new JSONObject(jsonData.toString());
        JSONObject records = jsonObject.getJSONObject("records");
        JSONObject data = records.getJSONObject("data");
        JSONObject surfaceObs = data.getJSONObject("surfaceObs");
        JSONArray locations = surfaceObs.getJSONArray("location");

        for (int i = 0; i < locations.length(); i++) {
            JSONObject location = locations.getJSONObject(i);
            JSONObject station = location.getJSONObject("station");
            String stationName = station.getString("StationName");
            stationNames.add(stationName);
        }

        return stationNames;
    }
}
