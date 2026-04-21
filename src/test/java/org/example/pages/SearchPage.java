package org.example.pages;

import io.qameta.allure.Step;
import org.example.utils.CoordinateUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static org.example.utils.WaitUtils.waitForElementVisible;

public class SearchPage extends BasePage {
    @FindBy(css = ".geocoder-control input")
    private WebElement searchField;

    @FindBy(css = ".coordinate")
    private WebElement coordinatesDisplay;

    @FindBy(css = ".search__list-item a")
    private List<WebElement> suggestions;

    @FindBy(css = ".map-container, #map, .leaflet-container, .mapboxgl-map")
    private WebElement mapArea;

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    @Step("Ввод координат '{coordinates}' и выбор из списка")
    public SearchPage searchAddress(String coordinates) {
        String convertedCoordinates = CoordinateUtils.toNumericFormat(coordinates);
        type(searchField, convertedCoordinates);

        if (!suggestions.isEmpty()) {
            click(suggestions.get(0));
        } else {
            searchField.sendKeys(Keys.ENTER);
        }

        actions.moveToElement(mapArea).perform();
        waitForElementVisible(driver, coordinatesDisplay, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Получение координат для проверок")
    public String getCoordinates() {
        return this.getText(coordinatesDisplay);
    }

    @Step("Ожидание исчезновения экрана загрузки")
    public SearchPage waitForLoading() {
        waitForLoadingScreenToDisappear();
        return this;
    }
}