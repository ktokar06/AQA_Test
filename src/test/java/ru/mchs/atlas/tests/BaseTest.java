package ru.mchs.atlas.tests;

import org.openqa.selenium.PageLoadStrategy;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import ru.mchs.atlas.utils.NetworkBlocker;
import ru.mchs.atlas.listeners.TestListener;

import java.net.MalformedURLException;
import java.net.URL;

@Listeners({AllureTestNg.class, TestListener.class})
public class BaseTest {
    protected WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Настраивает WebDriver для тестов.
     * <p>
     * Эта настройка предназначена для запуска только в Docker-среде.
     * Важно: эта конфигурация не поддерживает локальный запуск
     * на машине разработчика. Запуск вне Docker приведёт к ошибке,
     * так как RemoteWebDriver не сможет подключиться к Selenium-контейнеру.
     */
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

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
