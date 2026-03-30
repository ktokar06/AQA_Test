package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static org.example.utils.CoordinateUtils.toNumericFormat;
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

    @Step("Ввод адреса '{address}' и выбор из списка")
    public SearchPage searchAddress(String address) {
        String convertedAddress = toNumericFormat(address);
        type(searchField, convertedAddress);

        switch (suggestions.isEmpty() ? 0 : 1) {
            case 0 -> searchField.sendKeys(Keys.ENTER);
            case 1 -> click(suggestions.get(0));
        }

        actions.moveToElement(mapArea).perform();
        waitForElementVisible(driver, coordinatesDisplay, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Получение координат для проверок")
    public String getCoordinates() {
        return getText(coordinatesDisplay);
    }

    @Step("Ожидание исчезновения экрана загрузки и возврат страницы")
    public SearchPage waitForLoading() {
        waitForLoadingScreenToDisappear();
        return this;
    }
}