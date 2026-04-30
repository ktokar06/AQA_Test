package ru.mchs.atlas.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        click(startDatePicker);

        WaitUtils.waitForCondition(driver, d -> {
            try {
                WebElement popover = d.findElement(By.cssSelector(".vc-popover-content-wrapper"));
                return popover.isDisplayed() && !popover.getText().isEmpty();
            } catch (Exception e) {
                return false;
            }
        }, DEFAULT_TIMEOUT);

        WebElement calendarContainer = driver.findElement(By.cssSelector(".vc-popover-content-wrapper"));

        boolean dateSelected = false;
        int attempts = 0;

        while (!dateSelected && attempts < 3) {
            try {
                List<WebElement> availableDates = calendarContainer.findElements(By.xpath(".//*[text() and string-length(text()) > 0]"));

                for (WebElement date : availableDates) {
                    String text = date.getText().trim();
                    if (text.matches("\\d+") && !text.isEmpty()) {
                        WaitUtils.waitForElementClickable(driver, date, DEFAULT_TIMEOUT);
                        click(date);
                        dateSelected = true;
                        break;
                    }
                }
            } catch (org.openqa.selenium.StaleElementReferenceException e) {
                attempts++;
                calendarContainer = driver.findElement(By.cssSelector(".vc-popover-content-wrapper"));
            }
        }

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