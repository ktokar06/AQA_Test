package ru.mchs.atlas.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.mchs.atlas.utils.WaitUtils;

import java.util.List;

public class MapLayersPage extends BasePage {
    @FindBy(css = "#theme")
    private WebElement lightThemeIndicator;

    @FindBy(css = ".theme-control label")
    private WebElement themeToggle;

    @FindBy(css = ".tiles-component__button")
    private WebElement mapLayerButton;

    @FindBy(css = ".tiles-component .dropdown-menu__list .dropdown-menu__item")
    private List<WebElement> mapLayers;

    public MapLayersPage(WebDriver driver) {
        super(driver);
    }

    @Step("Переключение темы")
    public MapLayersPage switchTheme() {
        WaitUtils.waitForElementVisible(driver, themeToggle, DEFAULT_TIMEOUT);
        click(themeToggle);
        return this;
    }

    @Step("Открытие списка картографических подложек")
    public MapLayersPage openMapLayersList() {
        click(mapLayerButton);
        WaitUtils.waitForAllElementsVisible(driver, mapLayers, DEFAULT_TIMEOUT);
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

    @Step("Ожидание исчезновения экрана загрузки")
    public MapLayersPage waitForLoading() {
        waitForLoadingScreenToDisappear();
        return this;
    }
}