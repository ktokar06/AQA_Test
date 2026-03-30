package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static org.example.utils.WaitUtils.waitForAllElementsVisible;
import static org.example.utils.WaitUtils.waitForElementVisible;

public class MapPage extends BasePage {
    @FindBy(css = "#theme")
    private WebElement lightThemeIndicator;

    @FindBy(css = ".theme-control label")
    private WebElement themeToggle;

    @FindBy(css = ".tiles-component__button")
    private WebElement mapLayerButton;

    @FindBy(css = ".tiles-component .dropdown-menu__list .dropdown-menu__item")
    private List<WebElement> mapLayers;

    @FindBy(xpath = "//div[@class='sidebar-tabs']//p[contains(text(),'Маршруты')]")
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
        waitForElementVisible(driver, themeToggle, DEFAULT_TIMEOUT);

        click(themeToggle);
        return this;
    }

    @Step("Построение маршрута")
    public MapPage buildRoute() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", routesTab);

        waitForElementVisible(driver, routesTab, DEFAULT_TIMEOUT);

        click(buildRouteButton);

        waitForElementVisible(driver, routePanel, DEFAULT_TIMEOUT);

        click(fromField);
        actions.moveByOffset(500, 300).click().perform();

        click(toField);
        actions.moveByOffset(700, 400).click().perform();
        return this;
    }

    @Step("Проверка включена ли светлая тема")
    public boolean isLightThemeEnabled() {
        return !lightThemeIndicator.isSelected();
    }

    @Step("Проверка доступности более двух подложек")
    public boolean areMapLayersAvailable() {
        click(mapLayerButton);
        waitForAllElementsVisible(driver, mapLayers, DEFAULT_TIMEOUT);
        return mapLayers.size() > 2;
    }

    @Step("Получение расстояния маршрута")
    public String getRouteDistance() {
        return getText(routeDistance);
    }

    @Step("Ожидание исчезновения экрана загрузки и возврат страницы")
    public MapPage waitForLoading() {
        waitForLoadingScreenToDisappear();
        return this;
    }
}