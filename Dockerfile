# ЭТАП 1: Сборка
FROM maven:3.9.3-eclipse-temurin-17 AS builder
WORKDIR /build

# Копируем pom и скачиваем зависимости
COPY pom.xml .
RUN mvn dependency:resolve

# Копируем исходники и собираем проект
COPY src ./src
RUN mvn clean compile test-compile

# ЭТАП 2: Запуск Тестов
FROM maven:3.9.3-eclipse-temurin-17
WORKDIR /app

# Копируем собранные классы и зависимости из builder
COPY --from=builder /root/.m2 /root/.m2
COPY --from=builder /build/ /app/

# Команда по умолчанию:
# 1. Запускает тесты
# 2. Поднимает Allure отчет
CMD ["sh", "-c", "mvn test && exec mvn allure:serve"]