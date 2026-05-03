package ru.mchs.atlas.utils;

import com.google.common.collect.ImmutableList;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v130.network.Network;

import java.util.Optional;

/**
 * Класс для блокировки сетевых запросов в браузерах с использованием белого списка.
 */
public final class NetworkBlocker {

    /**
     * Блокирует все ресурсы в Chrome через Chrome DevTools Protocol,
     * разрешает только те URL, которые указаны в белом списке.
     *
     * @param driver экземпляр ChromeDriver
     * @throws IllegalArgumentException если driver не является ChromeDriver
     */
    public static void blockChrome(WebDriver driver) {
        ChromeDriver chromeDriver = (ChromeDriver) driver;
        DevTools devTools = chromeDriver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.send(Network.setBlockedURLs(ImmutableList.of("*")));
        devTools.send(Network.setBlockedURLs(ImmutableList.of(
                "https://atlas.mchs.gov.ru/*",
                "https://*.mchs.gov.ru/*",
                "https://cdn.jsdelivr.net/*",
                "https://fonts.googleapis.com/*",
                "https://fonts.gstatic.com/*"
        )));
    }

    /**
     * Настроить блокировку ресурсов в Firefox через preferences с использованием белого списка.
     * Отключает все ресурсы, кроме тех, что указаны в белом списке.
     *
     * @param options экземпляр FirefoxOptions для настройки
     * @return настроенный FirefoxOptions с примененными блокировками
     */
    public static FirefoxOptions blockFirefox(FirefoxOptions options) {
        options.addPreference("permissions.default.image", 2);
        options.addPreference("browser.display.use_document_fonts", 0);
        options.addPreference("network.http.speculative-parallel-limit", 0);
        options.addPreference("network.dns.disablePrefetch", true);
        options.addPreference("network.prefetch-next", false);
        options.addPreference("permissions.default.stylesheet", 2);

        options.addPreference("network.http.non-negotiate", true);

        return options;
    }
}