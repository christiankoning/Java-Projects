import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class FileOrganizer {
    private final String folderPath;
    private final Map<String, String> fileHashes = new HashMap<>();
    private final File logFile;
    private final Map<Pattern, String> userRules = new HashMap<>();

    public FileOrganizer(String folderPath) {
        this.folderPath = folderPath;
        this.logFile = new File(folderPath, "logs.txt");
        loadUserRules();
    }

    private void loadUserRules() {
        File rulesFile = new File(folderPath, "rules.txt");
        if(!rulesFile.exists()) {
            System.out.println("No custom rules found. Skipping...");
            return;
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(rulesFile))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] parts = line.split("->");
                if(parts.length == 2) {
                    String regex = parts[0].trim();
                    String category = parts[1].trim();
                    userRules.put(Pattern.compile(regex), category);
                }
            }
            System.out.println("Loaded " + userRules.size() + " custom rules.");
        } catch (IOException e) {
            System.out.println("Error loading rules: " + e.getMessage());
        }
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
                        log("Duplicate found: " + file.getName() + " (Duplicate of: " + fileHashes.get(hash) + ")");
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

    public void undoLastAction() {
        if(!logFile.exists() || logFile.length() == 0) {
            System.out.println("No actions to undo.");
            return;
        }

        List<String> logLines = new ArrayList<>();
        String lastAction = null;

        // Read all log entries
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logLines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error reading log file: " + e.getMessage());
            return;
        }

        if(logLines.isEmpty()) {
            System.out.println("No actions to undo.");
            return;
        }

        lastAction = logLines.remove(logLines.size() - 1);
        System.out.println("Undoing last action: " + lastAction);

        // Parse the last action
        if(lastAction.contains("Moved:")) {
            undoMove(lastAction);
        } else if (lastAction.contains("Renamed:")) {
            undoRename(lastAction);
        } else if (lastAction.contains("Deleted:")) {
            undoDelete(lastAction);
        } else {
            System.out.println("Unable to recognize the last action.");
            return;
        }

        // Rewrite the log file without the last action
        try (FileWriter writer = new FileWriter(logFile, false)) {
            for (String log : logLines) {
                writer.write(log + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error updating log file: " + e.getMessage());
        }
    }

    private void undoMove(String logEntry) {
        // Extract file name and category from the log entry
        String[] parts = logEntry.split("->");
        if(parts.length != 2) return;

        String fileName = parts[0].replace("Moved: ", "").trim();
        String category = parts[1].trim();

        File movedFile = new File(folderPath + "/" + category, fileName);
        File originalFile = new File(folderPath, fileName);

        if(movedFile.exists()) {
            if(movedFile.renameTo(originalFile)) {
                System.out.println("Restored: " + fileName + " back to " + folderPath);
            } else {
                System.out.println("Failed to restore: " + fileName);
            }
        } else {
            System.out.println("File not found for undo: " + fileName);
        }
    }

    private void undoRename(String logEntry) {
        // Extract old and new file names from the log entry
        String[] parts = logEntry.split("->");
        if(parts.length != 2) return;

        String oldName = parts[0].trim();
        String newName = parts[1].trim();

        File renamedFile = new File(folderPath, newName);
        File originalFile = new File(folderPath, oldName);

        if(renamedFile.exists()) {
            if(renamedFile.renameTo(originalFile)) {
                System.out.println("Renamend back: " + newName + " -> " + oldName);
            } else {
                System.out.println("Failed to rename back: " + newName);
            }
        } else {
            System.out.println("File not found for undo: " + newName);
        }
    }

    private void undoDelete(String logEntry) {
        // Extract file name from the log entry
        String fileName = logEntry.replace("Deleted: ", "").trim();
        File deletedFile = new File(folderPath, fileName + ".bak");

        if(deletedFile.exists()) {
            File restoredFile = new File(folderPath, fileName);
            if(deletedFile.renameTo(restoredFile)) {
                System.out.println("Restored: " + fileName);
            } else {
                System.out.println("Failed to restore: " + fileName);
            }
        } else {
            System.out.println("No backup found for " + fileName + ", cannot restore.");
        }
    }

    private String getCategory(File file, String method) {
        // First check for user-defined rules
        for (Map.Entry<Pattern, String> rule : userRules.entrySet()) {
            if (rule.getKey().matcher(file.getName()).matches()) {
                return rule.getValue();
            }
        }

        // Fallback to default sorting methods
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
            log("Moved: " + file.getName() + " → " + category);
        } catch (IOException e) {
            log("Failed to move: " + file.getName() + " (" + e.getMessage() + ")");
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
        File backup = new File(file.getParent(), file.getName() + ".bak");
        if (file.renameTo(backup)) {
            log("Deleted: " + file.getName());
        } else {
            System.out.println("Failed to delete: " + file.getName());
        }
    }

    private void renameFile(File file) {
        String fileName = file.getName();
        String newFileName = getNewFileName(file);
        File renamedFile = new File(file.getParent(), newFileName);

        if(file.renameTo(renamedFile)) {
            log("Renamed: " + fileName + " -> " + newFileName);
        } else {
            log("Failed to rename: " + fileName);
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

    private void log(String message) {
        try(FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " - " + message + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to log file: " + e.getMessage());
        }
    }
}
