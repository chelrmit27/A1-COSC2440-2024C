import adminOperatorManager.*;
import rentalmanager.RentalManager;
import domain.*;
import filemanager.*;
import utils.*;
import java.util.*;

public class MenuManager {

    public static void displayMenu() {
        System.out.println("Welcome to the Rental Agreement Management System");
        System.out.println("1. Add Agreement");
        System.out.println("2. Update Agreement");
        System.out.println("3. Delete Agreement");
        System.out.println("4. Get One Agreement");
        System.out.println("5. Get All Agreements");
        System.out.println("6. Exit");
    }

    public static void handleUserInput(RentalManager rentalManager, FileManager fileManager, HashMap<String, HashMap<String, ?>> dataMap) {
        AdminOperatorManager adminOperatorManager = new AdminOperatorManagerImpl();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.print("> Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();  // clear buffer

            switch (choice) {
                case 1:
                    // Add Agreement
                    adminOperatorManager.handleAddAgreement(rentalManager, dataMap);
                    System.out.println("Agreement added successfully.");
                    break;

                case 2:
                    // Update Agreement
                    adminOperatorManager.handleUpdateAgreement(rentalManager, dataMap);
                    System.out.println("Agreement updated successfully.");
                    break;

                case 3:
                    // Delete Agreement
                    adminOperatorManager.handleDeleteAgreement(rentalManager, dataMap);
                    System.out.println("Agreement deleted successfully.");
                    break;

                case 4:
                    // Get One Agreement
                    adminOperatorManager.handleGetOne(rentalManager, dataMap);
                    break;

                case 5:
                    // Get All Agreements
                    adminOperatorManager.handleGetAll(rentalManager, dataMap);
                    break;

                case 6:
                    // Exit
                    System.out.println("Exiting the system. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            // Save changes to the file after each operation
            if (choice >= 1 && choice <= 5) {
                try {
                    fileManager.saveFile(dataMap);
                } catch (Exception e) {
                    System.out.println("Error saving data: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } while (choice != 6);

        scanner.close();
    }
}
