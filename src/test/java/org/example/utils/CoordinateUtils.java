package org.example.utils;

/**
 * Класс для работы с координатами.
 */
public final class CoordinateUtils {

    /**
     * Преобразует координаты в числовой формат для поиска (с запятой)
     *
     * @param coordinates координаты в формате "XX.XXXXШ, YY.YYYYД"
     * @return координаты в формате "XX.XXXX, YY.YYYY"
     */
    public static String toNumericFormat(String coordinates) {
        if (coordinates == null || coordinates.isEmpty()) {
            return coordinates;
        }

        return coordinates
                .replace("Ш", "")
                .replace("Д", "")
                .replace(",", "")
                .trim()
                .replace(" ", ", ");
    }
}