import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Statistics {

    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    private HashSet<String> existingPages = new HashSet<>();

    private HashSet<String> nonExistingPages = new HashSet<>();

    private HashMap<String, Integer> osFrequency = new HashMap<>();

    private HashMap<String, Integer> browserFrequency = new HashMap<>();

    public HashSet<String> getExistingPages() {
        return existingPages;
    }

    public HashSet<String> getNonExistingPages() {
        return nonExistingPages;
    }

    public Statistics() {
        totalTraffic = 0;
        minTime = LocalDateTime.MAX;
        maxTime = LocalDateTime.MIN;
    }

    public void addEntry(LogEntry logEntry) {
        UserAgent userAgent = new UserAgent(logEntry.getUserAgent());
        String os = userAgent.getOperatingSystem();
        String browser = userAgent.getBrowser();
        totalTraffic += logEntry.getResponseSize();
        LocalDateTime logDateTime = logEntry.getRequestDateTime();
        if (logDateTime.isBefore(minTime)) minTime = logDateTime;
        if (logDateTime.isAfter(maxTime)) maxTime = logDateTime;
        if (logEntry.getResponseCode() == 200) existingPages.add(logEntry.getPath());
        if (logEntry.getResponseCode() == 404) nonExistingPages.add(logEntry.getPath());
        if (osFrequency.containsKey(os)) {
            osFrequency.put(os, osFrequency.get(os) + 1);
        } else osFrequency.put(os, 1);

        if (browserFrequency.containsKey(browser)) {
            browserFrequency.put(browser, browserFrequency.get(browser) + 1);
        } else browserFrequency.put(browser, 1);
    }

    public long getTrafficRate() {
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        return totalTraffic / hours;
    }

    public HashMap<String, Double> getOsProportion() {
        HashMap<String, Double> osProportion = new HashMap<>();
        for (Map.Entry entry : osFrequency.entrySet()) {
            double proportion = Double.parseDouble(entry.getValue().toString()) / getOsCount();
            osProportion.put(entry.getKey().toString(), proportion);
        }
        return osProportion;
    }

    public HashMap<String, Double> getBrowserProportion() {
        HashMap<String, Double> browserProportion = new HashMap<>();
        for (Map.Entry entry : browserFrequency.entrySet()) {
            double proportion = Double.parseDouble(entry.getValue().toString()) / getBrowserCount();
            browserProportion.put(entry.getKey().toString(), proportion);
        }
        return browserProportion;
    }

    private int getOsCount() {
        int osCount = 0;
        for (Map.Entry entry : osFrequency.entrySet()) {
            osCount += (int) entry.getValue();
        }
        return osCount;
    }

    private int getBrowserCount() {
        int browserCount = 0;
        for (Map.Entry entry : browserFrequency.entrySet()) {
            browserCount += (int) entry.getValue();
        }
        return browserCount;
    }
}
