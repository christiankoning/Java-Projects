import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get folder path from user
        System.out.println("Enter the folder path: ");
        String folderPath = scanner.nextLine();

        // Process file organization
        organizeFilesByType(folderPath);

        scanner.close();
    }

    private static void organizeFilesByType(String folderPath) {
        File folder = new File(folderPath);

        // Validate if folder exists
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Error: The provided path is not a valid directory.");
            return;
        }

        File[] files = folder.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("The folder is empty.");
            return;
        }

        System.out.println("\nOrganizing files in: " + folderPath);

        for (File file : files) {
            if (file.isFile()) {
                String category = getCategoryForFile(file);
                if (category != null) {
                    moveFileToCategory(file, folderPath, category);
                }
            }
        }
    }

    private static String getCategoryForFile(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");

        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return null;
        }

        String extension = fileName.substring(dotIndex + 1).toLowerCase();
        return Categories.getCategory(extension);
    }

    private static void moveFileToCategory(File file, String baseFolderPath, String category) {
        File categoryFolder = new File(baseFolderPath, category);

        if (!categoryFolder.exists()) {
            categoryFolder.mkdir();
        }

        Path sourcePath = file.toPath();
        Path destinationPath = new File(categoryFolder, file.getName()).toPath();

        try {
            Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Moved: " + file.getName() + " → " + category);
        } catch (IOException e) {
            System.out.println("Failed to move: " + file.getName() + " (" + e.getMessage() + ")");
        }
    }
}

class Categories {
    private static final Map<String, String> FILE_CATEGORIES = new HashMap<>();

    // Static block to initialize categories
    static {
        FILE_CATEGORIES.put("jpg", "Images");
        FILE_CATEGORIES.put("png", "Images");
        FILE_CATEGORIES.put("gif", "Images");
        FILE_CATEGORIES.put("bmp", "Images");
        FILE_CATEGORIES.put("svg", "Images");

        FILE_CATEGORIES.put("pdf", "Documents");
        FILE_CATEGORIES.put("docx", "Documents");
        FILE_CATEGORIES.put("txt", "Documents");
        FILE_CATEGORIES.put("xlsx", "Documents");
        FILE_CATEGORIES.put("pptx", "Documents");

        FILE_CATEGORIES.put("mp4", "Videos");
        FILE_CATEGORIES.put("avi", "Videos");
        FILE_CATEGORIES.put("mov", "Videos");
        FILE_CATEGORIES.put("mkv", "Videos");

        FILE_CATEGORIES.put("mp3", "Music");
        FILE_CATEGORIES.put("wav", "Music");
        FILE_CATEGORIES.put("aac", "Music");
        FILE_CATEGORIES.put("flac", "Music");

        FILE_CATEGORIES.put("zip", "Archives");
        FILE_CATEGORIES.put("rar", "Archives");
        FILE_CATEGORIES.put("tar", "Archives");
        FILE_CATEGORIES.put("gz", "Archives");
        FILE_CATEGORIES.put("7z", "Archives");
    }

    public static String getCategory(String extension) {
        return FILE_CATEGORIES.getOrDefault(extension.toLowerCase(), null);
    }
}
