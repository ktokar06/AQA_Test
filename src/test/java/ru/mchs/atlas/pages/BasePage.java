package ru.mchs.atlas.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import ru.mchs.atlas.utils.WaitUtils;

public abstract class BasePage {
    protected static final int DEFAULT_TIMEOUT = 15;
    protected WebDriver driver;
    protected Actions actions;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
        PageFactory.initElements(driver, this);
    }

    @Step("Клик по элементу")
    protected void click(WebElement element) {
        WaitUtils.waitForElementClickable(driver, element, DEFAULT_TIMEOUT).click();
    }

    @Step("Ввод текста: '{text}'")
    protected void type(WebElement element, String text) {
        WaitUtils.waitForElementVisible(driver, element, DEFAULT_TIMEOUT);
        element.clear();
        element.sendKeys(text);
    }

    @Step("Получение текста")
    protected String getText(WebElement element) {
        WaitUtils.waitForElementVisible(driver, element, DEFAULT_TIMEOUT);
        return element.getText();
    }

    protected void waitForLoadingScreenToDisappear() {
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
            try {
                WaitUtils.waitForElementInvisible(driver, selector, DEFAULT_TIMEOUT);
            } catch (Exception e) {

            }
        }

        WaitUtils.waitForPresenceOfElement(driver, By.cssSelector(".mapboxgl-canvas"), DEFAULT_TIMEOUT);
    }
}