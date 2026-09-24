package strategy;

import java.util.Comparator;
import java.util.List;

/**
 * Реализация алгоритма Quick Sort.
 *
 * Это основная стратегия сортировки в нашем проекте.
 *
 * Класс не зависит от Student и вообще не знает,
 * какой именно объект он сортирует.
 *
 * За правило сравнения отвечает Comparator.
 */
public class QuickSortStrategy<T> implements SortStrategy<T> {

    /**
     * Запускает сортировку списка.
     *
     * @param list список элементов для сортировки
     * @param comparator правило сравнения элементов
     */
    @Override
    public void sort(List<T> list, Comparator<T> comparator) {

        if (list == null) {
            throw new IllegalArgumentException("List cannot be null");
        }

        if (comparator == null) {
            throw new IllegalArgumentException("Comparator cannot be null");
        }

        // Если элементов меньше двух,
        // сортировать здесь уже нечего.
        if (list.size() < 2) {
            return;
        }

        quickSort(list, comparator, 0, list.size() - 1);
    }

    /**
     * Рекурсивная часть Quick Sort.
     *
     * Делим список на части относительно опорного элемента,
     * после чего отдельно сортируем левую и правую части.
     */
    private void quickSort(
            List<T> list,
            Comparator<T> comparator,
            int left,
            int right
    ) {

        if (left >= right) {
            return;
        }

        // После partition pivot окажется на своём месте.
        int pivotIndex = partition(
                list,
                comparator,
                left,
                right
        );

        // Сортируем всё, что слева от pivot.
        quickSort(
                list,
                comparator,
                left,
                pivotIndex - 1
        );

        // Сортируем всё, что справа от pivot.
        quickSort(
                list,
                comparator,
                pivotIndex + 1,
                right
        );
    }

    /**
     * Делит часть списка относительно опорного элемента.
     *
     * В качестве pivot используем последний элемент.
     *
     * Все элементы меньше или равные pivot
     * отправляются в левую часть.
     *
     * Остальные остаются справа.
     *
     * @return позиция pivot после разделения
     */
    private int partition(
            List<T> list,
            Comparator<T> comparator,
            int left,
            int right
    ) {

        T pivot = list.get(right);

        int smallerIndex = left - 1;

        for (int currentIndex = left;
             currentIndex < right;
             currentIndex++) {

            if (comparator.compare(
                    list.get(currentIndex),
                    pivot
            ) <= 0) {

                smallerIndex++;

                swap(
                        list,
                        smallerIndex,
                        currentIndex
                );
            }
        }

        // Ставим pivot между двумя частями.
        swap(
                list,
                smallerIndex + 1,
                right
        );

        return smallerIndex + 1;
    }

    /**
     * Меняет два элемента списка местами.
     *
     * Никакой готовой сортировки здесь нет —
     * это просто обычная перестановка двух элементов.
     */
    private void swap(
            List<T> list,
            int firstIndex,
            int secondIndex
    ) {

        T temporary = list.get(firstIndex);

        list.set(
                firstIndex,
                list.get(secondIndex)
        );

        list.set(
                secondIndex,
                temporary
        );
    }
}