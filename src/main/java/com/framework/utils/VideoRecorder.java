package com.framework.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class VideoRecorder {
    private static Process ffmpegProcess;
    private static String currentRecordingPath;
    private static final String VIDEOS_DIR = System.getProperty("user.dir") + "/test-output/videos";
    private static final int FRAME_RATE = 15;
    private static final String VIDEO_QUALITY = "good"; // Possible values: realtime, good, best
    private static final int CRF = 30; // Constant Rate Factor (0-63), lower means better quality, 30 is a good balance

    public static void startRecording(String testName) {
        try {
            if (!isFFmpegInstalled()) {
                LoggerUtil.error("FFmpeg is not installed. Please install FFmpeg to enable video recording.", null);
                return;
            }

            // Create videos directory if it doesn't exist
            new File(VIDEOS_DIR).mkdirs();

            // Generate unique filename for this recording
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            currentRecordingPath = VIDEOS_DIR + "/" + testName + "_" + timestamp + ".webm";

            // FFmpeg command to record screen with WebM/VP8 encoding
            String[] command = {
                    "ffmpeg",
                    "-f", "avfoundation", // for macOS (use "gdigrab" for Windows)
                    "-i", "1:none", // screen input (might need adjustment based on system)
                    "-r", String.valueOf(FRAME_RATE),
                    "-c:v", "libvpx", // VP8 codec for WebM
                    "-quality", VIDEO_QUALITY,
                    "-cpu-used", "2", // CPU usage preset (0-5), higher means faster encoding
                    "-crf", String.valueOf(CRF),
                    "-deadline", "realtime",
                    "-auto-alt-ref", "0",
                    "-b:v", "1M", // Target bitrate
                    "-maxrate", "1M",
                    "-bufsize", "2M",
                    "-y", // overwrite output file
                    currentRecordingPath
            };

            // Start FFmpeg process with output redirection
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.redirectErrorStream(true);
            File logFile = new File(VIDEOS_DIR + "/ffmpeg.log");
            processBuilder.redirectOutput(logFile);
            ffmpegProcess = processBuilder.start();

            LoggerUtil.info("Started recording for test: " + testName);

        } catch (Exception e) {
            LoggerUtil.error("Failed to start recording", e);
        }
    }

    public static String stopRecording() {
        try {
            if (ffmpegProcess != null) {
                // Gracefully stop FFmpeg
                gracefullyStopFFmpeg();

                // Read the video file and convert to base64
                File videoFile = new File(currentRecordingPath);
                if (videoFile.exists()) {
                    byte[] videoBytes = Files.readAllBytes(videoFile.toPath());
                    String base64Video = Base64.getEncoder().encodeToString(videoBytes);

                    // Clean up
                    cleanupFiles(videoFile);

                    return base64Video;
                }
            }
        } catch (Exception e) {
            LoggerUtil.error("Failed to stop recording", e);
        } finally {
            cleanup();
        }
        return null;
    }

    private static void gracefullyStopFFmpeg() throws IOException, InterruptedException {
        // Send SIGTERM signal
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            Runtime.getRuntime().exec("taskkill /F /PID " + ffmpegProcess.pid());
        } else {
            // Send SIGINT (Ctrl+C) signal on Unix-like systems
            Process killProcess = Runtime.getRuntime().exec("kill -2 " + ffmpegProcess.pid());
            killProcess.waitFor();
        }

        // Wait for the process to terminate
        ffmpegProcess.waitFor();
    }

    private static void cleanupFiles(File videoFile) {
        try {
            // Delete the video file
            if (videoFile.exists()) {
                Files.delete(videoFile.toPath());
            }

            // Delete the log file
            File logFile = new File(VIDEOS_DIR + "/ffmpeg.log");
            if (logFile.exists()) {
                Files.delete(logFile.toPath());
            }
        } catch (IOException e) {
            LoggerUtil.warn("Failed to cleanup some files: " + e.getMessage());
        }
    }

    private static void cleanup() {
        ffmpegProcess = null;
        currentRecordingPath = null;
    }

    private static boolean isFFmpegInstalled() {
        try {
            Process process = Runtime.getRuntime().exec("ffmpeg -version");
            return process.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}