import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get folder path from user
        System.out.println("Enter the folder path: ");
        String folderPath = scanner.nextLine();

        // Process folder listing
        listFilesInDirectory(folderPath);

        scanner.close();
    }

    private static void listFilesInDirectory(String folderPath) {
        File folder = new File(folderPath);

        // Check if folder exists and is a directory
        if(!folder.exists() || !folder.isDirectory()) {
            System.out.println("Error: The provided path is not a valid directory.");
            return;
        }

        // List all files and subdirectories
        File[] files = folder.listFiles();

        if(files == null || files.length == 0) {
            System.out.println("The folder is empty");
        } else {
            System.out.println("\nFiles and Folder in: " + folderPath);
            for(File file : files) {
                if(file.isFile()) {
                    System.out.println("[File] " + file.getName());
                } else if(file.isDirectory()) {
                    System.out.println("[Dir] " + file.getName());
                }
            }
        }
    }
}
