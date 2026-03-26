package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.example.utils.WaitUtils.*;

public class SearchPage extends BasePage {
    private static final int DEFAULT_TIMEOUT = 15;

    @FindBy(css = ".geocoder-control input")
    private WebElement searchField;

    @FindBy(css = ".coordinate")
    private WebElement coordinatesDisplay;

    @FindBy(css = ".search__list-item a")
    private WebElement firstSuggestion;

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    @Step("Ввод адреса '{address}' и выбор из списка")
    public SearchPage searchAddress(String address) {
        waitForElementClickable(driver, searchField, DEFAULT_TIMEOUT).sendKeys(address);
        waitForElementClickable(driver, firstSuggestion, DEFAULT_TIMEOUT).click();
        return this;
    }

    @Step("Получение координат для проверок")
    public String getCoordinates() {
        return coordinatesDisplay.getText();
    }
}