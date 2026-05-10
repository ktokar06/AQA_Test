# Руководство по запуску тестов
## 1. BaseTest.java — основные возможности
- Настройка WebDriver
- Два режима работы:
    - **Docker-режим** — через RemoteWebDriver и Selenium-контейнер
    - **Локальный режим** — через ChromeDriver или FirefoxDriver

---

## 2. Запуск тестов
### Через Docker (рекомендуемый способ)

Самый простой способ запуска — одной командой поднимается всё необходимое окружение:

```bash
docker compose up
```

Что происходит при запуске:
- Сборка проекта в Docker-контейнере
- Автоматический запуск Selenium Grid (Chrome и Firefox)
- Выполнение всех тестов
- Генерация и запуск Allure-отчёта

После завершения отчёт доступен по адресу:  
`http://127.0.0.1:5050/allure-docker-service/projects/default/reports/latest/index.html#`

---

### Локально (для отладки и разработки)

```bash
# Запуск всех тестов
mvn clean test
```

---

### Через IDE

1. Откройте файл `testng.xml`
2. Нажмите **Run**
3. Тесты запустятся параллельно в выбранном браузере

## 3. Варианты конфигурации BaseTest

### Docker-режим

```java
@BeforeMethod
@Parameters("browser")
public void setUp(String browser) throws MalformedURLException {
  RemoteWebDriver remote;

  switch (browser.toLowerCase()) {
    case "firefox":
      FirefoxOptions firefoxOptions = new FirefoxOptions();
      firefoxOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
      firefoxOptions.addArguments("--headless");
      firefoxOptions.addArguments("--width=1920");
      firefoxOptions.addArguments("--height=1080");

      remote = new RemoteWebDriver(
              new URL("http://selenium-firefox:4444"),
              firefoxOptions
      );
      break;

    case "chrome":
    default:
      ChromeOptions chromeOptions = new ChromeOptions();
      chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
      chromeOptions.addArguments("--headless");
      chromeOptions.addArguments("--no-sandbox");
      chromeOptions.addArguments("--disable-dev-shm-usage");
      chromeOptions.addArguments("--window-size=1920,1080");
      chromeOptions.addArguments("--disable-blink-features=AutomationControlled");

      remote = new RemoteWebDriver(
              new URL("http://selenium-chrome:4444"),
              chromeOptions
      );

      break;
  }

  remote.setFileDetector(new LocalFileDetector());
  driver = remote;
}
```

**Особенности:**
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

**Особенности:**
- Позволяет запускать тесты локально
- Поддерживает Chrome и Firefox
- Можно указывать браузер через параметр TestNG

---

## 4. Отчёты (Allure)

| Действие | Команда / URL |
|----------|----------------|
| Результаты сохраняются в | `target/allure-results` |
| Просмотр отчета в Docker | `http://127.0.0.1:5050/allure-docker-service/projects/default/reports/latest/index.html#` |
