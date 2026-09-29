package datasource;

import java.util.List;

/**
 * Общий интерфейс источника данных для заполнения коллекции.
 *
 * Реализации: ручной ввод, случайная генерация, чтение из файла
 * (см. задание Человека 3).
 */
public interface DataSource<T> {

    /**
     * Возвращает коллекцию из count элементов.
     */
    List<T> load(int count);
}
