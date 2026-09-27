package strategy;

import model.Student;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Реестр стратегий сортировки, доступных для выбора в меню приложения.
 *
 * Сейчас зарегистрирована только QuickSortStrategy — остальные алгоритмы
 * (доп. Bubble/Selection) относятся к заданию Человека 2.
 *
 * TODO (Человек 2): при добавлении BubbleSortStrategy и SelectionSortStrategy
 * зарегистрировать их здесь же — больше никакой код меню трогать не нужно.
 */
public class SortStrategyRegistry {

    private final Map<String, SortStrategy<Student>> strategies = new LinkedHashMap<>();

    public SortStrategyRegistry() {
        strategies.put("Быстрая сортировка (Quick Sort)", new QuickSortStrategy<>());
        // strategies.put("Сортировка пузырьком (Bubble Sort)", new BubbleSortStrategy<>());
        // strategies.put("Сортировка выбором (Selection Sort)", new SelectionSortStrategy<>());
    }

    public Map<String, SortStrategy<Student>> getStrategies() {
        return strategies;
    }
}
