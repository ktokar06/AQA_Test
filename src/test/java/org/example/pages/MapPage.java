package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static org.example.utils.WaitUtils.*;

public class MapPage extends BasePage {
    private static final int DEFAULT_TIMEOUT = 15;

    @FindBy(css = ".theme-control label")
    private WebElement themeToggle;

    @FindBy(css = ".tiles-component__button")
    private WebElement mapLayerButton;

    @FindBy(css = ".dropdown-menu__list .dropdown-menu__item")
    private List<WebElement> mapLayers;

    @FindBy(xpath = "//p[contains(text(),'Маршруты')]")
    private WebElement routesTab;

    @FindBy(xpath = "//button[contains(@class, 'btn-primary') and contains(., 'Построить маршрут')]")
    private WebElement buildRouteButton;

    @FindBy(css = ".route-setting-panel")
    private WebElement routePanel;

    @FindBy(xpath = "(//div[@class='mapbox-directions-destination']//input)[1]")
    private WebElement fromField;

    @FindBy(xpath = "(//div[@class='mapbox-directions-destination']//input)[2]")
    private WebElement toField;

    @FindBy(css = ".mapbox-directions-route-summary h1")
    private WebElement routeDistance;

    public MapPage(WebDriver driver) {
        super(driver);
    }

    @Step("Переключение темы")
    public MapPage switchTheme() {
        waitForElementClickable(driver, themeToggle, DEFAULT_TIMEOUT).click();
        return this;
    }

    @Step("Построение маршрута")
    public MapPage buildRoute() {
        waitForElementClickable(driver, routesTab, DEFAULT_TIMEOUT).click();
        waitForElementClickable(driver, buildRouteButton, DEFAULT_TIMEOUT).click();
        waitForElementClickable(driver, routePanel, DEFAULT_TIMEOUT);

        waitForElementClickable(driver, fromField, DEFAULT_TIMEOUT).click();
        new Actions(driver).moveByOffset(500, 300).click().perform();

        waitForElementClickable(driver, toField, DEFAULT_TIMEOUT).click();
        new Actions(driver).moveByOffset(700, 400).click().perform();

        return this;
    }

    @Step("Проверка включена ли светлая тема")
    public boolean isLightThemeEnabled() {
        return driver.findElement(By.tagName("body")).getAttribute("class").contains("light-theme");
    }

    @Step("Проверка доступности более двух подложек")
    public boolean areMapLayersAvailable() {
        waitForElementClickable(driver, mapLayerButton, DEFAULT_TIMEOUT).click();
        waitForAllElementsVisible(driver, mapLayers, DEFAULT_TIMEOUT);
        return mapLayers.size() > 2;
    }

    @Step("Получение расстояния маршрута")
    public String getRouteDistance() {
        return routeDistance.getText();
    }
}