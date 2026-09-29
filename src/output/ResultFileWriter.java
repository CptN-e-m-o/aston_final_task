package output;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Записывает результаты (отсортированную коллекцию, результат подсчёта
 * вхождений и т.п.) в текстовый файл.
 *
 * Доп. задание №2: запись всегда происходит в режиме добавления (append) —
 * старое содержимое файла не стирается, файл создаётся автоматически,
 * если ещё не существует. Каждая запись сопровождается заголовком
 * с описанием результата и меткой времени, чтобы в файле можно было
 * отличить разные запуски друг от друга.
 */
public class ResultFileWriter {

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Добавляет в конец файла блок из заголовка title (с текущей меткой
     * времени) и построчного содержимого lines.
     *
     * @param path  путь к файлу; если файла не существует, он будет создан
     * @param title описание записываемого результата
     * @param lines строки результата (например, отсортированные студенты)
     */
    public void append(Path path, String title, List<String> lines) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        if (lines == null) {
            throw new IllegalArgumentException("Lines cannot be null");
        }

        try (Writer writer = Files.newBufferedWriter(
                path,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            writer.write("===== " + title + " (" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + ") =====");
            writer.write(System.lineSeparator());

            if (lines.isEmpty()) {
                writer.write("(пусто)");
                writer.write(System.lineSeparator());
            } else {
                for (String line : lines) {
                    writer.write(line);
                    writer.write(System.lineSeparator());
                }
            }

            writer.write(System.lineSeparator());
        }
    }
}
