package com.framework.utils;

import com.framework.base.BaseTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {
    private static final String SCREENSHOT_DIR = "test-output/screenshots/";




    public static String getBase64Screenshot() {
        WebDriver driver = BaseTest.getDriver();
        TakesScreenshot ts = (TakesScreenshot) driver;
        return ts.getScreenshotAs(OutputType.BASE64);
    }

    /**
     * Takes screenshot and returns both file path and base64 string
     * 
     * @param testName Name of the test method
     * @return Object array containing [filePath, base64String]
     */
    public static void captureScreenshot(String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String screenshotDir = createScreenshotDirectory(timestamp);
        String fileName = String.format("%s_%s.png", testName, timestamp);
        String filePath = screenshotDir + fileName;
        WebDriver driver = BaseTest.getDriver();

        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            File destination = new File(filePath);
            FileUtils.copyFile(source, destination);

            // Get base64 string for reporting



        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * Creates a directory structure for screenshots
     * Format: test-output/screenshots/YYYYMMDD_HHmmss/
     */
    private static String createScreenshotDirectory(String timestamp) {
        String datePath = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String dirPath = SCREENSHOT_DIR + datePath + "/" + timestamp + "/";
        File directory = new File(dirPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        return dirPath;
    }

    /**
     * Cleans up old screenshot directories
     * Keeps only last 5 days of screenshots
     */
    public static void cleanupOldScreenshots() {
        File screenshotDir = new File(SCREENSHOT_DIR);
        if (!screenshotDir.exists())
            return;

        File[] dateDirs = screenshotDir.listFiles();
        if (dateDirs == null || dateDirs.length <= 5)
            return;

        // Sort directories by name (which is date)
        java.util.Arrays.sort(dateDirs);

        // Delete all but the last 5 directories
        for (int i = 0; i < dateDirs.length - 5; i++) {
            try {
                FileUtils.deleteDirectory(dateDirs[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}