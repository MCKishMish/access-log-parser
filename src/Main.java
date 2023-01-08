import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int guessCounter = 1;
        while (true) {
            System.out.println("Введите путь к файлу:");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            int stringCount=0;
            int maxLength=0;
            ArrayList<String> list = new ArrayList<String>();
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
                    while ((line = reader.readLine()) != null) {
                        list.add(line);
                        int length = line.length();
                        if (length>maxLength) maxLength=length;
                        stringCount++;
                        if (length>1024) throw new TooLongStringException("string length more than 1024 symbols");
                    }
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.out.println("Минимальная длина строки: " + minLength(list));
                System.out.println("Максимальная длина строки: " + maxLength);
                System.out.println("Общее количество строк: " + stringCount);
            }
        }

    }

    public static int minLength (ArrayList<String> list) {
        int minLength;
        if (list.size()==0) return 0;
        else minLength=list.get(0).length();
        for (String s: list) {
            if (s.length()<minLength) minLength=s.length();
        }
        return minLength;
    }
}
