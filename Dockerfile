# Глобальный аргумент
ARG MAVEN_OPTS=-Dmaven.repo.local=/tmp/maven-cache

# ЭТАП 1: Сборка
FROM maven:3.9.3-eclipse-temurin-17 AS builder
ARG MAVEN_OPTS
WORKDIR /build

# Копируем pom и скачиваем зависимости
COPY pom.xml .
RUN mkdir -p /tmp/maven-cache && mvn dependency:go-offline ${MAVEN_OPTS}

# Копируем исходники и собираем проект (включая тесты)
COPY src ./src
RUN mvn test-compile ${MAVEN_OPTS}

# ЭТАП 2: Запуск Тестов
FROM maven:3.9.3-eclipse-temurin-17
ARG MAVEN_OPTS
ENV MAVEN_OPTS=${MAVEN_OPTS}
WORKDIR /app

# Копируем кэш Maven (зависимости + плагины + собранные классы)
COPY --from=builder /tmp/maven-cache /tmp/maven-cache
COPY --from=builder /build/src /app/src
COPY --from=builder /build/pom.xml /app/

# Команда по умолчанию:
# 1. Запускает тесты
# 2. Поднимает Allure отчет
CMD ["sh", "-c", "mvn test ${MAVEN_OPTS} && mvn allure:serve ${MAVEN_OPTS}"]