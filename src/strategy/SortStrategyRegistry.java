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
 */
public class SortStrategyRegistry {

    private final Map<String, SortStrategy<Student>> strategies = new LinkedHashMap<>();

    public SortStrategyRegistry() {
        strategies.put("Быстрая сортировка (Quick Sort)", new QuickSortStrategy<>());
    }

    public Map<String, SortStrategy<Student>> getStrategies() {
        return strategies;
    }
}
