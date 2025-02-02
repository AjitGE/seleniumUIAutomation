package com.framework.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.framework.base.BaseTest;
import com.framework.utils.ExtentManager;
import com.framework.utils.ScreenshotUtil;
import com.framework.utils.LoggerUtil;
import com.framework.utils.VideoRecorder;
import com.framework.utils.ParameterUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {
    private static final ExtentReports extent = ExtentManager.createInstance();
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final ThreadLocal<ExtentTest> testClass = new ThreadLocal<>();


    @Override
    public void onStart(ITestContext context) {
        // Load all TestNG parameters to System properties
        ParameterUtil.loadParameters(context);

        // Print all parameters for debugging
        ParameterUtil.printAllParameters();

        String testClassName = context.getCurrentXmlTest().getClasses().get(0).getName();
        String simpleClassName = testClassName.substring(testClassName.lastIndexOf('.') + 1);
        ExtentTest classTest = extent.createTest(simpleClassName);
        testClass.set(classTest);

        VideoRecorder.startRecording(simpleClassName);
    }

    @Override
    public void onTestStart(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();

        ExtentTest extentTest = testClass.get().createNode(methodName, description);
        test.set(extentTest);
        LoggerUtil.setTest(extentTest);

        test.get().info(MarkupHelper.createLabel("TEST STARTED: " + methodName, ExtentColor.BLUE));

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().pass(MarkupHelper.createLabel("Test Passed", ExtentColor.GREEN));
        addScreenshot("Test Pass Screenshot", true);

            addLocalScreenshot(result);


    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.get().fail(MarkupHelper.createLabel("Test Failed", ExtentColor.RED));
        test.get().fail(result.getThrowable());
        addScreenshot("Test failure screenshot", false);


            addLocalScreenshot(result);


    }

    @Override
    public void onTestSkipped(ITestResult result) {
        test.get().skip(MarkupHelper.createLabel("Test Skipped", ExtentColor.YELLOW));
        test.get().skip(result.getThrowable());
        addScreenshot("Test skipped Screenshot", false);
        addLocalScreenshot(result);


    }

    private void addLocalScreenshot(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ScreenshotUtil.captureScreenshot(testName);
    }

    private void addScreenshot(String title, boolean isSuccess) {
        try {
            String base64Screenshot = ScreenshotUtil.getBase64Screenshot();
            String htmlContent = "<div class='screenshot-wrapper'>"
                    + "<div class='screenshot-title'>" + title + "</div>"
                    + "<div class='screenshot-container'>"
                    + "<img class='screenshot-image' src='data:image/png;base64," + base64Screenshot + "'/>"
                    + "</div></div>";
            if (isSuccess) {
                test.get().pass(htmlContent);
            } else {
                test.get().info(htmlContent);
            }
        } catch (Exception e) {
            test.get().info("Could not capture screenshot: " + e.getMessage());
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            String base64Video = VideoRecorder.stopRecording();

            if (base64Video != null) {
                ExtentTest videoNode = testClass.get().createNode("Test Recording");
                test.set(videoNode);

                // Create HTML video player with base64 video
                String videoHtml = "<div class='video-container'>" +
                        "<video width='800' height='600' controls autoplay muted>" +
                        "<source src='data:video/webm;base64," + base64Video + "' type='video/webm'>" +
                        "Your browser does not support the video tag." +
                        "</video></div>";

                // Add custom CSS for video container with improved styling
                String css = "<style>" +
                        ".video-container { " +
                        "    display: flex; " +
                        "    justify-content: center; " +
                        "    background: #f8f9fa; " +
                        "    padding: 20px; " +
                        "    border-radius: 8px; " +
                        "    box-shadow: 0 2px 5px rgba(0,0,0,0.1); " +
                        "}" +
                        "video { " +
                        "    max-width: 100%; " +
                        "    border-radius: 4px; " +
                        "    box-shadow: 0 4px 8px rgba(0,0,0,0.1); " +
                        "}" +
                        "video::-webkit-media-controls { " +
                        "    background-color: rgba(0,0,0,0.1); " +
                        "}" +
                        "</style>";

                test.get().info("Test Execution Video")
                        .info(css + videoHtml);
            }
        } catch (Exception e) {
            LoggerUtil.error("Failed to attach video recording", e);
        } finally {
            extent.flush();
            test.remove();
            testClass.remove();
            ParameterUtil.cleanup(); // Clean up parameter storage
            ScreenshotUtil.cleanupOldScreenshots();
        }
    }


}