package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.framework.utils.WaitUtil;

public class DragAndDropPage extends BasePage {
    private final By columnA = By.id("column-a");
    private final By columnB = By.id("column-b");

    public DragAndDropPage(WebDriver driver) {
        super(driver);
    }

    public void dragAToB() {
        WebElement source = waitUtil.waitForElementPresent(columnA);
        WebElement target = waitUtil.waitForElementPresent(columnB);
        actions.dragAndDrop(source, target).perform();
    }

    public String getColumnAText() {
        return waitUtil.waitForElementVisible(columnA).getText();
    }
}