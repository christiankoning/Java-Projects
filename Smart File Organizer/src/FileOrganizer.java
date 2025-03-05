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

        for (File file : files) {
            if (file.isFile()) {
                String hash = getFileHash(file);

                if(hash != null) {
                    if(fileHashes.containsKey(hash)) {
                        System.out.println("Duplicate found: " + file.getName() + " (Duplicate of: " + fileHashes.get(hash) + ")");
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
}
