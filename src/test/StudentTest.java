package test;

import model.Student;

public class StudentTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testValidStudentCreation();
        testBlankGroupValidation();
        testRecordBookNumberValidation();
        testAverageGradeValidation();
        testNonFiniteAverageGradeValidation();
        testEqualsAndHashCode();

        System.out.println();
        System.out.println("Пройдено: " + passed + ", провалено: " + failed);
    }

    private static void testValidStudentCreation() {
        Student student = new Student.Builder()
                .groupNumber("101")
                .averageGrade(4.5)
                .recordBookNumber(12345)
                .build();

        check(
                "Student: корректный объект создаётся",
                "101".equals(student.getGroupNumber())
                        && Double.compare(student.getAverageGrade(), 4.5) == 0
                        && student.getRecordBookNumber() == 12345
        );
    }

    private static void testBlankGroupValidation() {
        boolean exceptionThrown = false;

        try {
            new Student.Builder()
                    .groupNumber("   ")
                    .averageGrade(4.0)
                    .recordBookNumber(12345)
                    .build();
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        check(
                "Builder: пустой номер группы отклоняется",
                exceptionThrown
        );
    }

    private static void testRecordBookNumberValidation() {
        boolean exceptionThrown = false;

        try {
            new Student.Builder()
                    .groupNumber("101")
                    .averageGrade(4.0)
                    .recordBookNumber(0)
                    .build();
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        check(
                "Builder: номер зачётной книжки <= 0 отклоняется",
                exceptionThrown
        );
    }

    private static void testAverageGradeValidation() {
        boolean belowMinimumRejected = false;
        boolean aboveMaximumRejected = false;

        try {
            new Student.Builder()
                    .groupNumber("101")
                    .averageGrade(0.9)
                    .recordBookNumber(12345)
                    .build();
        } catch (IllegalArgumentException e) {
            belowMinimumRejected = true;
        }

        try {
            new Student.Builder()
                    .groupNumber("101")
                    .averageGrade(5.1)
                    .recordBookNumber(12345)
                    .build();
        } catch (IllegalArgumentException e) {
            aboveMaximumRejected = true;
        }

        check(
                "Builder: средний балл должен находиться в диапазоне 1.0 - 5.0",
                belowMinimumRejected && aboveMaximumRejected
        );
    }

    private static void testNonFiniteAverageGradeValidation() {
        boolean nanRejected = false;

        try {
            new Student.Builder()
                    .groupNumber("101")
                    .averageGrade(Double.NaN)
                    .recordBookNumber(12345)
                    .build();
        } catch (IllegalArgumentException e) {
            nanRejected = true;
        }

        check(
                "Builder: NaN не принимается как средний балл",
                nanRejected
        );
    }

    private static void testEqualsAndHashCode() {
        Student first = new Student.Builder()
                .groupNumber("101")
                .averageGrade(4.5)
                .recordBookNumber(12345)
                .build();

        Student second = new Student.Builder()
                .groupNumber("101")
                .averageGrade(4.5)
                .recordBookNumber(12345)
                .build();

        Student different = new Student.Builder()
                .groupNumber("102")
                .averageGrade(4.5)
                .recordBookNumber(12345)
                .build();

        check(
                "equals: студенты с одинаковыми полями равны",
                first.equals(second)
        );

        check(
                "hashCode: равные студенты имеют одинаковый hashCode",
                first.hashCode() == second.hashCode()
        );

        check(
                "equals: студенты с разными полями не равны",
                !first.equals(different)
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
}