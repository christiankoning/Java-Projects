import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileOrganizer {
    private final String folderPath;
    private final Map<String, String> fileHashes = new HashMap<>();

    public FileOrganizer(String folderPath) {
        this.folderPath = folderPath;
    }

    public void organizeFiles(String method) {
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

        System.out.println("\nOrganizing files by " + method + " in: " + folderPath);
        Scanner scanner = new Scanner(System.in);

        for (File file : files) {
            if (file.isFile()) {
                String hash = getFileHash(file);

                if(hash != null) {
                    if(fileHashes.containsKey(hash)) {
                        String existingFilePath = fileHashes.get(hash);
                        System.out.println("Duplicate found: " + file.getName() + " (Duplicate of: " + fileHashes.get(hash) + ")");
                        handleDuplicate(file, scanner);
                    } else {
                        fileHashes.put(hash, file.getAbsolutePath());
                        String category = getCategory(file, method);
                        if (category != null) {
                            moveFile(file, category);
                        }
                    }
                }
            }
        }
    }

    private String getCategory(File file, String method) {
        if ("type".equals(method)) {
            return getFileTypeCategory(file);
        } else if ("date".equals(method)) {
            return getFileDateCategory(file);
        }
        return null;
    }

    private String getFileTypeCategory(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");

        if (dotIndex == -1 || dotIndex == fileName.length() - 1) {
            return null;
        }

        String extension = fileName.substring(dotIndex + 1).toLowerCase();
        return Categories.getCategory(extension);
    }

    private String getFileDateCategory(File file) {
        try {
            Path filePath = file.toPath();
            BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);

            FileTime fileTime = attr.lastModifiedTime();
            Date date = new Date(fileTime.toMillis());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            return dateFormat.format(date);
        } catch (IOException e) {
            System.out.println("Error retrieving date for: " + file.getName());
            return null;
        }
    }

    private void moveFile(File file, String category) {
        File categoryFolder = new File(folderPath, category);

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

    private String getFileHash(File file) {
        try(FileInputStream fis = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            byte[] hashBytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            for(byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch(IOException | NoSuchAlgorithmException e) {
            System.out.println("Error generating hash for file: " + file.getName());
            return null;
        }
    }

    private void handleDuplicate(File duplicate, Scanner scanner) {
        System.out.println("Choose an action:");
        System.out.println("1. Delete the duplicate");
        System.out.println("2. Rename the duplicate");
        System.out.println("3. Move to 'Duplicates' folder");
        System.out.print("Enter your choice (1, 2, or 3): ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                deleteFile(duplicate);
                break;
            case 2:
                renameFile(duplicate);
                break;
            case 3:
                moveFile(duplicate, "Duplicates");
                break;
            default:
                System.out.println("Invalid choice. Skipping duplicate...");
        }
    }

    private void deleteFile(File file) {
        if(file.delete()) {
            System.out.println("Deleted: " + file.getName());
        } else {
            System.out.println("Failed to delete: " + file.getName());
        }
    }

    private void renameFile(File file) {
        String fileName = file.getName();
        String newFileName = getNewFileName(file);
        File renamedFile = new File(file.getParent(), newFileName);

        if(file.renameTo(renamedFile)) {
            System.out.println("Renamed: " + fileName + " -> " + newFileName);
        } else {
            System.out.println("Failed to rename: " + fileName);
        }
    }

    private String getNewFileName(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        String baseName = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
        String extension = (dotIndex == -1) ? "" : fileName.substring(dotIndex);

        int count = 1;
        File newFile;
        do {
            newFile = new File(file.getParent(), baseName + " (" + count + ")" + extension);
            count ++;
        } while (newFile.exists());

        return newFile.getName();
    }
}
