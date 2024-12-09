import adminOperatorManager.AdminOperatorManager;
import adminOperatorManager.AdminOperatorManagerImpl;
import filemanager.FileManager;
import rentalmanager.RentalManager;

import java.util.*;
import java.io.*;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class MenuManager {

    public static void displayMenu() {
        System.out.println("Welcome to the Rental Agreement Management System");
        System.out.println("1. Add Agreement");
        System.out.println("2. Update Agreement");
        System.out.println("3. Delete Agreement");
        System.out.println("4. Get One Agreement");
        System.out.println("5. Get All Agreements");
        System.out.println("6. Get Agreements By Attributes");
        System.out.println("7. Get Associated Entities");
        System.out.println("8. Exit");
    }

    public static void handleUserInput(RentalManager rentalManager, FileManager fileManager, HashMap<String, HashMap<String, ?>> dataMap) {
        AdminOperatorManager adminOperatorManager = new AdminOperatorManagerImpl();
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        do {
            try {
                System.out.print("\n> Enter your menu choice: ");
                choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        adminOperatorManager.handleAddAgreement(rentalManager, dataMap);
                        System.out.println("Agreement added successfully.");
                        break;

                    case 2:
                        adminOperatorManager.handleUpdateAgreement(rentalManager, dataMap);
                        System.out.println("Agreement updated successfully.");
                        break;

                    case 3:
                        adminOperatorManager.handleDeleteAgreement(rentalManager, dataMap);
                        System.out.println("Agreement deleted successfully.");
                        break;

                    case 4:
                        adminOperatorManager.handleGetOne(rentalManager, dataMap);
                        break;

                    case 5:
                        adminOperatorManager.handleGetAll(rentalManager, dataMap);
                        break;

                    case 6:
                        adminOperatorManager.handleGetBy(rentalManager, dataMap);
                        try {
                            fileManager.saveFile(dataMap);
                            // System.out.println("All data deleted and saved successfully.");
                        } catch (IOException e) {
                            // System.err.println("Error saving data after deletion: " + e.getMessage());
                        }
                        break;

                    case 7:
                        adminOperatorManager.handleGetAssociatedEntities(dataMap);
                        break;

                    case 8:
                        System.out.println("Exiting the system. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }

                if (choice >= 1 && choice <= 7) {
                    try {
                        fileManager.saveFile(dataMap);
                    } catch (IOException e) {
                        System.err.println("Error saving data: " + e.getMessage());
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine();
            }
        } while (choice != 8);
    }

}
