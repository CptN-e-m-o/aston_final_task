package test;

import app.InputReader;

import java.util.Scanner;

/**
 * Простые тесты для InputReader без сторонних библиотек — как договорились
 * в задании, достаточно ручных проверок в отдельном классе.
 *
 * Каждый тест эмулирует ввод пользователя через Scanner поверх заранее
 * заданной строки (в т.ч. с ошибочными значениями перед корректным),
 * чтобы проверить, что при некорректном вводе метод переспрашивает,
 * а не падает и не возвращает неверный результат.
 *
 * Запуск: test.InputReaderTest.main(null).
 */
public class InputReaderTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testReadIntValidOnFirstTry();
        testReadIntRetriesAfterNonNumber();
        testReadIntRetriesAfterOutOfRange();
        testReadDoubleRetriesAfterInvalidValue();
        testReadNonEmptyStringRetriesAfterBlank();
        testReadMenuChoiceRespectsBounds();

        System.out.println();
        System.out.println("Пройдено: " + passed + ", провалено: " + failed);
    }

    private static void testReadIntValidOnFirstTry() {
        InputReader reader = readerFor("5\n");
        check("readInt: корректное значение с первой попытки",
                reader.readInt("prompt: ", 1, 10) == 5);
    }

    private static void testReadIntRetriesAfterNonNumber() {
        InputReader reader = readerFor("abc\n7\n");
        check("readInt: повторный запрос после нечислового ввода",
                reader.readInt("prompt: ", 1, 10) == 7);
    }

    private static void testReadIntRetriesAfterOutOfRange() {
        InputReader reader = readerFor("100\n-5\n3\n");
        check("readInt: повторный запрос после значения вне диапазона",
                reader.readInt("prompt: ", 1, 10) == 3);
    }

    private static void testReadDoubleRetriesAfterInvalidValue() {
        InputReader reader = readerFor("not-a-number\n9.9\n4.5\n");
        check("readDouble: повторный запрос после некорректного значения",
                reader.readDouble("prompt: ", 1.0, 5.0) == 4.5);
    }

    private static void testReadNonEmptyStringRetriesAfterBlank() {
        InputReader reader = readerFor("   \n\nгруппа-101\n");
        check("readNonEmptyString: повторный запрос после пустой строки",
                "группа-101".equals(reader.readNonEmptyString("prompt: ")));
    }

    private static void testReadMenuChoiceRespectsBounds() {
        InputReader reader = readerFor("0\n4\n2\n");
        check("readMenuChoice: принимает только значения из диапазона меню",
                reader.readMenuChoice(3) == 2);
    }

    private static InputReader readerFor(String simulatedInput) {
        return new InputReader(new Scanner(simulatedInput));
    }

    private static void check(String testName, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("[OK]   " + testName);
        } else {
            failed++;
            System.out.println("[FAIL] " + testName);
        }
    }
}
