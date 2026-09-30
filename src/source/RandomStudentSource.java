package source;

import collection.CustomArrayList;
import model.Student;

import java.util.Random;
import java.util.stream.Stream;

public class RandomStudentSource implements DataSource<Student> {
    private final Random random = new Random();
    private static final String[] GROUPS = {"101", "102", "201", "202", "301"};

    @Override
    public CustomArrayList<Student> load(int count) {
        CustomArrayList<Student> students = new CustomArrayList<>();

        Stream.generate(this::generateOne)
                .limit(count)
                .forEach(students::add);

        return students;
    }

    private Student generateOne() {

        String group = GROUPS[random.nextInt(GROUPS.length)];
        double grade = 2.0 + (5.0 - 2.0) * random.nextDouble();
        grade = Math.round(grade * 10.0) / 10.0;
        int recordBook = 10000 + random.nextInt(90000);

        return new Student.Builder()
                .groupNumber(group)
                .averageGrade(grade)
                .recordBookNumber(recordBook)
                .build();
    }
}
