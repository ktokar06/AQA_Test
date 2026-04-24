package ru.mchs.atlas.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * <p>Класс для ожидания различных состояний элементов на веб-странице.</p>
 */
public final class WaitUtils {

    /**
     * Ожидает, пока элемент станет видимым.
     * <p>
     * @param driver  экземпляр WebDriver
     * @param element веб-элемент для ожидания
     * @param timeout время ожидания в секундах
     * @return видимый веб-элемент
     */
    public static WebElement waitForElementVisible(WebDriver driver, WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    /**
     * Ожидает, пока элемент станет кликабельным.
     * <p>
     * @param driver  экземпляр WebDriver
     * @param element веб-элемент для ожидания
     * @param timeout время ожидания в секундах
     * @return кликабельный веб-элемент
     */
    public static WebElement waitForElementClickable(WebDriver driver, WebElement element, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Ожидает, пока элемент появится в DOM
     * <p>
     * @param driver  экземпляр WebDriver
     * @param locator локатор элемента
     * @param timeout время ожидания в секундах
     * @return найденный веб-элемент
     */
    public static WebElement waitForPresenceOfElement(WebDriver driver, By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Ожидает, пока все элементы в списке станут видимыми.
     * <p>
     * @param driver   экземпляр WebDriver
     * @param elements список веб-элементов для ожидания
     * @param timeout  время ожидания в секундах
     * @return список видимых веб-элементов
     */
    public static List<WebElement> waitForAllElementsVisible(WebDriver driver, List<WebElement> elements, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    /**
     * Ожидает, пока элемент станет невидимым или исчезнет из DOM
     * <p>
     * @param driver  экземпляр WebDriver
     * @param locator локатор элемента
     * @param timeout время ожидания в секундах
     * @return true если элемент исчез, false если таймаут
     */
    public static boolean waitForElementInvisible(WebDriver driver, By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}