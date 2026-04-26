package ru.mchs.atlas.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.mchs.atlas.utils.WaitUtils;

public class TouristRoutesPage extends BasePage {
    @FindBy(xpath = "//div[@class='sidebar-tabs']//p[contains(text(),'Маршруты')]")
    private WebElement routesTab;

    @FindBy(xpath = "//span[contains(text(),'Кроноцкий заповедник')]")
    private WebElement kronotskyReserve;

    @FindBy(css = ".setting-panel-description")
    private WebElement routeInfoPanel;

    public TouristRoutesPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открытие вкладки маршрутов")
    public TouristRoutesPage openRoutesTab() {
        WaitUtils.waitForElementClickable(driver, routesTab, DEFAULT_TIMEOUT);
        click(routesTab);
        return this;
    }

    @Step("Выбор маршрута 'Кроноцкий заповедник'")
    public TouristRoutesPage selectKronotskyReserve() {
        WaitUtils.waitForElementVisible(driver, kronotskyReserve, DEFAULT_TIMEOUT);
        WaitUtils.waitForElementClickable(driver, kronotskyReserve, DEFAULT_TIMEOUT);
        click(kronotskyReserve);
        return this;
    }

    @Step("Ожидание отображения информации о маршруте")
    public TouristRoutesPage waitForRouteInfo() {
        WaitUtils.waitForElementVisible(driver, routeInfoPanel, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Проверка отображения информации о маршруте")
    public boolean isRouteInfoDisplayed() {
        return routeInfoPanel.isDisplayed();
    }

    @Step("Ожидание исчезновения экрана загрузки")
    public TouristRoutesPage waitForLoading() {
        waitForLoadingScreenToDisappear();
        return this;
    }
}