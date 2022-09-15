package Clock;

import com.google.gson.Gson;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author Shang Zemo on 2022/5/24
 */
public class BaiDuWeather {

    static String decodeWeather(String line) {

        line = line.replace(line.substring(0, line.indexOf('{')), "");
        line = line.replace(";", "");
        line = line.replaceAll(",", ",\n");

        while (line.contains("\\u")) {
            int handleIndex = line.indexOf("\\u");
            char unicodeChar = (char) Integer.parseInt(line.substring(handleIndex + 2, handleIndex + 6), 16);
            line = line.replace(line.substring(handleIndex, handleIndex + 6), "" + unicodeChar);
        }

        return line;
    }

    static String getInfo(double timeout) {
        StringBuilder diyWeatherInfo = new StringBuilder();
        try {

            URL url = new URL("https://weathernew.pae.baidu.com/weathernew/pc?query=%E6%B9%96%E5%8C%97%E6%AD%A6%E6%B1%89%E5%A4%A9%E6%B0%94&srcid=4982");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
            connection.setConnectTimeout((int) (timeout * 1000));
            connection.connect();

            String[] probablyWotChange = {"humidity",//0
                    "update_time",
                    "publish_time",
                    "bodytemp_info",
                    "temperature",
                    "wind_power",//5
                    "uv_info",
                    "wind_direction",
                    "uv",
                    "site",
                    "visibility",//10
                    "wind_direction_num",
                    "pressure",
                    "uv_num",
                    "weather",
                    "dew_temperature",//15
                    "prec_monitor_time",
                    "precipitation_type",
                    "wind_power_num",
                    "precipitation",
                    "real_feel_temperature",//20
                    "weather_day",
                    "weather_night",
                    "wind_direction_day",
                    "wind_power_day",
                    "wind_direction_night",//25
                    "wind_power_night",
                    "temperature_night",
                    "temperature_day"};//28

            Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.startsWith("data[\"weather\"]=")) {
                    line = decodeWeather(line);

                    WA wa = new Gson().fromJson(line, WA.class);
                    System.out.println(wa);

                    diyWeatherInfo.append(wa.real_feel_temperature);
                    diyWeatherInfo.append("¡æ ");
                    diyWeatherInfo.append(wa.weather);
                    diyWeatherInfo.append(" °×Ìì:");
                    diyWeatherInfo.append(wa.temperature_day);
                    diyWeatherInfo.append("¡æ ");
                    diyWeatherInfo.append(wa.weather_day);
                    diyWeatherInfo.append(" ÍíÉÏ:");
                    diyWeatherInfo.append(wa.temperature_night);
                    diyWeatherInfo.append("¡æ ");
                    diyWeatherInfo.append(wa.weather_night);
                    diyWeatherInfo.append(" (");
                    diyWeatherInfo.append(wa.publish_time);
                    diyWeatherInfo.append(")");

                    break;
                }
            }

        } catch (ConnectException | SSLHandshakeException | UnknownHostException | SocketTimeoutException e) {
            return "\nsbÐ£Ô°Íø\n";
        } catch (IOException i) {
            i.printStackTrace();
        }
        return diyWeatherInfo.toString();
    }

    class WA {
        String weather;
        String real_feel_temperature;
        String publish_time;
        String weather_day;
        String temperature_day;
        String weather_night;
        String temperature_night;

        @Override
        public String toString() {
            return "WA{" +
                    "weather='" + weather + '\'' +
                    ", real_feel_temperature='" + real_feel_temperature + '\'' +
                    ", publish_time='" + publish_time + '\'' +
                    ", weather_day='" + weather_day + '\'' +
                    ", temperature_day='" + temperature_day + '\'' +
                    ", weather_night='" + weather_night + '\'' +
                    ", temperature_night='" + temperature_night + '\'' +
                    '}';
        }
    }
}
