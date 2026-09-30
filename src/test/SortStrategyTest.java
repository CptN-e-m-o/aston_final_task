package test;

import comparator.StudentGroupNumberComparator;
import model.Student;
import strategy.QuickSortStrategy;
import strategy.SortStrategy;
import comparator.AverageGradeComparator;
import comparator.RecordBookNumberComparator;
import strategy.EvenOnlySortStrategy;

import java.util.ArrayList;
import java.util.List;

public class SortStrategyTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testQuickSortByGroupNumber();
        testQuickSortByAverageGrade();
        testQuickSortByRecordBookNumber();
        testEvenOnlySort();

        System.out.println();
        System.out.println("Пройдено: " + passed + ", провалено: " + failed);
    }

    private static void testQuickSortByGroupNumber() {
        Student first = new Student.Builder()
                .groupNumber("301")
                .averageGrade(4.0)
                .recordBookNumber(300)
                .build();

        Student second = new Student.Builder()
                .groupNumber("101")
                .averageGrade(5.0)
                .recordBookNumber(100)
                .build();

        Student third = new Student.Builder()
                .groupNumber("201")
                .averageGrade(3.0)
                .recordBookNumber(200)
                .build();

        List<Student> students = new ArrayList<>();
        students.add(first);
        students.add(second);
        students.add(third);

        SortStrategy<Student> strategy = new QuickSortStrategy<>();
        strategy.sort(students, new StudentGroupNumberComparator());

        check(
                "QuickSort: сортировка по номеру группы",
                "101".equals(students.get(0).getGroupNumber())
                        && "201".equals(students.get(1).getGroupNumber())
                        && "301".equals(students.get(2).getGroupNumber())
        );
    }

    private static void check(String testName, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("[OK]   " + testName);
        } else {
            failed++;
            System.out.println("[FAIL] " + testName);
        }
    }

    private static void testQuickSortByAverageGrade() {
        Student first = new Student.Builder()
                .groupNumber("101")
                .averageGrade(4.7)
                .recordBookNumber(100)
                .build();

        Student second = new Student.Builder()
                .groupNumber("102")
                .averageGrade(3.2)
                .recordBookNumber(200)
                .build();

        Student third = new Student.Builder()
                .groupNumber("103")
                .averageGrade(4.1)
                .recordBookNumber(300)
                .build();

        List<Student> students = new ArrayList<>();
        students.add(first);
        students.add(second);
        students.add(third);

        SortStrategy<Student> strategy = new QuickSortStrategy<>();
        strategy.sort(students, new AverageGradeComparator());

        check(
                "QuickSort: сортировка по среднему баллу",
                Double.compare(students.get(0).getAverageGrade(), 3.2) == 0
                        && Double.compare(students.get(1).getAverageGrade(), 4.1) == 0
                        && Double.compare(students.get(2).getAverageGrade(), 4.7) == 0
        );
    }

    private static void testQuickSortByRecordBookNumber() {
        Student first = new Student.Builder()
                .groupNumber("101")
                .averageGrade(4.0)
                .recordBookNumber(300)
                .build();

        Student second = new Student.Builder()
                .groupNumber("102")
                .averageGrade(4.0)
                .recordBookNumber(100)
                .build();

        Student third = new Student.Builder()
                .groupNumber("103")
                .averageGrade(4.0)
                .recordBookNumber(200)
                .build();

        List<Student> students = new ArrayList<>();
        students.add(first);
        students.add(second);
        students.add(third);

        SortStrategy<Student> strategy = new QuickSortStrategy<>();
        strategy.sort(students, new RecordBookNumberComparator());

        check(
                "QuickSort: сортировка по номеру зачётной книжки",
                students.get(0).getRecordBookNumber() == 100
                        && students.get(1).getRecordBookNumber() == 200
                        && students.get(2).getRecordBookNumber() == 300
        );
    }

    private static void testEvenOnlySort() {
        List<Student> students = new ArrayList<>();

        students.add(new Student.Builder()
                .groupNumber("101")
                .averageGrade(4.0)
                .recordBookNumber(8)
                .build());

        students.add(new Student.Builder()
                .groupNumber("102")
                .averageGrade(4.0)
                .recordBookNumber(5)
                .build());

        students.add(new Student.Builder()
                .groupNumber("103")
                .averageGrade(4.0)
                .recordBookNumber(4)
                .build());

        students.add(new Student.Builder()
                .groupNumber("104")
                .averageGrade(4.0)
                .recordBookNumber(7)
                .build());

        students.add(new Student.Builder()
                .groupNumber("105")
                .averageGrade(4.0)
                .recordBookNumber(2)
                .build());

        students.add(new Student.Builder()
                .groupNumber("106")
                .averageGrade(4.0)
                .recordBookNumber(3)
                .build());

        students.add(new Student.Builder()
                .groupNumber("107")
                .averageGrade(4.0)
                .recordBookNumber(6)
                .build());

        SortStrategy<Student> strategy = new EvenOnlySortStrategy<>(
                new QuickSortStrategy<>(),
                Student::getRecordBookNumber
        );

        strategy.sort(
                students,
                new RecordBookNumberComparator()
        );

        check(
                "EvenOnlySort: чётные значения сортируются, нечётные остаются на исходных позициях",
                students.get(0).getRecordBookNumber() == 2
                        && students.get(1).getRecordBookNumber() == 5
                        && students.get(2).getRecordBookNumber() == 4
                        && students.get(3).getRecordBookNumber() == 7
                        && students.get(4).getRecordBookNumber() == 6
                        && students.get(5).getRecordBookNumber() == 3
                        && students.get(6).getRecordBookNumber() == 8
        );
    }
}