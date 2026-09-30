package source;

import collection.CustomArrayList;
import model.Student;

import java.util.Scanner;
import java.util.stream.IntStream;

public class ManualStudentSource implements DataSource<Student> {
    private final Scanner scanner;

    public ManualStudentSource(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public CustomArrayList<Student> load(int count) {
        CustomArrayList<Student> students = new CustomArrayList<>();

        System.out.println("--- Ручной ввод (" + count + " студентов) ---");

        IntStream.rangeClosed(1, count)
                .forEach(i -> {
                    System.out.println("Студент #" + i);
                    students.add(readOneStudent());
                });

        return students;
    }

    private int readIntSafely(Scanner sc) {
        while (true) {
            try {
                String input = sc.nextLine().trim();
                if (input.isEmpty()) throw new IllegalArgumentException("Пустая строка");
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Неверный формат! Введите целое число: ");
            }
        }
    }

    private double readDoubleSafely(Scanner sc) {
        while (true) {
            try {
                String input = sc.nextLine().trim().replace(',', '.');
                if (input.isEmpty()) throw new IllegalArgumentException("Пустая строка");
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Неверный формат! Введите число (например, 4.5): ");
            }
        }
    }

    private Student readOneStudent() {
        while (true) {
            try {
                System.out.print("Введите номер группы: ");
                String group = scanner.nextLine();

                if (group.trim().isEmpty()) {
                    throw new IllegalArgumentException(
                            "Группа не может быть пустой!"
                    );
                }

                System.out.print("Введите средний балл (1.0 - 5.0): ");
                double grade = readDoubleSafely(scanner);

                System.out.print("Введите номер зачетной книжки (> 0): ");
                int recordBook = readIntSafely(scanner);

                return new Student.Builder()
                        .groupNumber(group)
                        .averageGrade(grade)
                        .recordBookNumber(recordBook)
                        .build();

            } catch (IllegalArgumentException e) {
                System.out.println(
                        "Ошибка: " + e.getMessage() + ". Попробуйте снова."
                );
            }
        }
    }
}