package ru.mchs.atlas.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.mchs.atlas.utils.FileUtils;
import ru.mchs.atlas.utils.WaitUtils;

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
        WaitUtils.waitForElementVisible(driver, mapCanvas, DEFAULT_TIMEOUT);
        actions.contextClick(mapCanvas).perform();
        WaitUtils.waitForElementVisible(driver, contextMenu, DEFAULT_TIMEOUT);
        return this;
    }

    /**
     * Выбор опции 'Импорт области' и загрузка файла.
     * <p>
     * <b>КОСТЫЛЬ:</b> Selenium не умеет взаимодействовать с нативным диалогом выбора файла ОС.
     * Путь к файлу отправляется напрямую в скрытый {@code <input type="file">}
     *
     * @param fileName имя файла в resources
     * @return текущая страница ContextMenuPage
     */
    @Step("Выбор опции 'Импорт области' и загрузка файла")
    public ContextMenuPage selectImportAreaAndUploadFile(String fileName) {
        click(importAreaOption);

        String filePath = FileUtils.getFilePathFromResources(fileName);

        WebElement input = WaitUtils.waitForPresenceOfElement(driver, By.cssSelector("input[type='file']"), DEFAULT_TIMEOUT);
        input.sendKeys(filePath);

        WaitUtils.waitForElementVisible(driver, areaValue, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Копирование координат из контекстного меню")
    public String copyCoordinates() {
        click(copyCoordinatesOption);
        WaitUtils.waitForElementVisible(driver, coordinatesDisplay, DEFAULT_TIMEOUT);
        return getText(coordinatesDisplay);
    }

    @Step("Закрытие модального окна выбора слоев")
    public ContextMenuPage closeChooseLayersModal() {
        WaitUtils.waitForElementVisible(driver, chooseLayersModal, DEFAULT_TIMEOUT);
        click(cancelButton);
        WaitUtils.waitForElementVisible(driver, areaValue, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Ожидание исчезновения экрана загрузки")
    public ContextMenuPage waitForLoading() {
        waitForLoadingScreenToDisappear();
        return this;
    }

    @Step("Проверка отображения опции 'Импорт области'")
    public boolean isImportAreaOptionDisplayed() {
        WaitUtils.waitForElementVisible(driver, importAreaOption, DEFAULT_TIMEOUT);
        return importAreaOption.isDisplayed();
    }

    @Step("Получение значения площади")
    public String getAreaValue() {
        return getText(areaValue);
    }
}