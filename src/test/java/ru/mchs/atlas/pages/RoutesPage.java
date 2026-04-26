package ru.mchs.atlas.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.mchs.atlas.utils.WaitUtils;

public class RoutesPage extends BasePage {
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

    @FindBy(css = ".mapboxgl-canvas")
    private WebElement mapCanvas;

    public RoutesPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открытие вкладки маршрутов")
    public RoutesPage openRoutesTab() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", routesTab);
        WaitUtils.waitForElementVisible(driver, routesTab, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Построение маршрута")
    public RoutesPage buildRoute() {
        click(buildRouteButton);
        WaitUtils.waitForElementVisible(driver, routePanel, DEFAULT_TIMEOUT);
        click(fromField);
        actions.moveByOffset(500, 300).click().perform();
        click(toField);
        actions.moveByOffset(700, 400).click().perform();
        return this;
    }

    @Step("Получение расстояния маршрута")
    public String getRouteDistance() {
        return getText(routeDistance);
    }

    @Step("Ожидание загрузки карты")
    public RoutesPage waitForMapToLoad() {
        WaitUtils.waitForElementVisible(driver, mapCanvas, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Ожидание исчезновения экрана загрузки")
    public RoutesPage waitForLoading() {
        waitForLoadingScreenToDisappear();
        return this;
    }
}