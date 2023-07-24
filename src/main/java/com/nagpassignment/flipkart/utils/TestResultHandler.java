package com.nagpassignment.flipkart.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestResultHandler {
    private static final String CURRENT_RESULTS_FOLDER = "Current test results";
    private static final String ARCHIVED_RESULTS_FOLDER = "Archived test results";

    public static void moveTestResults() {
        // Generate timestamp-based folder name
        String timestamp = generateTimestampFolderName();

        // Create the "Current test results" folder
        String currentFolderName = CURRENT_RESULTS_FOLDER + "_" + timestamp;
        File currentFolder = createFolder(currentFolderName);
        
        // Move the Extent Report to the "Current test results" folder
        moveFile("C:\\Users\\deepaksharma15\\eclipse-workspace\\mavenproject\\src\\main\\java\\com\\nagpassignment\\flipkart\\reporting\\ExtentReport.html", currentFolder.getAbsolutePath());
        
        // Move error screenshots to the "Current test results" folder
        moveFiles("C:\\Users\\deepaksharma15\\eclipse-workspace\\mavenproject\\src\\main\\java\\com\\nagpassignment\\flipkart\\reporting\\screenshots\\screenshot.png", currentFolder.getAbsolutePath());

        // Create the "Archived test results" folder
        File archivedFolder = createFolder(ARCHIVED_RESULTS_FOLDER);
        
        // Move the "Current test results" folder to the "Archived test results" folder
        moveFolder(currentFolder, archivedFolder);

        
    }

    private static String generateTimestampFolderName() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return now.format(formatter);
    }

    private static File createFolder(String folderName) {
        File folder = new File(folderName);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }

    private static void moveFile(String sourcePath, String destinationPath) {
        try {
            Path source = Paths.get(sourcePath);
            Path destination = Paths.get(destinationPath, source.getFileName().toString());
            Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            // Handle the exception
        }
    }

    private static void moveFiles(String sourceFolderPath, String destinationFolderPath) {
        try {
            File sourceFolder = new File(sourceFolderPath);
            File destinationFolder = new File(destinationFolderPath);
            File[] files = sourceFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    Path source = Paths.get(file.getAbsolutePath());
                    Path destination = Paths.get(destinationFolder.getAbsolutePath(), file.getName());
                    Files.move(source, destination, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (Exception e) {
            // Handle the exception
        }
    }

    private static void moveFolder(File sourceFolder, File destinationFolder) {
        try {
            Files.move(sourceFolder.toPath(), destinationFolder.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            // Handle the exception
        }
    }

    private static void cleanFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
    }
}

