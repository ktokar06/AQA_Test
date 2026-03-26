package org.example.listeners;

import io.qameta.allure.Allure;
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

        Object testClass = result.getInstance();
        if (testClass instanceof BaseTest baseTest) {
            WebDriver driver = baseTest.getDriver();
            if (driver != null) {
                try {
                    byte[] screenshot = ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.BYTES);
                    Allure.getLifecycle()
                            .addAttachment("Failure Screenshot", "image/png", "png", screenshot);
                } catch (Exception e) {
                    System.out.println("Ошибка при создании скриншота: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("SKIPPED: " + result.getName());
    }
}