package source;

import collection.CustomArrayList;
import model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileStudentSource implements DataSource<Student> {
    private final String filePath;

    public FileStudentSource(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public CustomArrayList<Student> load(int count) {
        CustomArrayList<Student> students = new CustomArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            lines.stream()
                    .filter(line -> !line.trim().isEmpty())
                    .map(this::parseLine)
                    .filter(student -> student != null)
                    .limit(count)
                    .forEach(students::add);

        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
        return students;
    }

    private Student parseLine(String line) {
        try {
            String[] parts = line.split(";");
            if (parts.length != 3) {
                System.err.println("Неверный формат строки (ожидается 3 поля): " + line);
                return null;
            }

            String group = parts[0].trim();
            if (group.isEmpty()) {
                System.err.println("Пустая группа в строке: " + line);
                return null;
            }

            double grade = Double.parseDouble(parts[1].trim().replace(',', '.'));
            int recordBook = Integer.parseInt(parts[2].trim());

            return new Student.Builder()
                    .groupNumber(group)
                    .averageGrade(grade)
                    .recordBookNumber(recordBook)
                    .build();

        } catch (NumberFormatException e) {
            System.err.println("Ошибка преобразования числа в строке: " + line);
            return null;
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка валидации данных: " + e.getMessage() + " в строке: " + line);
            return null;
        }
    }
}