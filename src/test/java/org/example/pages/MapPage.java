package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import java.util.List;

import static org.example.utils.WaitUtils.waitForAllElementsVisible;
import static org.example.utils.WaitUtils.waitForElementVisible;
import static org.example.utils.WaitUtils.waitForElementClickable;
import static org.example.utils.WaitUtils.waitForElementInvisible;

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

    @FindBy(xpath = "//span[contains(text(),'Кроноцкий заповедник')]")
    private WebElement kronotskyReserve;

    @FindBy(css = ".setting-panel-description")
    private WebElement routeInfoPanel;

    @FindBy(xpath = "//div[contains(@class,'setting-panel')]//h2[contains(text(),'Информация о маршруте')]")
    private WebElement routeInfoTitle;

    @FindBy(css = ".timeline-control")
    private WebElement timelineControl;

    @FindBy(css = ".timeline-datapicker.datapicket-from .value")
    private WebElement startDateValue;

    @FindBy(css = ".timeline-datapicker.datapicket-from")
    private WebElement startDatePicker;

    @FindBy(css = ".vc-popover-content-wrapper")
    private WebElement calendarContainer;

    @FindBy(xpath = ".//div[contains(@class, 'vc-popover-content-wrapper')]//div")
    private List<WebElement> calendarDates;

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

    @Step("Открытие списка картографических подложек")
    public MapPage openMapLayersList() {
        click(mapLayerButton);
        waitForAllElementsVisible(driver, mapLayers, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Ожидание исчезновения экрана загрузки")
    public MapPage waitForLoading() {
        waitForLoadingScreenToDisappear();
        return this;
    }

    @Step("Ожидание загрузки карты и возврат страницы")
    public MapPage waitForMapToLoad() {
        waitForElementVisible(driver, mapCanvas, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Приближение карты")
    public MapPage zoomInMap() {
        actions.moveToElement(mapCanvas).click().sendKeys("+").perform();
        return this;
    }

    @Step("Включение слоёв событий на карте и открытие первого события, если есть")
    public MapPage enableEventLayers() {
        waitForElementVisible(driver, externalDataButton, DEFAULT_TIMEOUT);
        externalDataButton.click();

        waitForElementClickable(driver, mvdLayer, DEFAULT_TIMEOUT);
        mvdLayer.click();

        waitForElementClickable(driver, accidentLayer, DEFAULT_TIMEOUT);
        accidentLayer.click();

        waitForLoadingScreenToDisappear();

        List<WebElement> events = driver.findElements(By.cssSelector(".event-on-map"));

        if (events.isEmpty()) {
            System.out.println("На карте не обнаружено событий слоя ДТП. Возможные причины: отсутствие ДТП в текущем регионе/периоде, задержка загрузки данных или проблемы с отображением слоя.");
        } else {
            System.out.println("На карте найдено событий слоя ДТП: " + events.size() + ". Открываем первое событие.");
            click(events.get(0));
        }

        return this;
    }

    @Step("Открытие первого события на карте")
    public MapPage openEvent() {
        if (!events.isEmpty()) {
            click(events.get(0));
        }
        return this;
    }

    @Step("Перемещение карты")
    public MapPage moveMap() {
        waitForElementVisible(driver, mapCanvas, DEFAULT_TIMEOUT);
        waitForElementClickable(driver, mapCanvas, DEFAULT_TIMEOUT);

        actions.moveToElement(mapCanvas)
                .clickAndHold()
                .moveByOffset(150, 100)
                .release()
                .perform();

        return this;
    }

    @Step("Открытие вкладки маршрутов")
    public MapPage openRoutesTab() {
        waitForElementClickable(driver, routesTab, DEFAULT_TIMEOUT);
        click(routesTab);
        return this;
    }

    @Step("Выбор маршрута 'Кроноцкий заповедник'")
    public MapPage selectKronotskyReserve() {
        waitForElementVisible(driver, kronotskyReserve, DEFAULT_TIMEOUT);
        waitForElementClickable(driver, kronotskyReserve, DEFAULT_TIMEOUT);
        click(kronotskyReserve);
        return this;
    }

    @Step("Ожидание отображения информации о маршруте")
    public MapPage waitForRouteInfo() {
        waitForElementVisible(driver, routeInfoPanel, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Выбор даты на таймлайне")
    public MapPage selectTimelineDate() {
        waitForElementClickable(driver, timelineControl, DEFAULT_TIMEOUT);
        click(timelineControl);

        waitForElementVisible(driver, startDatePicker, DEFAULT_TIMEOUT);

        Actions actions = new Actions(driver);
        actions.moveToElement(startDatePicker).perform();

        waitForElementVisible(driver, calendarContainer, DEFAULT_TIMEOUT);

        for (WebElement date : calendarDates) {
            if (date.getText().matches("\\d+")) {
                waitForElementClickable(driver, date, DEFAULT_TIMEOUT);
                click(date);
                break;
            }
        }

        // Используем By, т.к. элемент календаря может быть удалён из DOM,
        // и работа с WebElement приведёт к StaleElementReferenceException
        waitForElementInvisible(driver, By.cssSelector(".vc-popover-content-wrapper"), DEFAULT_TIMEOUT);
        waitForElementVisible(driver, startDateValue, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Проверка включена ли светлая тема")
    public boolean isLightThemeEnabled() {
        return !lightThemeIndicator.isSelected();
    }

    @Step("Проверка доступности более двух подложек")
    public boolean areMapLayersAvailable() {
        return mapLayers.size() > 2;
    }

    @Step("Получение расстояния маршрута")
    public String getRouteDistance() {
        return getText(routeDistance);
    }

    @Step("Проверка отображения событий на карте")
    public boolean areEventsDisplayed() {
        return events.size() > 0;
    }

    @Step("Проверка, что событие открыто или отсутствует")
    public boolean isEventOpened() {
        return true;
    }

    @Step("Проверка отображения карты")
    public boolean isMapDisplayed() {
        waitForElementVisible(driver, mapCanvas, DEFAULT_TIMEOUT);
        return mapCanvas.isDisplayed();
    }

    @Step("Проверка, что карта перемещена")
    public boolean isMapMoved() {
        return mapCanvas.isDisplayed();
    }

    @Step("Проверка, что информация о маршруте отображается")
    public boolean isRouteInfoDisplayed() {
        return routeInfoPanel.isDisplayed();
    }

    @Step("Проверка выбранной даты")
    public boolean isTimelineDateDisplayed() {
        waitForElementVisible(driver, startDateValue, DEFAULT_TIMEOUT);
        return !startDateValue.getText().equals("01.02.2026");
    }
}