package com.framework.pages;

import com.framework.base.BaseTest;
import com.framework.listeners.WebEventListener;
import com.framework.utils.DriverUtils;
import com.framework.utils.ParameterUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UselessFileDetector;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.framework.utils.WaitUtil;

import java.io.File;
import java.nio.file.Files;

public class FileUploadPage extends BasePage {
    private final By fileInput = By.id("file-upload");
    private final By uploadButton = By.id("file-submit");
    private final By uploadedFiles = By.id("uploaded-files");

    public FileUploadPage(WebDriver driver) {
        super(driver);
    }

    public void uploadFile(String filePath) {
        DriverUtils.unWrapDriver(new LocalFileDetector() );
        WebElement upload = wait.until(ExpectedConditions.presenceOfElementLocated(fileInput));
//        File file = new File(filePath);
        upload.sendKeys(filePath);
    }




    public void submitUpload() {
        wait.until(ExpectedConditions.elementToBeClickable(uploadButton)).click();
        DriverUtils.unWrapDriver(new UselessFileDetector());

    }

    public String getUploadedFileName() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(uploadedFiles)).getText();
    }
}
