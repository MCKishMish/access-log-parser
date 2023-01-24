import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Statistics {

    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    private HashSet<String> paths = new HashSet<>();

    private HashMap<String, Integer> osFrequency = new HashMap<>();

    public HashSet<String> getPaths() {
        return paths;
    }

    public Statistics() {
        totalTraffic=0;
        minTime=LocalDateTime.MAX;
        maxTime=LocalDateTime.MIN;
    }

    public void addEntry (LogEntry logEntry) {
        UserAgent userAgent = new UserAgent(logEntry.getUserAgent());
        String os = userAgent.getOperatingSystem();
        totalTraffic+= logEntry.getResponseSize();
        LocalDateTime logDateTime = logEntry.getRequestDateTime();
        if (logDateTime.isBefore(minTime)) minTime=logDateTime;
        if (logDateTime.isAfter(maxTime)) maxTime=logDateTime;
        if (logEntry.getResponseCode()==200) paths.add(logEntry.getPath());
        if (osFrequency.containsKey(os)) {
            osFrequency.put(os,osFrequency.get(os)+1);
        }
        else osFrequency.put(os,1);
    }

    public long getTrafficRate () {
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        System.out.println(osFrequency);
        return totalTraffic/hours;
    }

    public HashMap<String, Double> geOsProportion () {
        HashMap<String, Double> osProportion = new HashMap<>();
        for(Map.Entry entry: osFrequency.entrySet()) {
            double proportion = Double.parseDouble(entry.getValue().toString())/getOsCount();
            osProportion.put(entry.getKey().toString(),proportion);
        }
        return osProportion;
    }

    private int getOsCount () {
        int osCount = 0;
        for(Map.Entry entry: osFrequency.entrySet()) {
            osCount+=(int)entry.getValue();
        }
        return osCount;
    }
}
