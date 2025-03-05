import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get folder path from user
        System.out.println("Enter the folder path: ");
        String folderPath = scanner.nextLine();

        // Ask the user for sorting method
        System.out.println("Choose sorting method:");
        System.out.println("1. Sort by file type");
        System.out.println("2. Sort by file date");
        System.out.print("Enter choice (1 or 2): ");
        int choice = scanner.nextInt();

        FileOrganizer organizer = new FileOrganizer(folderPath);

        if (choice == 1) {
            organizer.organizeFiles("type");
        } else if (choice == 2) {
            organizer.organizeFiles("date");
        } else {
            System.out.println("Invalid choice. Exiting...");
        }

        scanner.close();
    }
}
