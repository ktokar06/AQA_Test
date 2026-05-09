# Глобальные аргументы
ARG MAVEN_REPO_LOCAL=/tmp/maven-cache
ARG MAVEN_OPTS=-Dmaven.repo.local=${MAVEN_REPO_LOCAL}

# ЭТАП 1: Сборка
FROM maven:3.9.3-eclipse-temurin-17 AS builder
ARG MAVEN_REPO_LOCAL
ARG MAVEN_OPTS
WORKDIR /build

# Копируем pom и скачиваем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline ${MAVEN_OPTS}

# Копируем исходники и собираем проект (включая тесты)
COPY src ./src
RUN mvn test-compile dependency:resolve-plugins ${MAVEN_OPTS}

# ЭТАП 2: Запуск Тестов
FROM maven:3.9.3-eclipse-temurin-17
ARG MAVEN_REPO_LOCAL
ARG MAVEN_OPTS
ENV MAVEN_OPTS=${MAVEN_OPTS}
WORKDIR /app

# Копируем кэш Maven
COPY --from=builder ${MAVEN_REPO_LOCAL} ${MAVEN_REPO_LOCAL}
COPY --from=builder /build/src /app/src
COPY --from=builder /build/pom.xml /app/

# Команда по умолчанию:
# 1. Запускает тесты
# 2. Поднимает Allure отчет
CMD ["sh", "-c", "mvn test -q ${MAVEN_OPTS} && mvn allure:serve -q ${MAVEN_OPTS}"]