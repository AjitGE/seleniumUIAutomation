package com.framework.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtil {
    private final WebDriverWait wait;
    private static final int DEFAULT_TIMEOUT = 30;
    private static final int DEFAULT_POLLING = 500; // milliseconds

    public WaitUtil(WebDriver driver) {
        this.wait = new WebDriverWait(driver,
                Duration.ofSeconds(DEFAULT_TIMEOUT),
                Duration.ofMillis(DEFAULT_POLLING));



    }

    public WebElement waitForElementClickable(By locator) {
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (TimeoutException e) {
            LoggerUtil.error("Element not clickable: " + locator, e);
            throw e;
        }
    }

    public WebElement waitForElementPresent(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            LoggerUtil.error("Element not present: " + locator, e);
            throw e;
        }
    }

    public WebElement waitForElementVisible(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            LoggerUtil.error("Element not visible: " + locator, e);
            throw e;
        }
    }

    public boolean waitForElementInvisible(By locator) {
        try {
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            LoggerUtil.error("Element still visible: " + locator, e);
            throw e;
        }
    }

    public <T> T waitFor(ExpectedCondition<T> condition) {
        try {
            return wait.until(condition);
        } catch (TimeoutException e) {
            LoggerUtil.error("Condition failed: " + condition.toString(), e);
            throw e;
        }
    }

    public void retryingClick(By locator) {
        int attempts = 0;
        while (attempts < 20) {
            try {
                WebElement element = waitForElementClickable(locator);
                element.click();
                return;
            } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
                attempts++;
                if (attempts == 20) {
                    LoggerUtil.error("Failed to click element after 20 attempts: " + locator, e);
                }
                try {
                    Thread.sleep(DEFAULT_POLLING);
                } catch (InterruptedException ignored) {
                }
            }
        }
    }
}