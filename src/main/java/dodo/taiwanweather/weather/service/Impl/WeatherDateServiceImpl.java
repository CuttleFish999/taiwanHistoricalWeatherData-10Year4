package dodo.taiwanweather.weather.service.Impl;
import dodo.taiwanweather.weather.service.WeatherDateService;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@Service
public class WeatherDateServiceImpl implements WeatherDateService {

    @Autowired
    private RestTemplate restTemplate;

//  本地
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

        public List<String> getStationNames() {
            List<String> stationNames = new ArrayList<>();
            String apiUrl = "https://opendata.cwa.gov.tw/api/v1/rest/datastore/O_A0001_001?Authorization=你的API金鑰";

            try {
                ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
                String jsonData = response.getBody();

                JSONObject jsonObject = new JSONObject(jsonData);
                JSONObject result = jsonObject.getJSONObject("result");
                JSONArray records = result.getJSONArray("records");

                for (int i = 0; i < records.length(); i++) {
                    JSONObject record = records.getJSONObject(i);
                    String stationName = record.getString("stationName");
                    stationNames.add(stationName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return stationNames;
        }

}
