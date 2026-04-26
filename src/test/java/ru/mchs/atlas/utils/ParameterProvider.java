package ru.mchs.atlas.utils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * <p>Провайдер параметров для чтения конфигурационных файлов.</p>
 */
public final class ParameterProvider {
    private static final String CONFIG_PATH = "configuration/config.properties";
    private static ParameterProvider instance;
    private final Map<String, String> parameters;

    /**
     * <p>Приватный конструктор для реализации паттерна Singleton.
     * Загружает параметры из конфигурационного файла.</p>
     *
     * @throws RuntimeException если произошла ошибка при загрузке конфигурационного файла
     */
    private ParameterProvider() {
        try {
            this.parameters = new HashMap<>();
            Properties prop = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_PATH);
            prop.load(inputStream);
            prop.stringPropertyNames()
                    .forEach(key -> this.parameters.put(key, prop.getProperty(key)));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке конфигурации", e);
        }
    }

    /**
     * <p>Возвращает значение параметра по указанному ключу.
     * При первом вызове метода инициализирует экземпляр класса и загружает параметры.</p>
     *
     * @param key ключ параметра для поиска
     * @return значение параметра или null, если параметр с указанным ключом не найден
     */
    public static String get(String key) {
        if (instance == null) {
            instance = new ParameterProvider();
        }
        return instance.parameters.get(key);
    }
}