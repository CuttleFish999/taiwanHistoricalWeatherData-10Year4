package dodo.taiwanweather.weather.service;

import java.util.List;
import java.util.Map;

public interface WeatherDateService {

//  api json
    List<Map<String, Object>> fetchStationData();
    List<Map<String, Object>> fetchWeatherData(String station);
}
