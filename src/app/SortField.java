package app;

import comparator.AverageGradeComparator;
import comparator.RecordBookNumberComparator;
import comparator.StudentGroupNumberComparator;
import model.Student;

import java.util.Comparator;

/**
 * Поля Student, по которым доступна сортировка в меню приложения,
 * вместе с соответствующим им Comparator'ом (см. пакет comparator).
 */
public enum SortField {

    GROUP_NUMBER("Номер группы", new StudentGroupNumberComparator()),
    AVERAGE_GRADE("Средний балл", new AverageGradeComparator()),
    RECORD_BOOK_NUMBER("Номер зачётной книжки", new RecordBookNumberComparator());

    private final String title;
    private final Comparator<Student> comparator;

    SortField(String title, Comparator<Student> comparator) {
        this.title = title;
        this.comparator = comparator;
    }

    public String getTitle() {
        return title;
    }

    public Comparator<Student> getComparator() {
        return comparator;
    }
}
