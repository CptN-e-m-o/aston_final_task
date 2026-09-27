package occurrence;

import java.util.List;

/**
 * Интерфейс сервиса подсчёта количества вхождений элемента в коллекцию.
 *
 * Финальную многопоточную реализацию (доп. задание №4: OccurrenceCounter,
 * Thread/start()/join(), разбиение коллекции на диапазоны) делает Человек 5.
 * Приложение зависит только от этого интерфейса, поэтому подключение
 * финальной реализации не потребует менять код меню.
 */
public interface OccurrenceCounterService<T> {

    /**
     * Считает, сколько раз target встречается в list (через equals()).
     */
    int count(List<T> list, T target);
}
