# Руководство по запуску тестов
## 1. Структура проекта

* `src/test/java/org/example/tests/` – папка с тестами
* `BaseTest.java` – базовый класс для всех тестов

    * Содержит настройку `WebDriver`
    * Может использоваться в двух вариантах:

        1. **Docker-режим** – через `RemoteWebDriver` и Selenium-контейнер
        2. **Локальный режим** – через `ChromeDriver` или `FirefoxDriver` на машине разработчика
* `testng.xml` – конфигурация для запуска всех тестов и параллельного запуска

---

## 2. Варианты `BaseTest`
### Docker-режим

```java
@BeforeMethod
public void setUp() throws MalformedURLException {
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless=new");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--window-size=1920,1080");

    RemoteWebDriver remote = new RemoteWebDriver(
            new URL("http://seleniuъm:4444/wd/hub"),
            options
    );
    remote.setFileDetector(new LocalFileDetector());
    driver = remote;
}

```

* Используется **только в Docker-среде**
* Подходит для CI/CD и командной работы
* Локальный запуск вне Docker приведёт к ошибке подключения к Selenium-контейнеру

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

* Позволяет запускать тесты локально
* Поддерживает Chrome и Firefox
* Можно указывать браузер через параметр TestNG

---

## 3. Запуск тестов
### Через Docker

```bash
# Сборка образов
docker compose build

# Запуск тестов
docker compose run --rm test-runner
```

### Локально

```bash
# Запуск всех тестов
mvn clean test
```

### Через IDE
* Найти файл `testng.xml`
* Кликнуть `Run`
* Запустится параллельный запуск тестов в выбранном браузере

---

## 4. Отчеты

* Allure сохраняет результаты в `target/allure-results`
* Генерация отчета в `target/site/allure-maven-plugin`
* Для локального быстрого просмотра:

```bash
mvn allure:serve
```