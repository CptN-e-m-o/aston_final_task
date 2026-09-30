package test;

import collection.CustomArrayList;

public class CustomArrayListTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        testAddAndGet();
        testGrow();
        testSet();

        System.out.println();
        System.out.println("Пройдено: " + passed + ", провалено: " + failed);
    }

    private static void testAddAndGet() {
        CustomArrayList<String> list = new CustomArrayList<>();

        list.add("A");
        list.add("B");
        list.add("C");

        check(
                "add/get: размер списка после добавления трёх элементов равен 3",
                list.size() == 3
        );

        check(
                "add/get: элементы находятся на ожидаемых позициях",
                "A".equals(list.get(0))
                        && "B".equals(list.get(1))
                        && "C".equals(list.get(2))
        );
    }

    private static void testGrow() {
        CustomArrayList<Integer> list = new CustomArrayList<>();

        for (int i = 0; i < 15; i++) {
            list.add(i);
        }

        check(
                "grow: после добавления 15 элементов размер списка равен 15",
                list.size() == 15
        );

        check(
                "grow: элементы после расширения массива не потерялись",
                list.get(0) == 0
                        && list.get(9) == 9
                        && list.get(10) == 10
                        && list.get(14) == 14
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

    private static void testSet() {
        CustomArrayList<String> list = new CustomArrayList<>();

        list.add("A");
        list.add("B");
        list.add("C");

        String oldValue = list.set(1, "X");

        check(
                "set: возвращает старое значение",
                "B".equals(oldValue)
        );

        check(
                "set: заменяет элемент по индексу",
                "X".equals(list.get(1))
                        && list.size() == 3
        );
    }
}