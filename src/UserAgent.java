//●     Создайте класс UserAgent по тому же принципу: с final-свойствами (полями), соответствующими свойствам, заданным в строке User-Agent (см. ниже),
//        и геттерами для этих свойств.
//
//        ●     Создайте также в классе UserAgent конструктор, который будет принимать в качестве параметра строку User-Agent и извлекать из неё свойства.
//        Из строки User-Agent необходимо извлекать два свойства: тип операционной системы (Windows, macOS или Linux)
//        и браузера (Edge, Firefox, Chrome, Opera или другой).
//        Для определения типа операционной системы и браузера воспользуйтесь инструкцией.

public class UserAgent {
    private final String operatingSystem;
    private final String browser;

    public UserAgent(String userAgent) {
        //должно быть вы не видели кода хуже, чем ниже, но все бывает в первый раз
        if (userAgent.contains("Firefox/")) browser = "Firefox";
        else if (userAgent.contains("Chrome/") && !userAgent.contains("OPR/") && !userAgent.contains("Edg/"))
            browser = "Chrome";
        else if (userAgent.contains("Safari/") && !userAgent.contains("Chrome/")) browser = "Safari";
        else if (userAgent.contains("Opera/") || userAgent.contains("Presto/") || userAgent.contains("OPR/"))
            browser = "Opera";
        else if (userAgent.contains("Edg/")) browser = "Edge";
        else if (userAgent.contains("IEMobile/")) browser = "Internet Explorer";
        else browser = "UNDEFINED";

        if (userAgent.contains("Windows")) operatingSystem = "Windows";
        else if (userAgent.contains("Macintosh") && userAgent.contains("Mac OS")) operatingSystem = "Mac OS";
        else if (userAgent.contains("Linux") && !userAgent.contains("Android")) operatingSystem = "Linux";
        else if (userAgent.contains("Linux") && userAgent.contains("Android")) operatingSystem = "Android";
        else if ((userAgent.contains("iPad") || userAgent.contains("iPhone")) && (userAgent.contains("Mobile")))
            operatingSystem = "iOS";
        else operatingSystem = "UNDEFINED";
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public String getBrowser() {
        return browser;
    }
}
