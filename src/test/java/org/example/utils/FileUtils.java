package org.example.utils;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.writeString;

/**
 * Класс для работы с файлами
 */
public final class FileUtils {

    /**
     * Создает файл с координатами
     *
     * @param coordinates координаты для сохранения
     * @return созданный файл
     * @throws RuntimeException если не удалось создать файл
     */
    public static File createCoordinatesFile(String coordinates) {
        File file = new File("coordinates.txt");
        try {
            String content = "Координаты: " + coordinates;
            writeString(file.toPath(), content, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать файл с координатами: " + coordinates, e);
        }
        return file;
    }

    /**
     * Читает содержимое файла
     *
     * @param file файл для чтения
     * @return массив байтов с содержимым файла
     * @throws RuntimeException если не удалось прочитать файл
     */
    public static byte[] readFileBytes(File file) {
        try {
            return readAllBytes(file.toPath());
        } catch (Exception e) {
            throw new RuntimeException("Не удалось прочитать файл: " + file.getName(), e);
        }
    }

    /**
     * Получает путь к файлу из директории resources
     *
     * @param fileName имя файла в resources
     * @return абсолютный путь к файлу
     * @throws RuntimeException если файл не найден
     */
    public static String getFilePathFromResources(String fileName) {
        try {
            URL resource = FileUtils.class.getClassLoader().getResource(fileName);
            if (resource == null) {
                throw new RuntimeException("Файл не найден в resources: " + fileName);
            }
            Path path = Paths.get(resource.toURI());
            return path.toString();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось получить путь к файлу: " + fileName, e);
        }
    }
}