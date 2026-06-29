package com.lab03.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        if (driver == null) {
            return null;
        }
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        
        // Save screenshot to target directory
        String destinationPath = System.getProperty("user.dir") + "/reports/screenshots/" + screenshotName + "_" + dateName + ".png";
        File finalDestination = new File(destinationPath);
        
        // Ensure parent directories exist
        File parentDir = finalDestination.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        
        try {
            Files.copy(source.toPath(), finalDestination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            // Return relative path for report embedding
            return "screenshots/" + screenshotName + "_" + dateName + ".png";
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }
}
