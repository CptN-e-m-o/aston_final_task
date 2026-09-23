package model;

import java.util.Objects;

public final class Student {
    private final String groupNumber;
    private final double averageGrade;
    private final int recordBookNumber;

    private Student(Builder builder) {
        this.groupNumber = builder.groupNumber;
        this.averageGrade = builder.averageGrade;
        this.recordBookNumber = builder.recordBookNumber;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public int getRecordBookNumber() {
        return recordBookNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Student student = (Student) obj;

        return Objects.equals(groupNumber, student.groupNumber)
                && Double.compare(averageGrade, student.averageGrade) == 0
                && recordBookNumber == student.recordBookNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupNumber, averageGrade, recordBookNumber);
    }

    @Override
    public String toString() {
        return "model.Student{" +
                "groupNumber='" + groupNumber + '\'' +
                ", averageGrade=" + averageGrade +
                ", recordBookNumber=" + recordBookNumber +
                '}';
    }



    public static class Builder {
        private String groupNumber;
        private double averageGrade;
        private int recordBookNumber;

        public Builder groupNumber(String groupNumber) {
            this.groupNumber = groupNumber;
            return this;
        }

        public Builder averageGrade(double averageGrade) {
            this.averageGrade = averageGrade;
            return this;
        }

        public Builder recordBookNumber(int recordBookNumber) {
            this.recordBookNumber = recordBookNumber;
            return this;
        }

        public Student build() {
            validate();
            return new Student(this);
        }

        private void validate() {
            if (this.groupNumber == null || this.groupNumber.isBlank()) {
                throw new IllegalArgumentException("Group Number не должен быть пустым или состоять лишь из пробелов");
            }
            if (this.recordBookNumber <= 0) {
                throw new IllegalArgumentException("Номер зачетной книжки не может быть 0 или меньше");
            }
            if (!Double.isFinite(this.averageGrade) || this.averageGrade < 1.0 || this.averageGrade > 5.0) {
                throw new IllegalArgumentException("Средний балл не может быть меньше, чем 1.0 или больше, чем 5.0");
            }
        }
    }
}
