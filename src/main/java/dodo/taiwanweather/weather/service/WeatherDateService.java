package dodo.taiwanweather.weather.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherDateService {

    public static void main(String[] args) throws IOException {
        // 設定輸出檔案
        PrintWriter out = new PrintWriter("weather.csv");

        // 設定查詢參數
        String startDate = "2014-01-01";
        String endDate = "2024-02-29";
        String stationId = "C0A880";

        // 建立 HTTP 連線
        URL url = new URL("https://data.gov.tw/api/v1/resource/CWB-19092-001?startDate=" + startDate + "&endDate=" + endDate + "&stationId=" + stationId);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");

        // 讀取回應
        InputStream inputStream = connection.getInputStream();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            // 解析 JSON 資料
            // ...

            // 輸出 CSV 資料
            out.println(line);
        }

        // 關閉輸出檔案
        out.close();
    }
}
