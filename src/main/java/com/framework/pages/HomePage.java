package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.framework.utils.WaitUtil;

public class HomePage extends BasePage {
    private final By dynamicLoadingLink = By.linkText("Dynamic Loading");
    private final By dragAndDropLink = By.linkText("Drag and Drop");
    private final By formAuthenticationLink = By.linkText("Form Authentication");
    private final By checkboxesLink = By.linkText("Checkboxes");
    private final By keyPressesLink = By.linkText("Key Presses");
    private final By javascriptAlertsLink = By.linkText("JavaScript Alerts");
    private final By fileUploadLink = By.linkText("File Upload");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public DynamicLoadingPage navigateToDynamicLoading() {
        waitUtil.retryingClick(dynamicLoadingLink);
        return new DynamicLoadingPage(driver);
    }

    public DragAndDropPage navigateToDragAndDrop() {
        waitUtil.retryingClick(dragAndDropLink);
        return new DragAndDropPage(driver);
    }

    public LoginPage navigateToLogin() {
        waitUtil.retryingClick(formAuthenticationLink);
        return new LoginPage(driver);
    }

    public CheckboxesPage navigateToCheckboxes() {
        waitUtil.retryingClick(checkboxesLink);
        return new CheckboxesPage(driver);
    }

    public KeyPressesPage navigateToKeyPresses() {
        waitUtil.retryingClick(keyPressesLink);
        return new KeyPressesPage(driver);
    }

    public AlertsPage navigateToAlerts() {
        waitUtil.retryingClick(javascriptAlertsLink);
        return new AlertsPage(driver);
    }

    public FileUploadPage navigateToFileUpload() {
        waitUtil.retryingClick(fileUploadLink);
        return new FileUploadPage(driver);
    }
}