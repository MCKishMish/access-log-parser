import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите 1 число:");
        int firstNumber = new Scanner(System.in).nextInt();
        System.out.println("Введите 2 число:");
        int secondNumber = new Scanner(System.in).nextInt();
        int sum = firstNumber+secondNumber;
        int dif = firstNumber-secondNumber;
        int product = firstNumber*secondNumber;
        double quotient = (double)firstNumber/secondNumber;
        System.out.println("Сумма чисел = " + sum);
        System.out.println("Разность чисел = " + dif);
        System.out.println("Произведение чисел = " + product);
        System.out.println("Частное чисел = " + quotient);
    }
}
