package datasource;

import model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * ВРЕМЕННАЯ реализация чтения студентов из файла.
 *
 * Ожидаемый формат строки: groupNumber;averageGrade;recordBookNumber
 * Например: 101;4.5;12345
 *
 * Это зона ответственности Человека 3 (проверка формата строки, количества
 * полей, преобразования чисел и т.д.). Здесь сделана рабочая версия:
 * некорректные строки пропускаются с сообщением в консоль, а не роняют
 * загрузку целиком.
 *
 * TODO (Человек 3): заменить на финальную реализацию.
 */
public class FileStudentSource implements DataSource<Student> {

    private static final String DELIMITER = ";";

    private final Path filePath;

    public FileStudentSource(Path filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        this.filePath = filePath;
    }

    @Override
    public List<Student> load(int count) {
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать файл: " + filePath, e);
        }

        List<Student> students = new ArrayList<>();
        int lineNumber = 0;

        for (String line : lines) {
            lineNumber++;
            if (students.size() >= count) {
                break;
            }
            if (line == null || line.isBlank()) {
                continue;
            }
            try {
                students.add(parseLine(line));
            } catch (RuntimeException e) {
                System.out.println("Строка " + lineNumber + " пропущена: " + e.getMessage());
            }
        }

        if (students.size() < count) {
            System.out.println("В файле найдено меньше подходящих строк (" + students.size()
                    + "), чем запрошено (" + count + ").");
        }

        return students;
    }

    private Student parseLine(String line) {
        String[] parts = line.split(DELIMITER);
        if (parts.length != 3) {
            throw new IllegalArgumentException("ожидалось 3 поля через '" + DELIMITER
                    + "', получено " + parts.length);
        }

        String group = parts[0].trim();
        double grade;
        int recordBook;
        try {
            grade = Double.parseDouble(parts[1].trim());
            recordBook = Integer.parseInt(parts[2].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("не удалось преобразовать числовые поля в числа");
        }

        // Финальная валидация значений всё равно происходит в Builder'е.
        return new Student.Builder()
                .groupNumber(group)
                .averageGrade(grade)
                .recordBookNumber(recordBook)
                .build();
    }
}
