package dodo.taiwanweather.weather.json;

import org.json.JSONObject;
import org.json.JSONArray;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class JsonFileProcessor{
    public static void main(String[] args) {
        String filePath = "C:\\Users\\apple\\OneDrive\\桌面\\taiwanHistoricalWeatherData-10Year4\\TaiwanHistoricalWeatherData-10Year\\src\\main\\java\\dodo\\taiwanweather\\weather\\json\\response_1709197604777.json";
        Integer count = 1;


        StringBuilder jsonData = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonData.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
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
            System.out.println(count++ + ": " + stationName);
        }
    }
}
