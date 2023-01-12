//Создайте класс для расчётов статистики — Statistics. У этого класса должен быть конструктор без параметров, в котором должны инициализироваться переменные класса.
//
//        ●     Добавьте в класс Statistics метод addEntry, принимающий в качестве параметра объект класса LogEntry.
//
//        ●     Реализуйте в классе Statistics подсчёт среднего объёма трафика сайта за час. Для этого:
//
//        создайте у класса свойство (поле) int totalTraffic, в которое в методе addEntry добавляйте объём данных, отданных сервером;
//        создайте свойства (поля) minTime и maxTime класса LocalDateTime и заполняйте их в методе addEntry,
//        если время в добавляемой записи из лога меньше minTime или больше maxTime соответственно;
//        реализуйте в классе метод getTrafficRate, в котором вычисляйте разницу между maxTime и minTime в часах
//        и делите общий объём трафика на эту разницу.
//        ●     Сделайте коммит в ветку master вашего репозитория access-log-parser.

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Statistics {

    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    public Statistics() {
        totalTraffic=0;
        minTime=LocalDateTime.MAX;
        maxTime=LocalDateTime.MIN;
    }

    public void addEntry (LogEntry logEntry) {
        totalTraffic+= logEntry.getResponseSize();
        LocalDateTime logDateTime = logEntry.getRequestDateTime();
        if (logDateTime.isBefore(minTime)) minTime=logDateTime;
        if (logDateTime.isAfter(maxTime)) maxTime=logDateTime;
    }

    public long getTrafficRate () {
        long hours = ChronoUnit.HOURS.between(minTime, maxTime);
        return totalTraffic/hours;
    }
}
