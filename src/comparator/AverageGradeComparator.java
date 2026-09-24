package comparator;

import model.Student;

import java.util.Comparator;

public class AverageGradeComparator implements Comparator<Student> {

    @Override
    public int compare(Student o1, Student o2) {
        return Double.compare(o1.getAverageGrade(), o2.getAverageGrade());
    }
}