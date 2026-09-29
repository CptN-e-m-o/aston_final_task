package datasource;

import model.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ВРЕМЕННАЯ реализация случайной генерации студентов.
 *
 * Это зона ответственности Человека 3 (в т.ч. заполнение через Stream API —
 * доп. задание №3). Здесь сделана простая рабочая версия для тестирования
 * основного цикла приложения.
 *
 * TODO (Человек 3): заменить/дополнить финальной версией.
 */
public class RandomStudentSource implements DataSource<Student> {

    private static final int GROUP_MIN = 100;
    private static final int GROUP_MAX = 999;
    private static final int RECORD_BOOK_MIN = 1;
    private static final int RECORD_BOOK_MAX = 100_000;

    private final Random random;

    public RandomStudentSource() {
        this(new Random());
    }

    public RandomStudentSource(Random random) {
        if (random == null) {
            throw new IllegalArgumentException("Random cannot be null");
        }
        this.random = random;
    }

    @Override
    public List<Student> load(int count) {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            students.add(generateOne());
        }
        return students;
    }

    private Student generateOne() {
        String group = String.valueOf(GROUP_MIN + random.nextInt(GROUP_MAX - GROUP_MIN + 1));
        double grade = 1.0 + (random.nextInt(401) / 100.0); // от 1.00 до 5.00
        int recordBook = RECORD_BOOK_MIN + random.nextInt(RECORD_BOOK_MAX - RECORD_BOOK_MIN + 1);

        return new Student.Builder()
                .groupNumber(group)
                .averageGrade(Math.min(grade, 5.0))
                .recordBookNumber(recordBook)
                .build();
    }
}
