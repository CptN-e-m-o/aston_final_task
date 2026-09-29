package strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

/**
 * Дополнительное задание №1.
 *
 * Сортирует только объекты с чётным значением
 * выбранного числового поля.
 *
 * Объекты с нечётным значением поля
 * остаются на своих исходных позициях.
 */
public class EvenOnlySortStrategy<T> implements SortStrategy<T> {

    private final SortStrategy<T> sortStrategy;
    private final ToIntFunction<T> numericField;

    public EvenOnlySortStrategy(
            SortStrategy<T> sortStrategy,
            ToIntFunction<T> numericField
    ) {

        if (sortStrategy == null) {
            throw new IllegalArgumentException(
                    "Sort strategy cannot be null"
            );
        }

        if (numericField == null) {
            throw new IllegalArgumentException(
                    "Numeric field cannot be null"
            );
        }

        this.sortStrategy = sortStrategy;
        this.numericField = numericField;
    }

    @Override
    public void sort(
            List<T> list,
            Comparator<T> comparator
    ) {

        if (list == null) {
            throw new IllegalArgumentException(
                    "List cannot be null"
            );
        }

        if (comparator == null) {
            throw new IllegalArgumentException(
                    "Comparator cannot be null"
            );
        }

        // Собираем только элементы с чётным
        // значением выбранного числового поля.
        List<T> evenElements = new ArrayList<>();

        for (T element : list) {

            if (numericField.applyAsInt(element) % 2 == 0) {
                evenElements.add(element);
            }
        }

        // Используем основной алгоритм сортировки проекта.
        sortStrategy.sort(
                evenElements,
                comparator
        );

        // Возвращаем отсортированные элементы
        // только на позиции чётных элементов.
        int sortedIndex = 0;

        for (int i = 0; i < list.size(); i++) {

            if (numericField.applyAsInt(list.get(i)) % 2 == 0) {

                list.set(
                        i,
                        evenElements.get(sortedIndex)
                );

                sortedIndex++;
            }
        }
    }
}