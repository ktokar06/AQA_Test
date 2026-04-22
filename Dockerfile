# ЭТАП 1: Сборка
FROM maven:3.9.3-eclipse-temurin-17 AS builder
WORKDIR /build

# Копируем pom и скачиваем зависимости
COPY pom.xml .
RUN mvn dependency:resolve

# Копируем исходники и собираем проект (включая тесты)
COPY src ./src
RUN mvn clean compile test-compile

# ЭТАП 2: Запуск Тестов
FROM maven:3.9.3-eclipse-temurin-17
WORKDIR /app

# Копируем собранные классы и зависимости из builder
COPY --from=builder /build/target ./target
COPY --from=builder /build/pom.xml .

# Копируем исходники (нужны для Allure отчетов)
COPY src ./src

# Команда по умолчанию:
# 1. Запускает тесты
# 2. Поднимает Allure отчет
CMD ["sh", "-c", "mvn test && exec mvn allure:serve"]