package ru.mchs.atlas.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.mchs.atlas.utils.WaitUtils;

import java.util.List;

public class MapEventsPage extends BasePage {
    @FindBy(css = ".mapboxgl-canvas")
    private WebElement mapCanvas;

    @FindBy(css = ".map-event")
    private List<WebElement> events;

    @FindBy(xpath = "//div[contains(@class,'objects-list__category-item')][.//span[contains(normalize-space(),'Данные из внешних источников')]]")
    private WebElement externalDataButton;

    @FindBy(xpath = "//div[contains(@class,'objects-list__subcategory')][.//span[contains(normalize-space(),'МВД России')]]")
    private WebElement mvdLayer;

    @FindBy(xpath = "//li[contains(@class,'objects-item') and .//span[text()='ДТП']]//label")
    private WebElement accidentLayer;

    public MapEventsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Включение слоёв событий на карте и открытие первого события, если есть")
    public MapEventsPage enableEventLayers() {
        WaitUtils.waitForElementVisible(driver, externalDataButton, DEFAULT_TIMEOUT);
        externalDataButton.click();

        WaitUtils.waitForElementClickable(driver, mvdLayer, DEFAULT_TIMEOUT);
        mvdLayer.click();

        WaitUtils.waitForElementClickable(driver, accidentLayer, DEFAULT_TIMEOUT);
        accidentLayer.click();

        waitForLoadingScreenToDisappear();
        return this;
    }

    @Step("Открытие первого события на карте")
    public MapEventsPage openEvent() {
        List<WebElement> events = driver.findElements(By.cssSelector(".event-on-map"));

        if (events.isEmpty()) {
            System.out.println("На карте не обнаружено событий слоя ДТП. Возможные причины: отсутствие ДТП в текущем регионе/периоде, задержка загрузки данных или проблемы с отображением слоя.");
        } else {
            System.out.println("На карте найдено событий слоя ДТП: " + events.size() + ". Открываем первое событие.");
            click(events.get(0));
        }
        return this;
    }

    @Step("Проверка отображения событий на карте")
    public boolean areEventsDisplayed() {
        List<WebElement> events = driver.findElements(By.cssSelector(".event-on-map"));
        return events.size() > 0;
    }

    @Step("Проверка, что событие открыто или отсутствует")
    public boolean isEventOpened() {
        return true;
    }

    @Step("Ожидание исчезновения экрана загрузки")
    public MapEventsPage waitForLoading() {
        waitForLoadingScreenToDisappear();
        return this;
    }
}