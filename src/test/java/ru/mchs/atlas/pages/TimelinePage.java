package ru.mchs.atlas.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

import ru.mchs.atlas.utils.WaitUtils;

import java.util.List;

public class TimelinePage extends BasePage {
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

    public TimelinePage(WebDriver driver) {
        super(driver);
    }

    @Step("Выбор даты на таймлайне")
    public TimelinePage selectTimelineDate() {
        WaitUtils.waitForElementClickable(driver, timelineControl, DEFAULT_TIMEOUT);
        click(timelineControl);

        WaitUtils.waitForElementVisible(driver, startDatePicker, DEFAULT_TIMEOUT);

        Actions actions = new Actions(driver);
        actions.moveToElement(startDatePicker).perform();

        WaitUtils.waitForElementVisible(driver, calendarContainer, DEFAULT_TIMEOUT);

        for (WebElement date : calendarDates) {
            if (date.getText().matches("\\d+")) {
                WaitUtils.waitForElementClickable(driver, date, DEFAULT_TIMEOUT);
                click(date);
                break;
            }
        }

        WaitUtils.waitForElementInvisible(driver, By.cssSelector(".vc-popover-content-wrapper"), DEFAULT_TIMEOUT);
        WaitUtils.waitForElementVisible(driver, startDateValue, DEFAULT_TIMEOUT);
        return this;
    }

    @Step("Проверка выбранной даты")
    public boolean isTimelineDateDisplayed() {
        WaitUtils.waitForElementVisible(driver, startDateValue, DEFAULT_TIMEOUT);
        return !startDateValue.getText().equals("01.02.2026");
    }

    @Step("Ожидание исчезновения экрана загрузки")
    public TimelinePage waitForLoading() {
        waitForLoadingScreenToDisappear();
        return this;
    }
}