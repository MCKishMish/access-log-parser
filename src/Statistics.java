import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Statistics {

    private long totalTraffic;
    private LocalDateTime minTime;

    private LocalDateTime lastSecond;
    private LocalDateTime maxTime;
    private double humanVisitsNumber;
    private double errorResponseCount;

    private HashSet<String> existingPages = new HashSet<>();

    private HashSet<String> nonExistingPages = new HashSet<>();

    private HashMap<String, Integer> osFrequency = new HashMap<>();

    private HashMap<String, Integer> browserFrequency = new HashMap<>();

    private HashMap<String, Integer> realPeopleIpCount = new HashMap<>();

    private HashMap<Integer, Integer> pickAttendanceCount = new HashMap<>();

    private int secondsSerialNumber;

    private HashSet<String> referrers = new HashSet<>();

    public HashSet<String> getExistingPages() {
        return existingPages;
    }

    public HashSet<String> getNonExistingPages() {
        return nonExistingPages;
    }

    public HashMap<Integer, Integer> getPickAttendanceCount() {
        return pickAttendanceCount;
    }

    public HashSet<String> getReferrers() {
        return referrers;
    }

    public Statistics() {
        totalTraffic = 0;
        minTime = LocalDateTime.MAX;
        maxTime = LocalDateTime.MIN;
        lastSecond = LocalDateTime.MIN;
        humanVisitsNumber = 0;
        errorResponseCount = 0;
        secondsSerialNumber = 1;
    }

    public void addEntry(LogEntry logEntry) throws MalformedURLException {
        UserAgent userAgent = new UserAgent(logEntry.getUserAgent());
        if (!UserAgent.isBot(logEntry.getUserAgent())) {
            humanVisitsNumber++;
            if (lastSecond != LocalDateTime.MIN && ChronoUnit.SECONDS.between(lastSecond, logEntry.getRequestDateTime()) == 0) {
                pickAttendanceCount.put(secondsSerialNumber, pickAttendanceCount.get(secondsSerialNumber) + 1);
                lastSecond = logEntry.getRequestDateTime();
            } else if (pickAttendanceCount.isEmpty()) {
                pickAttendanceCount.put(secondsSerialNumber, 1);
                lastSecond = logEntry.getRequestDateTime();
            } else {
                secondsSerialNumber++;
                pickAttendanceCount.put(secondsSerialNumber, 1);
                lastSecond = logEntry.getRequestDateTime();
            }
        }
        String os = userAgent.getOperatingSystem();
        String browser = userAgent.getBrowser();
        String ip = logEntry.getIpAddr();
        String referer = logEntry.getReferer();
        totalTraffic += logEntry.getResponseSize();
        if (Integer.toString(logEntry.getResponseCode()).startsWith("4") || Integer.toString(logEntry.getResponseCode()).startsWith("5"))
            errorResponseCount++;
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
        if (!UserAgent.isBot(logEntry.getUserAgent()) && realPeopleIpCount.containsKey(ip)) {
            realPeopleIpCount.put(ip, realPeopleIpCount.get(ip) + 1);
        } else if (!UserAgent.isBot(logEntry.getUserAgent())) realPeopleIpCount.put(ip, 1);
        if (referer != null && !referer.equals("\"-\"")) {
            int begin = referer.indexOf('/', referer.indexOf('/')+1);
            int end = referer.indexOf('/', begin+1);
            referrers.add(referer.substring(begin+1, end));
        }
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

    public double getAverageVisitsNumberPerHour() {
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        return hours / humanVisitsNumber;
    }

    public double getAverageFailedRequestsNumberPerHour() {
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        return hours / errorResponseCount;
    }

    public double getAverageAttendancePerUser() {
        return humanVisitsNumber / realPeopleIpCount.size();
    }

    public int getPickAttendanceInSomeSecond() {
        return pickAttendanceCount.values().stream().max(Comparator.naturalOrder()).get();
    }

    public int getMaxAttendanceByOneUser () {
        return realPeopleIpCount.values().stream().max(Comparator.naturalOrder()).get();
    }
}
