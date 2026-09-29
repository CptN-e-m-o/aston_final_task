package test;

import app.Application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * Ручные сквозные (интеграционные) тесты основного цикла Application.
 *
 * Идея: подменяем System.in заранее подготовленной последовательностью
 * "нажатий" пользователя (включая пункт меню "Выход" в конце, иначе цикл
 * никогда не завершится) и подменяем System.out, чтобы проверить,
 * что в выводе появилось ожидаемое.
 *
 * Запуск: test.ApplicationTest.main(null).
 */
public class ApplicationTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testManualCreationAndSort();
        testInvalidMenuChoiceThenExitDoesNotCrash();
        testOccurrenceCounting();
        testWriteResultToFileAppendsWithoutErasing();
        testWritingWithNoResultYetShowsMessage();

        System.out.println();
        System.out.println("Пройдено: " + passed + ", провалено: " + failed);
    }

    /**
     * Сценарий: создать коллекцию из 2 студентов вручную, отсортировать
     * обычным способом по среднему баллу, убедиться, что результат выведен
     * в отсортированном порядке, затем выйти.
     */
    private static void testManualCreationAndSort() {
        String simulatedInput = String.join("\n",
                "1",              // создать коллекцию
                "1",              // способ: вручную
                "2",              // количество студентов
                "101", "4.0", "111",  // студент 1
                "102", "5.0", "222",  // студент 2
                "3",              // отсортировать коллекцию
                "2",              // поле: средний балл
                "1",              // алгоритм: Quick Sort (единственный в реестре)
                "1",              // режим: обычная
                "6"               // выход
        ) + "\n";

        String output = runApplication(simulatedInput);

        check("Сценарий 'ручной ввод + сортировка': коллекция создана",
                output.contains("Коллекция создана. Студентов: 2"));
        check("Сценарий 'ручной ввод + сортировка': порядок в результате верный "
                        + "(меньший средний балл — первым)",
                output.indexOf("averageGrade=4.0") < output.indexOf("averageGrade=5.0")
                        && output.contains("Результат сортировки"));
    }

    /**
     * Проверяет, что некорректный пункт главного меню не прерывает работу
     * программы: она переспрашивает и продолжает цикл до явного выхода.
     */
    private static void testInvalidMenuChoiceThenExitDoesNotCrash() {
        String simulatedInput = String.join("\n",
                "0",   // некорректный пункт меню (вне диапазона 1-6)
                "abc", // некорректный (не число) пункт меню
                "6"    // выход
        ) + "\n";

        String output = runApplication(simulatedInput);

        check("Некорректный пункт меню не приводит к падению программы",
                output.contains("Работа программы завершена."));
    }

    /**
     * Сценарий: создать коллекцию из 2 одинаковых студентов и посчитать
     * количество вхождений третьего с теми же данными.
     */
    private static void testOccurrenceCounting() {
        String simulatedInput = String.join("\n",
                "1",             // создать коллекцию
                "1",             // способ: вручную
                "2",             // количество студентов
                "101", "4.0", "111",  // студент 1
                "101", "4.0", "111",  // студент 2 (такой же)
                "4",             // подсчёт вхождений
                "101", "4.0", "111",  // искомый студент
                "6"              // выход
        ) + "\n";

        String output = runApplication(simulatedInput);

        check("Подсчёт вхождений находит обоих одинаковых студентов",
                output.contains("Найдено вхождений: 2"));
    }

    /**
     * Доп. задание №2: результат сортировки дважды дописывается в один
     * и тот же файл. Проверяем, что запись действительно в режиме append —
     * первая запись не стирается второй, обе оказываются в файле.
     */
    private static void testWriteResultToFileAppendsWithoutErasing() {
        Path tempFile;
        try {
            tempFile = Files.createTempFile("aston-result", ".txt");
            Files.writeString(tempFile, "старая запись, которая уже была в файле" + System.lineSeparator());
        } catch (IOException e) {
            failed++;
            System.out.println("[FAIL] Запись результата в файл: не удалось создать временный файл");
            return;
        }

        try {
            String simulatedInput = String.join("\n",
                    "1", "1", "1",              // создать коллекцию: вручную, 1 студент
                    "101", "4.0", "111",        // данные студента
                    "3", "1", "1", "1",         // сортировка: поле группа, Quick Sort, обычная
                    "5", tempFile.toString(),   // записать результат в файл (1-й раз)
                    "5", tempFile.toString(),   // записать результат в файл (2-й раз)
                    "6"                         // выход
            ) + "\n";

            String output = runApplication(simulatedInput);
            check("Запись результата в файл: подтверждение выведено в консоль",
                    output.contains("Результат добавлен в файл: " + tempFile));

            String fileContent = Files.readString(tempFile);
            check("Запись результата в файл: старое содержимое файла сохранено (append, не перезапись)",
                    fileContent.contains("старая запись, которая уже была в файле"));
            check("Запись результата в файл: новый результат дописан",
                    fileContent.contains("groupNumber='101'"));

            long headerCount = fileContent.lines()
                    .filter(line -> line.contains("Отсортированная коллекция"))
                    .count();
            check("Запись результата в файл: обе записи (два вызова) оказались в файле",
                    headerCount == 2);
        } catch (IOException e) {
            failed++;
            System.out.println("[FAIL] Запись результата в файл: " + e.getMessage());
        } finally {
            try {
                Files.deleteIfExists(tempFile);
            } catch (IOException ignored) {
                // временный файл — не критично, если не удалось удалить
            }
        }
    }

    /**
     * Если ни сортировка, ни подсчёт вхождений ещё не выполнялись,
     * попытка записи в файл должна вывести понятное сообщение,
     * а не упасть и не создать пустой/некорректный файл.
     */
    private static void testWritingWithNoResultYetShowsMessage() {
        String simulatedInput = String.join("\n",
                "5",  // записать результат в файл — но результата ещё нет
                "6"   // выход
        ) + "\n";

        String output = runApplication(simulatedInput);

        check("Запись без результата: показано понятное сообщение вместо ошибки",
                output.contains("Нет результата для записи"));
    }

    private static String runApplication(String simulatedInput) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();

        try {
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(capturedOutput, true, StandardCharsets.UTF_8));

            Application application = new Application(new Scanner(System.in));
            application.run();
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        return capturedOutput.toString(StandardCharsets.UTF_8);
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
