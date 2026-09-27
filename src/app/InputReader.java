package app;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Утилита для чтения и валидации пользовательского ввода из консоли.
 *
 * Вся обработка ошибок ввода (не число, число вне диапазона, пустая строка)
 * находится здесь: при некорректном значении метод печатает сообщение
 * об ошибке и запрашивает ввод заново, не прерывая работу программы.
 *
 * Scanner передаётся через конструктор, а не создаётся внутри класса,
 * чтобы класс можно было протестировать на заранее заданном наборе строк.
 */
public class InputReader {

    private final Scanner scanner;

    public InputReader(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner cannot be null");
        }
        this.scanner = scanner;
    }

    /**
     * Читает целое число в диапазоне [min, max] включительно.
     * При ошибке ввода запрашивает значение заново.
     */
    public int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = nextLine();
            try {
                int value = Integer.parseInt(line.trim());
                if (value < min || value > max) {
                    System.out.println("Значение должно быть в диапазоне от " + min
                            + " до " + max + ". Повторите ввод.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Некорректное целое число. Повторите ввод.");
            }
        }
    }

    /**
     * Читает вещественное число в диапазоне [min, max] включительно.
     * При ошибке ввода запрашивает значение заново.
     */
    public double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = nextLine();
            try {
                double value = Double.parseDouble(line.trim());
                if (!Double.isFinite(value) || value < min || value > max) {
                    System.out.println("Значение должно быть в диапазоне от " + min
                            + " до " + max + ". Повторите ввод.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Некорректное число. Повторите ввод.");
            }
        }
    }

    /**
     * Читает непустую строку (пробелы по краям обрезаются).
     * При пустой строке запрашивает значение заново.
     */
    public String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = nextLine().trim();
            if (line.isEmpty()) {
                System.out.println("Строка не может быть пустой. Повторите ввод.");
                continue;
            }
            return line;
        }
    }

    /**
     * Читает выбор пункта меню — целое число от 1 до optionsCount включительно.
     */
    public int readMenuChoice(int optionsCount) {
        return readInt("Ваш выбор: ", 1, optionsCount);
    }

    private String nextLine() {
        try {
            if (!scanner.hasNextLine()) {
                throw new NoSuchElementException("Ввод неожиданно закончился");
            }
            return scanner.nextLine();
        } catch (NoSuchElementException | IllegalStateException e) {
            // Поток ввода закрыт или недоступен — дальше продолжать нельзя,
            // пробрасываем как непроверяемое исключение более высокого уровня.
            throw new IllegalStateException("Поток ввода недоступен", e);
        }
    }
}
