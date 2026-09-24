package comparator;

import model.Student;

import java.util.Comparator;

public class RecordBookNumberComparator implements Comparator<Student> {

    @Override
    public int compare(Student o1, Student o2) {
        return Integer.compare(o1.getRecordBookNumber(), o2.getRecordBookNumber());
    }
}