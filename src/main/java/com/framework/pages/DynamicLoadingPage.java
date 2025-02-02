package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.framework.utils.WaitUtil;

public class DynamicLoadingPage extends BasePage {
    private final By example2Link = By.linkText("Example 2: Element rendered after the fact");
    private final By startButton = By.cssSelector("#start button");
    private final By finishText = By.cssSelector("#finish h4");

    public DynamicLoadingPage(WebDriver driver) {
        super(driver);
    }

    public void clickExample2() {
        waitUtil.retryingClick(example2Link);
    }

    public void clickStart() {
        waitUtil.retryingClick(startButton);
    }

    public String getFinishText() {
        return waitUtil.waitForElementVisible(finishText).getText();
    }
}