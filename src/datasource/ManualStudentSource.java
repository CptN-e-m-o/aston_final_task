package datasource;

import app.InputReader;
import model.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * ВРЕМЕННАЯ реализация ручного ввода студентов.
 *
 * Это зона ответственности Человека 3 — здесь сделан рабочий минимум
 * (поля запрашиваются по одному, финальная валидация — через Student.Builder,
 * при ошибке ввод конкретного студента повторяется), чтобы можно было
 * протестировать основной цикл приложения целиком.
 *
 * TODO (Человек 3): заменить на финальную реализацию с более подробной
 * посимвольной валидацией ввода, если это требуется по заданию.
 */
public class ManualStudentSource implements DataSource<Student> {

    private final InputReader inputReader;

    public ManualStudentSource(InputReader inputReader) {
        if (inputReader == null) {
            throw new IllegalArgumentException("InputReader cannot be null");
        }
        this.inputReader = inputReader;
    }

    @Override
    public List<Student> load(int count) {
        List<Student> students = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            System.out.println("Студент " + i + " из " + count + ":");
            students.add(readOneStudent());
        }
        return students;
    }

    private Student readOneStudent() {
        while (true) {
            try {
                String group = inputReader.readNonEmptyString("  Номер группы: ");
                double grade = inputReader.readDouble("  Средний балл (1.0 - 5.0): ", 1.0, 5.0);
                int recordBook = inputReader.readInt("  Номер зачётной книжки (> 0): ", 1, Integer.MAX_VALUE);

                // Повторная валидация значений происходит внутри Builder'а —
                // это гарантирует, что обойти её через этот источник данных нельзя.
                return new Student.Builder()
                        .groupNumber(group)
                        .averageGrade(grade)
                        .recordBookNumber(recordBook)
                        .build();
            } catch (IllegalArgumentException e) {
                System.out.println("  Ошибка: " + e.getMessage() + ". Повторите ввод этого студента.");
            }
        }
    }
}
