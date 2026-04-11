package org.example.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.net.MalformedURLException;
import java.net.URL;

public class BaseTest {
    protected WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Настраивает WebDriver для тестов.
     *
     * Эта настройка предназначена для запуска только в Docker-среде.
     * Важно: эта конфигурация не поддерживает локальный запуск
     * на машине разработчика. Запуск вне Docker приведёт к ошибке,
     * так как RemoteWebDriver не сможет подключиться к Selenium-контейнеру.
     */
    @BeforeMethod
    public void setUp() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        String host = System.getenv("SELENIUM_HOST");
        String port = System.getenv("SELENIUM_PORT");

        String url = "http://" + host + ":" + port + "/wd/hub";

        RemoteWebDriver remote = new RemoteWebDriver(
                new URL(url),
                options
        );

        remote.setFileDetector(new LocalFileDetector());
        driver = remote;
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}