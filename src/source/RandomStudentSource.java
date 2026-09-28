package source;

import collection.CustomArrayList;
import model.Student;

import java.util.Random;

public class RandomStudentSource implements DataSource<Student> {
    private final Random random = new Random();
    private static final String[] GROUPS = {"101", "102", "201", "202", "301"};

    @Override
    public CustomArrayList<Student> getData(int count) {
        CustomArrayList<Student> students = new CustomArrayList<>();
        for (int i = 0; i < count; i++) {
            String group = GROUPS[random.nextInt(GROUPS.length)];
            double grade = 2.0 + (5.0 - 2.0) * random.nextDouble();
            grade = Math.round(grade * 10.0) / 10.0;
            int recordBook = 10000 + random.nextInt(90000);

            Student student = new Student.Builder()
                    .groupNumber(group)
                    .averageGrade(grade)
                    .recordBookNumber(recordBook)
                    .build();
            students.add(student);
        }
        return students;
    }
}
