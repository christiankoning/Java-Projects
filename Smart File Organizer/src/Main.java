import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Get folder path from user
        System.out.println("Enter the folder path: ");
        String folderPath = scanner.nextLine();

        FileOrganizer organizer = new FileOrganizer(folderPath);

        while (true) {
            // Ask the user for sorting method or undo option
            System.out.println("\nChoose an option:");
            System.out.println("1. Sort by file type");
            System.out.println("2. Sort by file date");
            System.out.println("3. Undo last action");
            System.out.println("4. Exit");
            System.out.print("Enter choice (1, 2, 3, or 4): ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                organizer.organizeFiles("type");
            } else if (choice == 2) {
                organizer.organizeFiles("date");
            } else if (choice == 3) {
                organizer.undoLastAction();
            } else if (choice == 4) {
                System.out.println("Exiting program...");
                break;
            } else {
                System.out.println("Invalid choice. Please enter a valid option.");
            }
        }

        scanner.close();
    }
}
