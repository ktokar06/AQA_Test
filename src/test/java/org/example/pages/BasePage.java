package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static org.example.utils.WaitUtils.waitForElementClickable;
import static org.example.utils.WaitUtils.waitForElementInvisible;
import static org.example.utils.WaitUtils.waitForElementVisible;
import static org.example.utils.WaitUtils.waitForPresenceOfElement;

public abstract class BasePage {
    protected WebDriver driver;
    protected Actions actions;
    protected static final int DEFAULT_TIMEOUT = 15;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    @Step("Клик по элементу")
    protected void click(WebElement element) {
        waitForElementClickable(driver, element, DEFAULT_TIMEOUT).click();
    }

    @Step("Ввод текста: '{text}'")
    protected void type(WebElement element, String text) {
        waitForElementVisible(driver, element, DEFAULT_TIMEOUT);
        element.clear();
        element.sendKeys(text);
    }

    @Step("Получение текста")
    protected String getText(WebElement element) {
        waitForElementVisible(driver, element, DEFAULT_TIMEOUT);
        return element.getText();
    }

    @Step("Ожидание исчезновения экрана загрузки")
    public void waitForLoadingScreenToDisappear() {
        By[] loaderSelectors = {
                By.cssSelector(".spinner"),
                By.cssSelector(".loader"),
                By.cssSelector(".loading"),
                By.cssSelector(".preloader"),
                By.cssSelector("[class*='spinner']"),
                By.cssSelector("[class*='loader']"),
                By.cssSelector("[class*='loading']"),
                By.cssSelector(".map-loading"),
                By.xpath("//div[contains(@class, 'loader')]"),
                By.xpath("//div[contains(@class, 'spinner')]")
        };

        for (By selector : loaderSelectors) {
            List<WebElement> elements = driver.findElements(selector);

            for (WebElement element : elements) {
                if (element.isDisplayed()) {
                    waitForElementInvisible(driver, selector, DEFAULT_TIMEOUT);
                    break;
                }
            }
        }

        waitForPresenceOfElement(driver, By.cssSelector(".mapboxgl-canvas"), DEFAULT_TIMEOUT);
    }
}