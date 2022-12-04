import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int guessCounter=1;
        while (true) {
            System.out.println("Введите путь к файлу:");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (!fileExists) {
                System.out.println("Файл не существует");
                continue;
            }
            else if (isDirectory) {
                System.out.println("Указанный путь является путём к папке");
                continue;
            }
            else {
                System.out.println("Путь указан верно");
                System.out.println("Это файл номер " + guessCounter);
                guessCounter++;
            }
        }
    }
}
