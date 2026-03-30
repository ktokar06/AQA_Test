package org.example.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.example.utils.FileUtils.getFilePathFromResources;
import static org.example.utils.WaitUtils.waitForElementVisible;
import static org.example.utils.WaitUtils.waitForPresenceOfElement;

public class ContextMenuPage extends BasePage {
    @FindBy(css = ".mapboxgl-canvas")
    private WebElement mapCanvas;

    @FindBy(css = ".coordinate")
    private WebElement coordinatesDisplay;

    @FindBy(css = ".measure-area__square")
    private WebElement areaValue;

    @FindBy(css = ".choose-layers-modal__container button.btn-secondary")
    private WebElement cancelButton;

    @FindBy(css = ".choose-layers-modal__container")
    private WebElement chooseLayersModal;

    @FindBy(css = ".context-menu, [role='menu'], .dropdown-menu")
    private WebElement contextMenu;

    @FindBy(xpath = ".//button[contains(text(), 'Импорт области')]")
    private WebElement importAreaOption;

    @FindBy(xpath = ".//button[contains(text(), 'Скопировать координаты')]")
    private WebElement copyCoordinatesOption;

    public ContextMenuPage(WebDriver driver) {
        super(driver);
    }

    @Step("Открытие контекстного меню на карте")
    public ContextMenuPage openContextMenuOnMap() {
        waitForElementVisible(driver, mapCanvas, DEFAULT_TIMEOUT);

        actions.contextClick(mapCanvas).perform();

        waitForElementVisible(driver, contextMenu, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Проверка отображения опции 'Импорт области'")
    public boolean isImportAreaOptionDisplayed() {
        waitForElementVisible(driver, importAreaOption, DEFAULT_TIMEOUT);
        return importAreaOption.isDisplayed();
    }

    @Step("Выбор опции 'Импорт области' и загрузка файла")
    public ContextMenuPage selectImportAreaAndUploadFile(String fileName) {
        click(importAreaOption);

        String filePath = getFilePathFromResources(fileName);

        WebElement input = waitForPresenceOfElement(driver, By.cssSelector("input[type='file']"), DEFAULT_TIMEOUT);

        ((JavascriptExecutor) driver).executeScript("arguments[0].style.display = 'block';", input);
        input.sendKeys(filePath);

        waitForElementVisible(driver, areaValue, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Копирование координат из контекстного меню")
    public String copyCoordinates() {
        click(copyCoordinatesOption);
        waitForElementVisible(driver, coordinatesDisplay, DEFAULT_TIMEOUT);
        return getText(coordinatesDisplay);
    }

    @Step("Закрытие модального окна выбора слоев")
    public ContextMenuPage closeChooseLayersModal() {
        waitForElementVisible(driver, chooseLayersModal, DEFAULT_TIMEOUT);

        click(cancelButton);

        waitForElementVisible(driver, areaValue, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Получение значения площади")
    public String getAreaValue() {
        return getText(areaValue);
    }

    @Step("Ожидание исчезновения экрана загрузки и возврат страницы")
    public ContextMenuPage waitForLoading() {
        waitForLoadingScreenToDisappear();
        return this;
    }
}