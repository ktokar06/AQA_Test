package ru.mchs.atlas.utils;

import com.google.common.collect.ImmutableList;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v130.network.Network;
import org.openqa.selenium.devtools.v130.network.model.ErrorReason;
import org.openqa.selenium.devtools.v130.network.model.RequestPattern;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Optional;

/**
 * Класс для блокировки сетевых запросов в браузерах Chrome и Firefox.
 */
public final class NetworkBlocker {

    /**
     * Блокирует сетевые запросы в Chrome.
     *
     * @param driver экземпляр ChromeDriver, в котором необходимо заблокировать запросы
     * @throws ClassCastException если переданный WebDriver не является экземпляром ChromeDriver
     */
    public static void blockChrome(WebDriver driver) {
        ChromeDriver chromeDriver = (ChromeDriver) driver;
        DevTools devTools = chromeDriver.getDevTools();
        devTools.createSession();

        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));

        devTools.send(Network.setRequestInterception(
                ImmutableList.of(new RequestPattern(Optional.of("*"), Optional.empty(), Optional.empty()))
        ));

        devTools.addListener(Network.requestIntercepted(), request -> {
            String url = request.getRequest().getUrl();
            boolean allowed = isDomainAllowed(url, "atlas.mchs.gov.ru");

            if (allowed) {
                devTools.send(Network.continueInterceptedRequest(
                        request.getInterceptionId(),
                        Optional.empty(),
                        Optional.empty(), Optional.empty(), Optional.empty(),
                        Optional.empty(), Optional.empty(), Optional.empty()
                ));
            } else {
                devTools.send(Network.continueInterceptedRequest(
                        request.getInterceptionId(),
                        Optional.of(ErrorReason.FAILED),
                        Optional.empty(), Optional.empty(), Optional.empty(),
                        Optional.empty(), Optional.empty(), Optional.empty()
                ));
            }
        });
    }

    /**
     * Проверяет, разрешён ли указанный URL.
     *
     * @param url URL для проверки
     * @param allowedDomain разрешённый домен
     * @return true если URL разрешён, false в противном случае
     */
    private static boolean isDomainAllowed(String url, String allowedDomain) {
        try {
            java.net.URL parsedUrl = new java.net.URL(url);
            String protocol = parsedUrl.getProtocol();

            if (!"http".equals(protocol) && !"https".equals(protocol)) {
                return false;
            }

            String host = parsedUrl.getHost();
            if (host == null || host.isEmpty()) return false;

            host = java.net.IDN.toASCII(host);

            return host.equals(allowedDomain) || host.endsWith("." + allowedDomain);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Настраивает блокировку ресурсов в Firefox через preferences с использованием белого списка.
     * Отключает загрузку изображений, стилей, шрифтов, предзагрузку DNS и спекулятивную загрузку.
     *
     * @param options экземпляр FirefoxOptions для настройки
     * @return настроенный FirefoxOptions с применёнными блокировками
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