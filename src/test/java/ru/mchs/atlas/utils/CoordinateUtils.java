package ru.mchs.atlas.utils;

/**
 * <p>Класс для работы с координатами.</p>
 */
public final class CoordinateUtils {

    /**
     * <p>Преобразует координаты в числовой формат для поиска (с запятой)</p>
     *
     * @param coordinates координаты в формате "XX.XXXXШ, YY.YYYYД"
     * @return координаты в формате "XX.XXXX, YY.YYYY"
     */
    public static String toNumericFormat(String coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            return coordinates;
        }

        return coordinates
                .replaceAll("[ШД,]", "")
                .trim()
                .replace(" ", ", ");
    }
}