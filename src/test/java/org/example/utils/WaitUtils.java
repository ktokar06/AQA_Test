package org.example.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Класс для ожидания различных состояний элементов на веб-странице.
 */
public final class WaitUtils {

    /**
     * Ожидает, пока элемент станет кликабельным (видимым и включенным).
     *
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
     * Ожидает, пока все элементы в списке станут видимыми.
     *
     * @param driver   экземпляр WebDriver
     * @param elements список веб-элементов для ожидания
     * @param timeout  время ожидания в секундах
     * @return список видимых веб-элементов
     */
    public static List<WebElement> waitForAllElementsVisible(WebDriver driver, List<WebElement> elements, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        return wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }
}