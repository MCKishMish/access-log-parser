import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

//Создайте класс LogEntry со свойствами (полями), соответствующими компонентам строк лог-файла: IP-адресу, дате и времени запроса, методу запроса,
//        пути запроса, коду ответа, размеру отданных сервером данных, referer, а также User-Agent.
//        Возможные методы HTTP-запросов положите в enum. Типы остальных полей определите самостоятельно.
public class LogEntry {
    private final String ipAddr;
    private final LocalDateTime requestDateTime;
    private final HttpMethod httpMethod;
    private final String path;
    private final int responseCode;
    private final int responseSize;
    private final String referer;
    private final String userAgent;

    public LogEntry(String logLine) {
        String[] logParts = logLine.split(" ");
        ipAddr = logParts[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss").withLocale(Locale.ENGLISH);
        requestDateTime = LocalDateTime.parse(logParts[3].substring(1), formatter);
        httpMethod = HttpMethod.valueOf(logParts[5].substring(1));
        path = logParts[6];
        responseCode = Integer.parseInt(logParts[8]);
        responseSize = Integer.parseInt(logParts[9]);
        referer = logParts[10];
        userAgent = getUserAgentFromLogLine(logParts);
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public LocalDateTime getRequestDateTime() {
        return requestDateTime;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public String getReferer() {
        return referer;
    }

    public String getUserAgent() {
        return userAgent;
    }

    private String getUserAgentFromLogLine(String[] logParts) {
        String s = "";
        for (int i = 11; i < logParts.length; i++) {
            s += logParts[i] + " ";
        }
        s = s.trim();
        return s;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "ipAddr='" + ipAddr + '\'' +
                ", requestDateTime=" + requestDateTime +
                ", httpMethod=" + httpMethod +
                ", path='" + path + '\'' +
                ", responseCode=" + responseCode +
                ", responseSize=" + responseSize +
                ", referer='" + referer + '\'' +
                ", userAgent='" + userAgent + '\'' +
                '}';
    }
}
