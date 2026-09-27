package strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.ToIntFunction;

/**
 * Декоратор для дополнительного задания №1 (Человек 2).
 *
 * Оборачивает уже существующую стратегию сортировки: элементы с чётным
 * значением заданного числового поля сортируются как обычно, а элементы
 * с нечётным значением остаются на своих исходных позициях в списке.
 * Дублирования алгоритмов сортировки здесь нет — используется delegate.
 *
 * ВРЕМЕННО размещено здесь, чтобы можно было протестировать пункт меню
 * "обычная/дополнительная сортировка" целиком.
 * TODO (Человек 2): свериться с финальной версией доп. задания №1
 * и при необходимости заменить эту реализацию.
 */
public class EvenOnlySortDecorator<T> implements SortStrategy<T> {

    private final SortStrategy<T> delegate;
    private final ToIntFunction<T> numericField;

    public EvenOnlySortDecorator(SortStrategy<T> delegate, ToIntFunction<T> numericField) {
        if (delegate == null || numericField == null) {
            throw new IllegalArgumentException("Delegate and numericField cannot be null");
        }
        this.delegate = delegate;
        this.numericField = numericField;
    }

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        if (list == null || comparator == null) {
            throw new IllegalArgumentException("List and comparator cannot be null");
        }

        List<T> evenElements = new ArrayList<>();
        List<Integer> evenPositions = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            T element = list.get(i);
            if (numericField.applyAsInt(element) % 2 == 0) {
                evenElements.add(element);
                evenPositions.add(i);
            }
        }

        delegate.sort(evenElements, comparator);

        for (int i = 0; i < evenPositions.size(); i++) {
            list.set(evenPositions.get(i), evenElements.get(i));
        }
    }
}
