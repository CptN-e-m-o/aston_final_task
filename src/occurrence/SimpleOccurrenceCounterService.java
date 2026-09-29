package occurrence;

import java.util.List;
import java.util.Objects;

/**
 * ВРЕМЕННАЯ однопоточная реализация подсчёта вхождений.
 *
 * Это зона ответственности Человека 5 (доп. задание №4: многопоточный
 * подсчёт через Thread/start()/join()). Эта версия используется только
 * для того, чтобы протестировать пункт меню целиком, пока финальная
 * реализация не готова.
 *
 * TODO (Человек 5): заменить на OccurrenceCounter<T>, реализующий этот
 * же интерфейс OccurrenceCounterService<T>.
 */
public class SimpleOccurrenceCounterService<T> implements OccurrenceCounterService<T> {

    @Override
    public int count(List<T> list, T target) {
        if (list == null) {
            throw new IllegalArgumentException("List cannot be null");
        }

        int result = 0;
        for (T element : list) {
            if (Objects.equals(element, target)) {
                result++;
            }
        }
        return result;
    }
}
