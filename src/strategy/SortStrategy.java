package strategy;

import java.util.Comparator;
import java.util.List;

/**
 * Общий интерфейс для стратегий сортировки.
 *
 * Нужен для паттерна Strategy.
 *
 * Благодаря ему программа может работать с любой
 * реализацией сортировки, не зная, какой алгоритм
 * находится внутри.
 */
public interface SortStrategy<T> {

    /**
     * Сортирует переданный список.
     *
     * @param list список элементов
     * @param comparator правило сравнения элементов
     */
    void sort(List<T> list, Comparator<T> comparator);
}