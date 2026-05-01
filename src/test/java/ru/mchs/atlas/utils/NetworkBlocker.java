package ru.mchs.atlas.utils;

import com.google.common.collect.ImmutableList;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v130.network.Network;

import java.util.Optional;

/**
 * Класс для блокировки сетевых запросов в браузерах.
 */
public final class NetworkBlocker {

    /**
     * Блокирует загрузку ресурсов в Chrome через Chrome DevTools Protocol.
     * Блокируются: изображения, шрифты, медиафайлы, аналитика, метрика, соцсети.
     *
     * @param driver экземпляр ChromeDriver
     * @throws IllegalArgumentException если driver не является ChromeDriver
     */
    public static void blockChrome(WebDriver driver) {
        ChromeDriver chromeDriver = (ChromeDriver) driver;
        DevTools devTools = chromeDriver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        devTools.send(Network.setBlockedURLs(ImmutableList.of(
                "*.jpg", "*.jpeg", "*.png", "*.gif", "*.svg", "*.webp", "*.ico",
                "*.woff", "*.woff2", "*.ttf", "*.eot", "*.otf",
                "*google-analytics.com*", "*googletagmanager.com*", "*doubleclick.net*",

                "*mc.yandex.ru*",
                "*.mp4", "*.mp3", "*.webm", "*.ogg"
        )));
    }

    /**
     * Настраивает блокировку ресурсов в Firefox через preferences.
     * Отключает изображения, шрифты, предзагрузку, телеметрию
     * и блокирует запросы к сервисам аналитики.
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

        return options;
    }
}