package org.example.listeners;

import io.qameta.allure.Attachment;
import org.example.tests.BaseTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("START: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("SUCCESS: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("FAILED: " + result.getName());
        attachScreenshot(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("SKIPPED: " + result.getName());
        attachScreenshot(result);
    }

    private void attachScreenshot(ITestResult result) {
        Object testClass = result.getInstance();
        if (testClass instanceof BaseTest baseTest) {
            WebDriver driver = baseTest.getDriver();
            if (driver != null) {
                takeScreenshot(driver);
            }
        }
    }

    @Attachment(value = "Скриншот", type = "image/png")
    public byte[] takeScreenshot(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            System.err.println("Не удалось сделать скриншот: " + e.getMessage());
            return new byte[0];
        }
    }
}