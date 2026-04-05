# Используем образ Maven с JDK 17 (Eclipse Temurin)
FROM maven:3.9.3-eclipse-temurin-17

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем только pom.xml для первоначального скачивания зависимостей
COPY pom.xml .

# Разрешаем зависимости, чтобы кэшировать их на этапе сборки
RUN mvn dependency:resolve

# Копируем исходный код проекта в контейнер
COPY src ./src

# Компилируем тесты (создает target/test-classes/ и копирует ресурсы)
RUN mvn test-compile

# Создаем директорию для результатов Allure
RUN mkdir -p target/allure-results

# Команда по умолчанию для запуска тестов при старте контейнера
CMD ["mvn", "test"]