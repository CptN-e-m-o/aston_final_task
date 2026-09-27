package app;

import datasource.DataSource;
import datasource.FileStudentSource;
import datasource.ManualStudentSource;
import datasource.RandomStudentSource;
import model.Student;
import occurrence.OccurrenceCounterService;
import occurrence.SimpleOccurrenceCounterService;
import strategy.EvenOnlySortDecorator;
import strategy.SortStrategy;
import strategy.SortStrategyRegistry;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Консольное приложение: основной цикл, меню, обработка ошибок ввода.
 *
 * Это зона ответственности Человека 4. Класс не содержит бизнес-логику
 * сортировки, источников данных или подсчёта вхождений — она находится
 * в соответствующих пакетах (strategy, datasource, occurrence) и сюда
 * только вызывается. Main остаётся минимальным и лишь запускает run().
 *
 * Выход из бесконечного цикла программы — только через пункт меню "Выход".
 * Ошибочный ввод пользователя обрабатывается через try-catch внутри
 * InputReader и здесь, в run(), — на верхнем уровне, чтобы неожиданная
 * ошибка не могла уронить всю программу.
 *
 * Запись результатов в файл (доп. задание №2) сюда намеренно не включена —
 * будет добавлена отдельно.
 */
public class Application {

    private static final int MAX_STUDENTS = 10_000;

    private final InputReader inputReader;
    private final SortStrategyRegistry sortStrategyRegistry;
    private final OccurrenceCounterService<Student> occurrenceCounterService;

    private List<Student> originalStudents = new ArrayList<>();

    public Application(Scanner scanner) {
        this.inputReader = new InputReader(scanner);
        this.sortStrategyRegistry = new SortStrategyRegistry();
        this.occurrenceCounterService = new SimpleOccurrenceCounterService<>();
    }

    /**
     * Запускает основной бесконечный цикл приложения.
     */
    public void run() {
        System.out.println("=== Сортировка студентов ===");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = inputReader.readMenuChoice(5);

            try {
                switch (choice) {
                    case 1:
                        createCollection();
                        break;
                    case 2:
                        showOriginalCollection();
                        break;
                    case 3:
                        sortAndShow();
                        break;
                    case 4:
                        countOccurrences();
                        break;
                    case 5:
                        running = false;
                        break;
                    default:
                        System.out.println("Неизвестный пункт меню.");
                }
            } catch (Exception e) {
                // Любая непредвиденная ошибка в бизнес-логике не должна
                // ронять программу — печатаем сообщение и продолжаем цикл.
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }

        System.out.println("Работа программы завершена.");
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("1. Создать новую коллекцию студентов");
        System.out.println("2. Показать исходную коллекцию");
        System.out.println("3. Отсортировать коллекцию");
        System.out.println("4. Подсчитать количество вхождений студента");
        System.out.println("5. Выход");
    }

    private void createCollection() {
        System.out.println();
        System.out.println("Способ заполнения коллекции:");
        System.out.println("1. Ввести вручную");
        System.out.println("2. Сгенерировать случайно");
        System.out.println("3. Прочитать из файла");
        int sourceChoice = inputReader.readMenuChoice(3);

        int count = inputReader.readInt("Количество студентов (1 - " + MAX_STUDENTS + "): ", 1, MAX_STUDENTS);

        DataSource<Student> dataSource = resolveDataSource(sourceChoice);

        List<Student> loaded = dataSource.load(count);
        originalStudents = new ArrayList<>(loaded);
        System.out.println("Коллекция создана. Студентов: " + originalStudents.size());
    }

    private DataSource<Student> resolveDataSource(int sourceChoice) {
        switch (sourceChoice) {
            case 1:
                return new ManualStudentSource(inputReader);
            case 2:
                return new RandomStudentSource();
            case 3:
                String path = inputReader.readNonEmptyString("Путь к файлу: ");
                return new FileStudentSource(Path.of(path));
            default:
                throw new IllegalStateException("Неизвестный способ заполнения");
        }
    }

    private void showOriginalCollection() {
        printStudents(originalStudents, "Исходная коллекция");
    }

    private void sortAndShow() {
        if (originalStudents.isEmpty()) {
            System.out.println("Коллекция пуста. Сначала создайте коллекцию (пункт 1).");
            return;
        }

        SortField field = chooseSortField();
        SortStrategy<Student> strategy = chooseSortStrategy();
        boolean evenOnly = chooseSortMode(field);

        if (evenOnly) {
            strategy = new EvenOnlySortDecorator<>(strategy, Student::getRecordBookNumber);
        }

        List<Student> workingCopy = new ArrayList<>(originalStudents);
        strategy.sort(workingCopy, field.getComparator());

        printStudents(workingCopy, "Результат сортировки");
    }

    private SortField chooseSortField() {
        SortField[] fields = SortField.values();

        System.out.println();
        System.out.println("Поле сортировки:");
        for (int i = 0; i < fields.length; i++) {
            System.out.println((i + 1) + ". " + fields[i].getTitle());
        }

        int choice = inputReader.readMenuChoice(fields.length);
        return fields[choice - 1];
    }

    private SortStrategy<Student> chooseSortStrategy() {
        Map<String, SortStrategy<Student>> strategies = sortStrategyRegistry.getStrategies();
        List<String> names = new ArrayList<>(strategies.keySet());

        System.out.println();
        System.out.println("Алгоритм сортировки:");
        for (int i = 0; i < names.size(); i++) {
            System.out.println((i + 1) + ". " + names.get(i));
        }

        int choice = inputReader.readMenuChoice(names.size());
        return strategies.get(names.get(choice - 1));
    }

    private boolean chooseSortMode(SortField field) {
        System.out.println();
        System.out.println("Режим сортировки:");
        System.out.println("1. Обычная");
        System.out.println("2. Дополнительная (сортируются только студенты с чётным значением поля \""
                + field.getTitle() + "\", остальные остаются на месте)");

        int choice = inputReader.readMenuChoice(2);

        if (choice == 2 && field != SortField.RECORD_BOOK_NUMBER) {
            System.out.println("Дополнительный режим по заданию определён только для номера "
                    + "зачётной книжки. Будет выполнена обычная сортировка.");
            return false;
        }

        return choice == 2;
    }

    private void countOccurrences() {
        if (originalStudents.isEmpty()) {
            System.out.println("Коллекция пуста. Сначала создайте коллекцию (пункт 1).");
            return;
        }

        System.out.println("Введите данные студента, вхождения которого нужно посчитать:");
        String group = inputReader.readNonEmptyString("  Номер группы: ");
        double grade = inputReader.readDouble("  Средний балл (1.0 - 5.0): ", 1.0, 5.0);
        int recordBook = inputReader.readInt("  Номер зачётной книжки (> 0): ", 1, Integer.MAX_VALUE);

        Student target = new Student.Builder()
                .groupNumber(group)
                .averageGrade(grade)
                .recordBookNumber(recordBook)
                .build();

        int occurrences = occurrenceCounterService.count(originalStudents, target);
        System.out.println("Найдено вхождений: " + occurrences);
    }

    private void printStudents(List<Student> students, String title) {
        System.out.println();
        System.out.println(title + " (" + students.size() + "):");
        if (students.isEmpty()) {
            System.out.println("  (пусто)");
            return;
        }
        for (int i = 0; i < students.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + students.get(i));
        }
    }
}
