# Руководство по запуску тестов
### BaseTest.java — основные возможности:
- Настройка WebDriver
- Два режима работы:

Docker-режим — через RemoteWebDriver и Selenium-контейнер

Локальный режим — через ChromeDriver или FirefoxDriver

---

## 2. Варианты конфигурации BaseTest

### Docker-режим

```java
@BeforeMethod
@Parameters("browser")
public void setUp(String browser) throws MalformedURLException {
    RemoteWebDriver remote;

    switch (browser.toLowerCase()) {
        case "firefox":
            FirefoxOptions ffOptions = new FirefoxOptions();
            ffOptions.addArguments("--headless");
            ffOptions.addArguments("--width=1920");
            ffOptions.addArguments("--height=1080");

            remote = new RemoteWebDriver(
                    new URL("http://selenium-firefox:4444"),
                    ffOptions
            );
            break;

        case "chrome":
        default:
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--disable-blink-features=AutomationControlled");

            remote = new RemoteWebDriver(
                    new URL("http://selenium-chrome:4444"),
                    options
            );
            break;
    }

    remote.setFileDetector(new LocalFileDetector());
    driver = remote;
}
```

Особенности:
- Используется только в Docker-среде
- Подходит для CI/CD и командной работы
- Локальный запуск вне Docker приведёт к ошибке подключения

---

### Локальный режим

```java
@BeforeMethod
@Parameters("browser")
public void setUp(@Optional("chrome") String browser) {
    switch (browser.toLowerCase()) {
        case "chrome" -> {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        }
        case "firefox" -> {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        }
        default -> throw new IllegalArgumentException("Неподдерживаемый браузер: " + browser);
    }

    driver.manage().window().maximize();
}
```

Особенности:
- Позволяет запускать тесты локально
- Поддерживает Chrome и Firefox
- Можно указывать браузер через параметр TestNG

---

## 3. Запуск тестов
### Через Docker

```bash
docker compose up --build
```

### Локально

```bash
# Запуск всех тестов
mvn clean test
```

### Через IDE

1. Откройте файл `testng.xml`
2. Нажмите Run
3. Тесты запустятся параллельно в выбранном браузере

---

## 4. Отчёты (Allure)

| Действие                   | Команда / URL |
|----------------------------|----------------|
| Результаты сохраняются в   | `target/allure-results` |
| Просмотр отчета в Docker   | `http://localhost:5050/allure-docker-service/projects/default/reports/latest/index.html#suites` |

---

## 5. Пример .env

```env
ALLURE_PORT=5050
CONTAINER_PORT=5050
```

---

## 6. Примечания

- Для локального режима требуется установленный Chrome или Firefox
- Docker-режим автоматически поднимает Selenium Grid
- Allure отчёт доступен только после выполнения тестов
