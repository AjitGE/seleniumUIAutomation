package com.framework.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

    private ExtentManager() {
}

private static class GetExtentsReportInstance {
private static final ExtentReports INSTANCE = initializeReport();

private static ExtentReports initializeReport() {
    ExtentSparkReporter sparkReporter = new ExtentSparkReporter("test-output/ExtentReport.html");

    // Configure report appearance
    sparkReporter.config().setDocumentTitle("Automation Test Results");
    sparkReporter.config().setReportName("Test Execution Report");
    sparkReporter.config().setTimeStampFormat("EEEE, MMMM dd, yyyy, hh:mm a '('zzz')'");
    sparkReporter.config().setTheme(Theme.DARK);

    // Updated CSS for direct screenshot display
    // No JavaScript needed anymore since we're showing images directly

            ExtentReports extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // Add system info
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Browser", System.getProperty("browser", "chrome"));

            return extent;
        }
    }

    public static ExtentReports createInstance() {
        return GetExtentsReportInstance.INSTANCE;
    }
}