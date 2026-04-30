# ЭТАП 1: Сборка
FROM maven:3.9.3-eclipse-temurin-17 AS builder
WORKDIR /build

# Временная директория для кэша Maven
ENV MAVEN_OPTS="-Dmaven.repo.local=/tmp/maven-cache"

# Копируем pom и скачиваем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline -Dmaven.repo.local=/tmp/maven-cache

# Копируем исходники и собираем проект (включая тесты)
COPY src ./src
RUN mvn test-compile dependency:resolve-plugins -Dmaven.repo.local=/tmp/maven-cache

# ЭТАП 2: Запуск Тестов
FROM maven:3.9.3-eclipse-temurin-17
WORKDIR /app

# Копируем кэш Maven (зависимости + плагины + собранные классы)
COPY --from=builder /tmp/maven-cache /tmp/maven-cache

# Копируем исходники и pom
COPY --from=builder /build/src /app/src
COPY --from=builder /build/pom.xml /app/

# Указываем Maven использовать временную директорию
ENV MAVEN_OPTS="-Dmaven.repo.local=/tmp/maven-cache"

# Команда по умолчанию:
# 1. Запускает тесты
# 2. Поднимает Allure отчет
CMD ["sh", "-c", "mvn test && exec mvn allure:serve"]