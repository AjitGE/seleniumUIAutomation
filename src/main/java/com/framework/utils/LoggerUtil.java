package com.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import com.aventstack.extentreports.ExtentTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LoggerUtil {
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    private static Logger getLogger() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
//        System.out.println(Arrays.toString(stackTrace));

        // Filter out LoggerUtil and java.lang.Thread calls
        List<StackTraceElement> relevantTrace = Arrays.stream(stackTrace)
                .filter(element -> element.getClassName().contains("com.framework.pages"))
                .collect(Collectors.toList());

        // Get the first relevant calling class
        if (!relevantTrace.isEmpty()) {
            String callerClassName = relevantTrace.get(0).toString();
            return LogManager.getLogger(callerClassName);
        }

        // Fallback to a default logger if no relevant caller is found
        return LogManager.getLogger("DefaultLogger");
    }

    public static void startTestCase(String testCaseName) {
        ThreadContext.put("testCase", testCaseName);
        ThreadContext.put("executionId", UUID.randomUUID().toString());
        getLogger().info("========================= Test Case: {} Started =========================", testCaseName);
    }

    public static void endTestCase(String testCaseName) {
        getLogger().info("========================= Test Case: {} Ended =========================", testCaseName);
        ThreadContext.clearAll();
    }

    public static void setTest(ExtentTest test) {
        extentTest.set(test);
    }

    public static void info(String message) {
        getLogger().info(message);
        if (!message.contains("Element Details") && extentTest.get() != null) {
            extentTest.get().info(message);
        }
    }

    public static void debug(String message) {
        getLogger().debug(message);
    }

    public static void error(String message, Throwable throwable) {
        getLogger().error(message, throwable);
        if (extentTest.get() != null) {
            extentTest.get().fail(message + "\n" + throwable.getMessage());
        }
    }

    public static void warn(String message) {
        getLogger().warn(message);
        if (extentTest.get() != null) {
            extentTest.get().warning(message);
        }
    }

    public static void fatal(String message) {
        getLogger().fatal(message);
        if (extentTest.get() != null) {
            extentTest.get().fail(message);
        }
    }

    public static void removeTest() {
        extentTest.remove();
    }
}