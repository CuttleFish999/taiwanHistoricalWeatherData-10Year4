package dodo.taiwanweather.weather.dto;

public class HourlyUnits {
    private String time; // 時間
    private String temperature_2m; // 2米攝氏溫度温度

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature_2m() {
        return temperature_2m;
    }

    public void setTemperature_2m(String temperature_2m) {
        this.temperature_2m = temperature_2m;
    }
}
