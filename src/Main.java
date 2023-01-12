import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        int guessCounter = 1;
        while (true) {
            System.out.println("Введите путь к файлу:");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            double stringCount = 0;
            if (!fileExists) {
                System.out.println("Файл не существует");
            } else if (isDirectory) {
                System.out.println("Указанный путь является путём к папке");
            } else {
                System.out.println("Путь указан верно");
                System.out.println("Это файл номер " + guessCounter);
                guessCounter++;
                try (FileReader fileReader = new FileReader(path);
                     BufferedReader reader =
                             new BufferedReader(fileReader)) {
                    String line;
                    double yandexCount = 0;
                    double googleCount = 0;
                    Statistics statistics = new Statistics();
                    while ((line = reader.readLine()) != null) {
                        int length = line.length();
                        stringCount++;
                        if (length > 1024) throw new TooLongStringException("string length more than 1024 symbols");
                        LogEntry logEntry = new LogEntry(line);
                        //System.out.println(logEntry);
                        statistics.addEntry(logEntry);
                        //String firstBracketsFromUserAgent = getFirstBracketsFromUserAgent(logEntry.getUserAgent());
                        //List<String> allBracketsFromUserAgent = getAllBracketsFromUserAgent(userAgent);
                        //String botName = getBotNameFromUserAgentFirstBrackets(firstBracketsFromUserAgent);
                        if (logEntry.getUserAgent().contains("YandexBot/")) yandexCount++;
                        if (logEntry.getUserAgent().contains("Googlebot/")) googleCount++;
                    }
                    System.out.println("Общее количество строк: " + stringCount);
                    System.out.println("YandexBot percentage: " + (yandexCount / stringCount) * 100);
                    System.out.println("Googlebot percentage: " + (googleCount / stringCount) * 100);
                    System.out.println("Средний объём трафика сайта за час: " + statistics.getTrafficRate());
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public static String getUserAgent(String[] logs) {
        String s = "";
        for (int i = 11; i < logs.length; i++) {
            s += logs[i] + " ";
        }
        s = s.trim();
        return s;
    }

    public static String getFirstBracketsFromUserAgent(String userAgent) {
        int begin = 0;
        int end = 0;
        for (int i = 0; i < userAgent.length(); i++) {
            if (userAgent.charAt(i) == '(') begin = i + 1;
            if (userAgent.charAt(i) == ')') {
                end = i;
                break;
            }
        }
        return userAgent.substring(begin, end);
    }

    public static List<String> getAllBracketsFromUserAgent(String userAgent) {
        List<String> lst = new ArrayList<>();
        int begin = 0;
        int end = 0;
        for (int i = 0; i < userAgent.length(); i++) {
            if (userAgent.charAt(i) == '(') begin = i + 1;
            if (userAgent.charAt(i) == ')') end = i;
            if (begin != 0 && end != 0) {
                System.out.println(userAgent);
                lst.add(userAgent.substring(begin, end));
                begin = 0;
                end = 0;
            }
        }
        System.out.println(lst);
        return lst;
    }

    public static String getBotNameFromUserAgentFirstBrackets(String firstBracketsFromUserAgent) {
        String[] parts = firstBracketsFromUserAgent.split(";");
        String fragment = "";
        if (parts.length >= 2) {
            fragment = parts[1];
        }
        String secondPart = fragment.replaceAll(" ", "");
        if (secondPart.length() == 0 || !secondPart.contains("/")) return "";
        String botName = "";
        int i = 0;
        while (secondPart.charAt(i) != '/') {
            botName += secondPart.charAt(i);
            i++;
        }
        return botName;
    }
}
