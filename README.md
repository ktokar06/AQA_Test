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
`http://localhost:5050/allure-docker-service/projects/default/reports/latest/index.html#suites`

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
  String seleniumPort = System.getenv().getOrDefault("SELENIUM_PORT", "4444");
  String seleniumHost;

  switch (browser.toLowerCase()) {
    case "firefox":
      seleniumHost = System.getenv().getOrDefault("FIREFOX_HOST", "selenium-firefox");
      FirefoxOptions firefoxOptions = new FirefoxOptions();
      firefoxOptions.addArguments("--headless");
      firefoxOptions.addArguments("--width=1920");
      firefoxOptions.addArguments("--height=1080");

      remote = new RemoteWebDriver(
              new URL("http://" + seleniumHost + ":" + seleniumPort),
              firefoxOptions
      );
      break;

    case "chrome":
    default:
      seleniumHost = System.getenv().getOrDefault("CHROME_HOST", "selenium-chrome");
      ChromeOptions options = new ChromeOptions();
      options.addArguments("--headless");
      options.addArguments("--no-sandbox");
      options.addArguments("--disable-dev-shm-usage");
      options.addArguments("--window-size=1920,1080");
      options.addArguments("--disable-blink-features=AutomationControlled");

      remote = new RemoteWebDriver(
              new URL("http://" + seleniumHost + ":" + seleniumPort),
              options
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
| Просмотр отчета в Docker | `http://localhost:5050/allure-docker-service/projects/default/reports/latest/index.html#suites` |

---

## 5. Пример .env

```env
ALLURE_PORT=5050
CONTAINER_PORT=5050

CHROME_HOST=selenium-chrome
FIREFOX_HOST=selenium-firefox
SELENIUM_PORT=4444
```

---

## 6. Примечания

- Для локального режима требуется установленный Chrome или Firefox
- Docker-режим автоматически поднимает Selenium Grid
- Allure отчёт доступен только после выполнения тестов