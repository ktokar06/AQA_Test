package ru.mchs.atlas.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.mchs.atlas.utils.WaitUtils;

public class MapPage extends BasePage {
    @FindBy(css = ".mapboxgl-canvas")
    private WebElement mapCanvas;

    public MapPage(WebDriver driver) {
        super(driver);
    }

    @Step("Ожидание загрузки карты и возврат страницы")
    public MapPage waitForMapToLoad() {
        WaitUtils.waitForElementVisible(driver, mapCanvas, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Ожидание исчезновения экрана загрузки")
    public MapPage waitForLoading() {
        waitForLoadingScreenToDisappear();
        return this;
    }

    @Step("Приближение карты")
    public MapPage zoomInMap() {
        actions.moveToElement(mapCanvas).click().sendKeys("+").perform();
        return this;
    }

    @Step("Перемещение карты")
    public MapPage moveMap() {
        WaitUtils.waitForElementVisible(driver, mapCanvas, DEFAULT_TIMEOUT);
        WaitUtils.waitForElementClickable(driver, mapCanvas, DEFAULT_TIMEOUT);

        actions.moveToElement(mapCanvas)
                .clickAndHold()
                .moveByOffset(150, 100)
                .release()
                .perform();
        return this;
    }

    @Step("Проверка отображения карты")
    public boolean isMapDisplayed() {
        WaitUtils.waitForElementVisible(driver, mapCanvas, DEFAULT_TIMEOUT);
        return mapCanvas.isDisplayed();
    }

    @Step("Проверка, что карта перемещена")
    public boolean isMapMoved() {
        return mapCanvas.isDisplayed();
    }
}