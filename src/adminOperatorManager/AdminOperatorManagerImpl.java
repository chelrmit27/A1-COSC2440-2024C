package adminOperatorManager;

import filemanager.*;
import rentalmanager.*;
import utils.*;
import domain.*;
import sortingFeature.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class AdminOperatorManagerImpl implements AdminOperatorManager {
    private final RentalManager rentalManager = new RentalManagerImpl();
    private final FileManager fileManager = new FileManagerImpl();
    private final InputValidator validator = new InputValidator();
    private final ConsoleToFileSaver consoleSaver = new ConsoleToFileSaver();
    static Scanner scanner = new Scanner(System.in);
    static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Deletes all data from memory and storage after user confirmation.
     *
     * <p>This method prompts the user for confirmation and, if confirmed,
     * clears all data from the provided data map and invokes the file manager
     * to delete all data files from disk. If the user cancels, no changes are made.</p>
     *
     * @param dataMap a HashMap containing data collections to be cleared from memory.
     */
    @Override
    public void handleDeleteAllData(HashMap<String, HashMap<String, ?>> dataMap) {
        System.out.print("Are you sure you want to delete all data? This action cannot be undone. (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("yes")) {
            for (HashMap<String, ?> map : dataMap.values()) {
                map.clear();
            }

            fileManager.deleteAllDataFiles();
            System.out.println("All data has been deleted from both memory and storage.");
        } else {
            System.out.println("Deletion canceled.");
        }
    }

    /**
     * Handles the process of adding a new rental agreement.
     *
     * <p>This method collects input from the user to create a new rental agreement,
     * validates the data, updates related entities, and saves the updated data to disk.</p>
     *
     * @param rentalManager the RentalManager to manage rental agreements.
     * @param dataMap       a HashMap containing all data collections to be updated.
     */
    @Override
    public void handleAddAgreement(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap) {
        HashMap<String, RentalAgreement> rentalHashMap = (HashMap<String, RentalAgreement>) dataMap.get("rental_agreements");

        // Rental Agreement ID
        String agreementId;
        do {
            System.out.print("Enter new Rental Agreement ID (RA***): ");
            agreementId = scanner.nextLine().trim();
            if (rentalHashMap.containsKey(agreementId)) {
                System.out.println("WARNING: Rental Agreement ID already exists. Please enter a unique ID.");
            }
        } while (rentalHashMap.containsKey(agreementId));

        // Retrieve entities
        Property property = Utils.getPropertyById(dataMap);
        Tenant mainTenant = Utils.getTenantById(dataMap);
        List<Tenant> subTenants = Utils.getSubTenantsById(dataMap);

        // Other details for the agreement
        LeasePeriod leasePeriod = Utils.getEnumInput(LeasePeriod.class, "Lease Period (DAILY, WEEKLY, FORTNIGHTLY, MONTHLY, QUARTERLY, BIANNUALLY, ANNUALLY)");
        Date contractDate = Utils.getDateInput("Contract Date (dd/MM/yyyy)");
        double rentingFee = Utils.getPositiveDouble("Renting Fee");
        RentalStatus status = Utils.getEnumInput(RentalStatus.class, "Rental Status (NEW, ACTIVE, COMPLETED)");

        // Create the new RentalAgreement
        RentalAgreement newAgreement = new RentalAgreement(
                agreementId, property, mainTenant, subTenants, leasePeriod, contractDate, rentingFee, status);

        // Add the new agreement to rental manager and data map
        rentalManager.addAgreement(newAgreement);
        rentalHashMap.put(agreementId, newAgreement);

        // Update associated entities with the new rental agreement
        updateEntitiesForNewAgreement(dataMap, newAgreement);

        // Save all updates to file
        try {
            fileManager.saveFile(dataMap);
            System.out.println("Rental Agreement added successfully, and all data saved to file.");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    /**
     * Updates related entities with the new rental agreement.
     *
     * <p>This method ensures the new rental agreement is associated with the correct main tenant,
     * sub-tenants, and payment records, and updates the relevant data collections in the provided data map.</p>
     *
     * @param dataMap       a HashMap containing all data collections to be updated.
     * @param newAgreement  the newly created rental agreement to be associated with other entities.
     */
    private void updateEntitiesForNewAgreement(HashMap<String, HashMap<String, ?>> dataMap, RentalAgreement newAgreement) {
        // Update Main Tenant
        Tenant mainTenant = newAgreement.getMainTenant();
        if (mainTenant != null) {
            mainTenant.getRentalAgreements().add(newAgreement);
            ((HashMap<String, Tenant>) dataMap.get("tenants")).put(mainTenant.getId(), mainTenant);
        }

        // Update Sub-Tenants
        for (Tenant subTenant : newAgreement.getSubTenants()) {
            subTenant.getRentalAgreements().add(newAgreement);
            ((HashMap<String, Tenant>) dataMap.get("tenants")).put(subTenant.getId(), subTenant);
        }

        // Update Payment
        if (mainTenant != null) {
            HashMap<String, Payment> paymentHashMap = (HashMap<String, Payment>) dataMap.get("payments");

            Payment matchingPayment = null;

            for (Payment payment : paymentHashMap.values()) {
                boolean isAmountMatching = Math.abs(payment.getAmount() - newAgreement.getRentingFee()) < 0.01; // Allow small tolerance
                boolean isTenantMatching = payment.getMainTenant() == null || payment.getMainTenant().getId().equals(mainTenant.getId());
                boolean isAgreementUnlinked = payment.getRentalAgreement() == null;

                if (isAmountMatching && isTenantMatching && isAgreementUnlinked) {
                    matchingPayment = payment;
                    break;
                }
            }

            if (matchingPayment != null) {
                matchingPayment.setMainTenant(mainTenant);
                matchingPayment.setRentalAgreement(newAgreement);
                paymentHashMap.put(matchingPayment.getId(), matchingPayment); // Update the payment in the map
            }
        }
    }


    /**
     * Handles updating an existing rental agreement.
     *
     * <p>This method allows the user to select and update specific fields of an existing rental agreement,
     * including property, main tenant, sub-tenants, lease period, contract date, renting fee, and rental status.</p>
     *
     * <p>Once updates are made, the method ensures changes are saved to both the in-memory data map
     * and the persistent storage.</p>
     *
     * @param rentalManager the RentalManager instance managing rental agreements.
     * @param dataMap       a HashMap containing all data collections, including rental agreements, properties, and tenants.
     */
    @Override
    public void handleUpdateAgreement(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap) {
        String id;
        RentalAgreement agreementToUpdate = null;

        do {
            System.out.print("> Enter Rental Agreement ID (RA***): ");
            id = scanner.nextLine().trim();
            if (!validator.isValidRentalAgreementIDFormat(id)) {
                System.out.println("Invalid Rental Agreement ID. Try again.");
            } else {
                agreementToUpdate = rentalManager.getOne(id);
                if (agreementToUpdate == null) {
                    System.out.println("Rental Agreement does not exist. Try again.");
                }
            }
        } while (!validator.isValidRentalAgreementIDFormat(id) || agreementToUpdate == null);

        System.out.println("\nChoose the field to update:");
        System.out.println("1. Property");
        System.out.println("2. Main Tenant");
        System.out.println("3. Sub Tenants");
        System.out.println("4. Lease Period");
        System.out.println("5. Contract Date");
        System.out.println("6. Renting Fee");
        System.out.println("7. Rental Status");
        System.out.print("> Enter your choice: ");

        int choice = -1;

        do {
            try {
                System.out.print("Enter your choice (1-7): ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                if (choice < 1 || choice > 7) {
                    System.out.println("Invalid Choice. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                scanner.nextLine(); // Clear invalid input
            }
        } while (choice < 1 || choice > 7);


        switch (choice) {
            case 1:
                Property newProperty = Utils.getPropertyById(dataMap);
                if (newProperty != null) {
                    agreementToUpdate.setProperty(newProperty);
                }
                break;
            case 2:
                Tenant newMainTenant = Utils.getTenantById(dataMap);
                if (newMainTenant != null) {
                    agreementToUpdate.setMainTenant(newMainTenant);
                }
                break;
            case 3:
                List<Tenant> newSubTenants = Utils.getSubTenantsById(dataMap);
                agreementToUpdate.setSubTenants(newSubTenants);
                break;
            case 4:
                LeasePeriod newLeasePeriod = Utils.getEnumInput(LeasePeriod.class, "Lease Period");
                agreementToUpdate.setPeriod(newLeasePeriod);
                break;
            case 5:
                Date newContractDate = Utils.getDateInput("Contract Date (dd/MM/yyyy)");
                agreementToUpdate.setContractDate(newContractDate);
                break;
            case 6:
                double newRentingFee = Utils.getPositiveDouble("Renting Fee");
                agreementToUpdate.setRentingFee(newRentingFee);
                break;
            case 7:
                RentalStatus newStatus = Utils.getEnumInput(RentalStatus.class, "Rental Status");
                agreementToUpdate.setStatus(newStatus);
                break;
        }

        // Save changes
        try {
            fileManager.saveFile(dataMap);
            System.out.println("Rental agreement updated successfully and saved to file.");
        } catch (IOException e) {
            System.err.println("Error saving updated rental agreement to file.");
        }
    }

    /**
     * Handles the deletion of a rental agreement.
     *
     * <p>This method allows the user to delete a rental agreement by entering its ID.
     * It validates the ID and removes the agreement from the rental manager, updates associated main and sub-tenants,
     * and deletes the agreement from the in-memory data map.</p>
     *
     * <p>After performing the deletion, the method saves the updated data to persistent storage.</p>
     *
     * @param rentalManager the RentalManager instance managing rental agreements.
     * @param dataMap       a HashMap containing all data collections, including rental agreements, tenants, and others.
     */
    @Override
    public void handleDeleteAgreement(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap) {
        String id;
        RentalAgreement agreementToDelete = null;

        do {
            System.out.print("> Enter Rental Agreement ID (RA***): ");
            id = scanner.nextLine().trim();
            if (!validator.isValidRentalAgreementIDFormat(id)) {
                System.out.println("Invalid Rental Agreement ID. Try again.");
            } else {
                agreementToDelete = rentalManager.getOne(id);
                if (agreementToDelete == null) {
                    System.out.println("Rental Agreement does not exist. Try again.");
                }
            }
        } while (!validator.isValidRentalAgreementIDFormat(id) || agreementToDelete == null);

        System.out.println(agreementToDelete.getId());
        if (agreementToDelete != null) {
            // Remove agreement from rental manager
            rentalManager.deleteAgreement(id);

            Tenant mainTenant = agreementToDelete.getMainTenant();
            if (mainTenant != null) {
                mainTenant.getRentalAgreements().remove(agreementToDelete);
                ((HashMap<String, Tenant>) dataMap.get("tenants")).put(mainTenant.getId(), mainTenant);
            }

            // Update Sub-Tenants
            for (Tenant subTenant : agreementToDelete.getSubTenants()) {
                subTenant.getRentalAgreements().remove(agreementToDelete);
                ((HashMap<String, Tenant>) dataMap.get("tenants")).put(subTenant.getId(), subTenant);
            }

            // Remove rental agreement reference from associated payment
            HashMap<String, Payment> paymentHashMap = (HashMap<String, Payment>) dataMap.get("payments");
            for (Payment payment : paymentHashMap.values()) {
                if (payment.getRentalAgreement() != null && payment.getRentalAgreement().getId().equals(id)) {
                    payment.setRentalAgreement(null); // Unlink the rental agreement
                    paymentHashMap.put(payment.getId(), payment); // Update payment in the map
                }
            }

            // Remove agreement from data map
            ((HashMap<String, RentalAgreement>) dataMap.get("rental_agreements")).remove(id);

            // Save changes to file
            try {
                fileManager.saveFile(dataMap);
                System.out.println("Rental agreement deleted successfully and changes saved to file.");
            } catch (IOException e) {
                System.err.println("Error saving changes to file after deletion.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles retrieval and display of a specific rental agreement.
     *
     * <p>This method prompts the user to input a Rental Agreement ID, validates its format,
     * and checks if the agreement exists. If found, it displays the details of the agreement using
     * the `printAgreement()` method.</p>
     *
     * @param rentalManager the RentalManager instance managing rental agreements.
     * @param dataMap       a HashMap containing all data collections, including rental agreements, tenants, and others.
     */
    @Override
    public void handleGetOne(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap) {
        String id;
        RentalAgreement agreementToGet = null;

        do {
            System.out.print("> Enter Rental Agreement ID (RA***): ");
            id = scanner.nextLine().trim();
            if (!validator.isValidRentalAgreementIDFormat(id)) {
                System.out.println("Invalid Rental Agreement ID format. Try again.");
            } else {
                agreementToGet = rentalManager.getOne(id);
                if (agreementToGet == null) {
                    System.out.println("Rental Agreement does not exist. Try again.");
                }
            }
        } while (!validator.isValidRentalAgreementIDFormat(id) || agreementToGet == null);

        if (agreementToGet != null) {
            System.out.println("\t\tRental Agreement Details");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-18s %-12s %-15s %-30s %-12s %-15s %-15s %-10s%n",
                    "RentalAgreementID", "PropertyID", "MainTenantID", "SubTenantIDs", "LeasePeriod", "ContractDate", "RentingFee", "Status");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");

            agreementToGet.printAgreement(); // Call the printAgreement() method to print the agreement details.

            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
        }
        List<Object> finalResults = new ArrayList<>();
        finalResults.add(agreementToGet);
        consoleSaver.saveReportToFile(finalResults);
    }

    /**
     * Handles retrieval and display of all rental agreements with sorting options.
     *
     * <p>This method retrieves all rental agreements from the RentalManager and displays them.
     * The user can choose a sorting parameter (e.g., Lease Period, Contract Date, Renting Fee, Status, or ID)
     * to organize the agreements before display. If no sorting choice is valid, agreements are displayed without sorting.</p>
     *
     * @param rentalManager the RentalManager instance managing rental agreements.
     * @param dataMap       a HashMap containing all data collections, including rental agreements, tenants, and others.
     */
    @Override
    public void handleGetAll(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap) {
        List<RentalAgreement> allAgreements = rentalManager.getAll();

        if (allAgreements.isEmpty()) {
            System.out.println("No rental agreements found.");
            return;
        }

        // Prompt user for sorting choice
        System.out.println("Choose a sorting parameter:");
        System.out.println("1. Lease Period");
        System.out.println("2. Contract Date");
        System.out.println("3. Renting Fee");
        System.out.println("4. Status");
        System.out.println("5. ID");
        System.out.print("> Enter your choice: ");

        int choice = -1;

        do {
            try {
                System.out.print("Enter your choice (1-5): ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                if (choice < 1 || choice > 5) {
                    System.out.println("Invalid Choice. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                scanner.nextLine(); // Clear invalid input
            }
        } while (choice < 1 || choice > 5);


        // Apply sorting based on user choice
        switch (choice) {
            case 1:
                Collections.sort(allAgreements, new SortingByLeasePeriod());
                break;
            case 2:
                Collections.sort(allAgreements, new SortingByContractDate());
                break;
            case 3:
                Collections.sort(allAgreements, new SortingByRentingFee());
                break;
            case 4:
                Collections.sort(allAgreements, new SortingByStatus());
                break;
            case 5:
                Collections.sort(allAgreements, new SortingByID());
                break;
            default:
                System.out.println("Invalid choice, displaying without sorting.");
                break;
        }

        if (allAgreements.isEmpty()) {
            System.out.println("No Rental Agreements found.");
        } else {
            System.out.printf("%-18s %-12s %-15s %-30s %-12s %-15s %-15s %-10s%n",
                    "Agreement ID", "Property ID", "Main Tenant", "Sub Tenants", "Period", "Contract Date", "Renting Fee", "Status");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
            for (RentalAgreement agreement : allAgreements) {
                if (agreement != null) {
                    agreement.printAgreement();
                }
            }
        }

        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("Total Rental Agreements: %d%n", allAgreements.size());

        List<Object> finalResults = new ArrayList<>(allAgreements);
        consoleSaver.saveReportToFile(finalResults);
    }

    /**
     * Handles retrieval and display of rental agreements based on user-selected filters.
     *
     * <p>This method allows the user to filter rental agreements by:
     * <ul>
     *   <li>Owner Name</li>
     *   <li>Property Address</li>
     *   <li>Rental Status</li>
     * </ul>
     * Based on the user's choice, the appropriate filter is applied, and matching rental agreements are displayed.
     * The `printAgreement` method is used to display the details of each agreement.</p>
     *
     * @param rentalManager the RentalManager instance managing rental agreements.
     * @param dataMap       a HashMap containing all data collections, including rental agreements, properties, owners, and others.
     */
    @Override
    public void handleGetBy(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap) {
        HashMap<String, RentalAgreement> rentalHashMap = (HashMap<String, RentalAgreement>) dataMap.get("rental_agreements");

        System.out.println("Choose an option to filter Rental Agreements:");
        System.out.println("1. Get by Owner Name");
        System.out.println("2. Get by Property Address");
        System.out.println("3. Get by Rental Status");
        System.out.print("Enter your choice: ");

        int choice = -1;

        do {
            try {
                System.out.print("Enter your choice (1-3): ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                if (choice < 1 || choice > 3) {
                    System.out.println("Invalid Choice. Try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                scanner.nextLine(); // Clear invalid input
            }
        } while (choice < 1 || choice > 3);

        List<RentalAgreement> results = new ArrayList<>();

        switch (choice) {
            case 1:
                System.out.print("Enter Owner Name: ");
                String ownerName = scanner.nextLine().trim();
                results = rentalManager.getByOwnerName(ownerName, (HashMap<String, Owner>) dataMap.get("owners"), rentalHashMap);
                break;

            case 2:
                System.out.print("Enter Property Address: ");
                String address = scanner.nextLine().trim();
                results = rentalManager.getByPropertyAddress(address, (HashMap<String, Property>) dataMap.get("properties"), rentalHashMap);
                break;

            case 3:
                System.out.print("Enter Rental Status (NEW, ACTIVE, COMPLETED): ");
                String statusStr = scanner.nextLine().trim().toUpperCase();
                results = rentalManager.getByStatus(statusStr, rentalHashMap);
                break;

            default:
                System.out.println("Invalid choice. Please try again.");
                return;
        }

        // Display results
        if (results.isEmpty()) {
            System.out.println("No Rental Agreements found matching your criteria.");
        } else {
            System.out.println("Rental Agreements found:");
            // Print header
            System.out.printf("%-18s %-12s %-15s %-30s %-12s %-15s %-15s %-10s%n",
                    "Agreement ID", "Property ID", "Main Tenant", "Sub Tenants", "Period", "Contract Date", "Renting Fee", "Status");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
            // Use the printAgreement method for each rental agreement
            for (RentalAgreement agreement : results) {
                if (agreement != null) {
                    agreement.printAgreement();
                }
            }
        }
        List<Object> finalResults = new ArrayList<>(results);
        consoleSaver.saveReportToFile(finalResults);
    }

    public void handleGetAssociatedEntities(HashMap<String, HashMap<String, ?>> dataMap) {
        System.out.println("\nChoose an entity and an attribute to filter:");
        System.out.println("1. Owner (1-ID, 2-Name, 3-DOB)");
        System.out.println("2. Host (1-ID, 2-Name, 3-DOB)");
        System.out.println("3. Tenant (1-ID, 2-Name, 3-DOB)");
        System.out.println("4. Payment (1-ID, 2-Date, 3-Price)");
        System.out.println("5. Property (1-ID, 2-Price)");
        System.out.print("Enter your choice (e.g., '1 2' for Owner and Name): ");

        int entityChoice = -1;
        int attributeChoice = -1;

        // Input validation for two integers (entity and attribute choices)
        do {
            try {
                System.out.print("Enter your choices (entity attribute): ");
                String input = scanner.nextLine().trim();
                String[] parts = input.split("\\s+");

                if (parts.length != 2) {
                    System.out.println("Invalid input. Please enter exactly two integers separated by a space.");
                    continue;
                }

                entityChoice = Integer.parseInt(parts[0]);
                attributeChoice = Integer.parseInt(parts[1]);

                if (entityChoice < 1 || entityChoice > 5) {
                    System.out.println("Invalid entity choice. Must be between 1 and 5.");
                    entityChoice = -1; // Reset for loop validation
                } else if ((entityChoice <= 3 && (attributeChoice < 1 || attributeChoice > 3)) ||
                        (entityChoice >= 4 && attributeChoice > 3)) {
                    System.out.println("Invalid attribute choice for the selected entity.");
                    attributeChoice = -1; // Reset for loop validation
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter integers only.");
            }
        } while (entityChoice == -1 || attributeChoice == -1);

        System.out.println("\nYou chose entity " + entityChoice + " and attribute " + attributeChoice);

        // Handling each entity and attribute combination
        List<?> entityList = null;
        Comparator<?> comparator = null;

        switch (entityChoice) {
            case 1 -> {
                entityList = new ArrayList<>(((HashMap<String, Owner>) dataMap.get("owners")).values());
                comparator = switch (attributeChoice) {
                    case 1 -> Person.sortByIdComparator();
                    case 2 -> Person.sortByNameComparator();
                    case 3 -> Person.sortByDOBComparator();
                    default -> throw new IllegalArgumentException("Invalid attribute choice for Owner.");
                };
            }
            case 2 -> {
                entityList = new ArrayList<>(((HashMap<String, Host>) dataMap.get("hosts")).values());
                comparator = switch (attributeChoice) {
                    case 1 -> Person.sortByIdComparator();
                    case 2 -> Person.sortByNameComparator();
                    case 3 -> Person.sortByDOBComparator();
                    default -> throw new IllegalArgumentException("Invalid attribute choice for Host.");
                };
            }
            case 3 -> {
                entityList = new ArrayList<>(((HashMap<String, Tenant>) dataMap.get("tenants")).values());
                comparator = switch (attributeChoice) {
                    case 1 -> Person.sortByIdComparator();
                    case 2 -> Person.sortByNameComparator();
                    case 3 -> Person.sortByDOBComparator();
                    default -> throw new IllegalArgumentException("Invalid attribute choice for Tenant.");
                };
            }
            case 4 -> {
                entityList = new ArrayList<>(((HashMap<String, Payment>) dataMap.get("payments")).values());
                comparator = switch (attributeChoice) {
                    case 1 -> Payment.sortByIdComparator();
                    case 2 -> Payment.sortByDateComparator();
                    case 3 -> Payment.sortByPriceComparator();
                    default -> throw new IllegalArgumentException("Invalid attribute choice for Payment.");
                };
            }
            case 5 -> {
                entityList = new ArrayList<>(((HashMap<String, Property>) dataMap.get("properties")).values());
                comparator = switch (attributeChoice) {
                    case 1 -> Property.sortByIdComparator();
                    case 2 -> Property.sortByPriceComparator();
                    default -> throw new IllegalArgumentException("Invalid attribute choice for Property.");
                };
            }
            default -> throw new IllegalArgumentException("Invalid entity choice.");
        }

        // Sorting the selected entity list
        if (entityList != null && comparator != null) {
            Collections.sort(entityList, (Comparator) comparator);
            printEntityHeader(entityChoice);
            for (Object entity : entityList) {
                printEntityDetails(entity);
            }
        }
        consoleSaver.saveReportToFile((List<Object>)entityList);
    }

    private void printEntityHeader(int entityChoice) {
        switch (entityChoice) {
            case 1:
                System.out.printf("%-10s %-20s %-15s %-15s %-30s%n", "ID", "Name", "Date of Birth", "Phone Number", "Owned Properties");
                System.out.println("--------------------------------------------------------------------------------");
                break;
            case 2:
                System.out.printf("%-10s %-20s %-15s %-15s %-30s %-30s%n", "ID", "Name", "Date of Birth", "Phone Number", "Managed Properties", "Cooperating Owners");
                System.out.println("-----------------------------------------------------------------------------------------------------------------");
                break;
            case 3:
                System.out.printf("%-10s %-20s %-15s %-15s %-30s %-30s%n", "ID", "Name", "Date of Birth", "Phone Number", "Rental Agreements", "Payments");
                System.out.println("-------------------------------------------------------------------------------------------------------------");
                break;
            case 4:
                System.out.printf("%-10s %-12s %-15s %-20s %-15s %-15s%n",
                    "ID", "Amount", "Date", "Payment Method", "Main Tenant", "Agreement ID");
                System.out.println("-----------------------------------------------------------------------------------------");
                break;
            case 5:
                System.out.printf("%-10s %-20s %-10s %-10s %-10s %-20s %-25s %-25s %-25s%n",
                    "ID", "Address", "Price", "Status", "Owner", "Host List", "Attribute 1", "Attribute 2", "Attribute 3");
                System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                break;
            default:
                throw new IllegalArgumentException("Invalid entity choice for header.");
        }
    }

    private void printEntityDetails(Object entity) {
        if (entity instanceof Owner) {
            ((Owner) entity).printOwner();
        } else if (entity instanceof Host) {
            ((Host) entity).printHost();
        } else if (entity instanceof Tenant) {
            ((Tenant) entity).printTenant();
        } else if (entity instanceof Payment) {
            ((Payment) entity).printPayment();
        } else if (entity instanceof Property) {
            if (entity instanceof CommercialProperty) {
                ((CommercialProperty) entity).printCommercialProperty();
            } else if (entity instanceof ResidentialProperty) {
                ((ResidentialProperty) entity).printResidentialProperty();
            }
        } else {
            throw new IllegalArgumentException("Unknown entity type.");
        }
    }

}
