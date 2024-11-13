package adminOperatorManager;

import rentalmanager.*;
import filemanager.*;
import utils.*;
import domain.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.text.ParseException;
import java.io.IOException;

import java.util.HashMap;

public class AdminOperatorManagerImpl implements AdminOperatorManager {
    private final RentalManager rentalManager = new RentalManagerImpl();
    private final FileManager fileManager = new FileManagerImpl();
    private final InputValidator validator = new InputValidator();
    private final Utils utils = new Utils();
    static Scanner scanner = new Scanner(System.in);
    static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    public void handleAddAgreement(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap){
        RentalAgreement newAgreement = Utils.createRentalAgreement(dataMap);

        if (newAgreement != null){
            // Add new agreement to rental manager
            rentalManager.addAgreement(newAgreement);

            // Update rental agreements in datamap
            List<RentalAgreement> rentalAgreements = rentalManager.getAll();
            HashMap<String, RentalAgreement> tempRentalHashMap = new HashMap<>();
            for (RentalAgreement r : rentalAgreements){
                tempRentalHashMap.put(r.getId(), r);
            }
            dataMap.put("rental_agreements", tempRentalHashMap);

            // Update tenants,hosts, owners with the new agreement and update respective hashMaps
            Tenant mainTenant = newAgreement.getMainTenant();
            Host host = newAgreement.getHost();
            Owner owner = newAgreement.getOwner();

            HashMap<String, Tenant> tenantHashMap = (HashMap<String, Tenant>) dataMap.get("tenants");
            HashMap<String, Host> hostHashMap = (HashMap<String, Host>) dataMap.get("hosts");
            HashMap<String, Owner> ownerHashMap = (HashMap<String, Owner>) dataMap.get("owners");

            if (mainTenant != null) {
                mainTenant.getRentalAgreements().add(newAgreement);
                tenantHashMap.put(mainTenant.getId(), mainTenant);
            }
            if (host != null) {
                host.getRentalAgreements().add(newAgreement);
                hostHashMap.put(host.getId(), host);
            }
            if (owner != null) {
                owner.getRentalAgreements().add(newAgreement);
                ownerHashMap.put(owner.getId(), owner);
            }

            for (Tenant subTenant : newAgreement.getSubTenants()){
                subTenant.getRentalAgreements().add(newAgreement);
                tenantHashMap.put(subTenant.getId(), subTenant);
            }

            // save the whole dataMap
            try{
                fileManager.saveFile(dataMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void handleUpdateAgreement(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap){
        String id = null;
        RentalAgreement agreementToUpdate = null;
        do{
            System.out.println("> Enter Rental Agreement ID (RA***): ");
            id = scanner.nextLine();
            if (!validator.isValidRentalAgreementIDFormat(id)){
                System.out.println("Invalid Rental Agreement ID. Try again.");
            } else{
                agreementToUpdate = rentalManager.getOne(id);
                if (agreementToUpdate == null){
                    System.out.println("Rental Agreement does not exist. Try again.");
                }
            }
        } while (!validator.isValidRentalAgreementIDFormat(id) || agreementToUpdate == null);

        if (agreementToUpdate != null) {
            rentalManager.addAgreement(agreementToUpdate);

            // Display header and agreement details with refined format
            String tabs = "\t".repeat(17);
            System.out.println(tabs + "Rental Agreement Details");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-18s %-12s %-12s %-12s %-12s %-30s %-12s %-12s %-10s %-10s%n",
                    "Rental Agreement ID", "Property ID", "Main Tenant ID", "Host ID", "Owner ID", "Sub Tenant IDs", "Lease Period", "Contract Date", "Renting Fee", "Status");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            agreementToUpdate.printAgreement();
        }

        System.out.println("\nChoose the field to update:");
        System.out.println("1. Property");
        System.out.println("2. Main Tenant");
        System.out.println("3. Host");
        System.out.println("4. Owner");
        System.out.println("5. Sub Tenants");
        System.out.println("6. Lease Period");
        System.out.println("7. Contract Date");
        System.out.println("8. Renting Fee");
        System.out.println("9. Rental Status");
        System.out.print("> Enter your choice: ");

        int choice = 0;
        do{
            choice = scanner.nextInt();
            scanner.nextLine();
            if (choice < 1 || choice > 10){
                System.out.println("Invalid Choice. Try again.");
            }
        } while(choice < 1 || choice > 10);

        switch (choice) {
            case 1:
                // Update Property
                Property newProperty = Utils.createProperty(dataMap);
                agreementToUpdate.setProperty(newProperty);
                break;
            case 2:
                // Update Main Tenant
                Tenant newMainTenant = Utils.createTenant(dataMap);
                agreementToUpdate.setMainTenant(newMainTenant);
                break;
            case 3:
                // Update Host
                Host newHost = Utils.createHost(dataMap);
                agreementToUpdate.setHost(newHost);
                break;
            case 4:
                // Update Owner
                Owner newOwner = Utils.createOwner(dataMap);
                agreementToUpdate.setOwner(newOwner);
                break;
            case 5:
                // Update Sub Tenants
                List<Tenant> newSubTenants = Utils.createList(() -> Utils.createTenant(dataMap));
                agreementToUpdate.setSubTenants(newSubTenants);
                break;
            case 6:
                // Update Lease Period
                String newLeasePeriod = Utils.getInput("Lease Period");
                agreementToUpdate.setPeriod(newLeasePeriod);
                break;
            case 7:
                // Update Contract Date
                Date newContractDate = Utils.getDateInput("Contract Date (dd/MM/yyyy)");
                agreementToUpdate.setContractDate(newContractDate);
                break;
            case 8:
                // Update Renting Fee
                double newRentingFee = Utils.getPositiveDouble("Renting Fee");
                agreementToUpdate.setRentingFee(newRentingFee);
                break;
            case 9:
                // Update Rental Status
                RentalStatus newStatus = Utils.getEnumInput(RentalStatus.class, "Rental Status (NEW, ACTIVE, COMPLETED)");
                agreementToUpdate.setStatus(newStatus);
                break;
            default:
                System.out.println("Invalid Choice. Try again.");
                return;
        }

        // Update in rentalManager and dataMap
        rentalManager.updateAgreement(agreementToUpdate);
        ((HashMap<String, RentalAgreement>) dataMap.get("rental_agreements")).put(agreementToUpdate.getId(), agreementToUpdate);

        // Save updated dataMap to file
        try {
            fileManager.saveFile(dataMap);
            System.out.println("Rental agreement updated successfully and saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving updated rental agreement to file.");
            e.printStackTrace();
        }
    }

    @Override
    public void handleDeleteAgreement(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap){
        String id = null;
        RentalAgreement agreementToDelete = null;
        do{
            System.out.println("> Enter Rental Agreement ID (RA***): ");
            id = scanner.nextLine();
            if (!validator.isValidRentalAgreementIDFormat(id)){
                System.out.println("Invalid Rental Agreement ID. Try again.");
            } else{
                agreementToDelete = rentalManager.getOne(id);
                if (agreementToDelete == null){
                    System.out.println("Rental Agreement does not exist. Try again.");
                }
            }
        } while (!validator.isValidRentalAgreementIDFormat(id) || agreementToDelete == null);

        if (agreementToDelete != null) {
            // Remove agreement from the RentalManager
            rentalManager.deleteAgreement(id);

            // Update related entities
            Tenant mainTenant = agreementToDelete.getMainTenant();
            Host host = agreementToDelete.getHost();
            Owner owner = agreementToDelete.getOwner();

            if (mainTenant != null) mainTenant.getRentalAgreements().remove(agreementToDelete);
            if (host != null) host.getRentalAgreements().remove(agreementToDelete);
            if (owner != null) owner.getRentalAgreements().remove(agreementToDelete);

            for (Tenant subTenant : agreementToDelete.getSubTenants()) {
                subTenant.getRentalAgreements().remove(agreementToDelete);
            }

            // Update the dataMap's rental agreement entries
            ((HashMap<String, RentalAgreement>) dataMap.get("rental_agreements")).remove(id);

            // Save updated dataMap to file
            try {
                fileManager.saveFile(dataMap);
                System.out.println("Rental agreement deleted successfully and changes saved to file.");
            } catch (IOException e) {
                System.out.println("Error saving changes to file after deletion.");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void handleGetOne(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap) {
        String id;
        RentalAgreement agreementToGet = null;

        // Validate Rental Agreement ID and retrieve the agreement
        do {
            System.out.print("> Enter Rental Agreement ID (RA***): ");
            id = scanner.nextLine();
            if (!validator.isValidRentalAgreementIDFormat(id)) {
                System.out.println("Invalid Rental Agreement ID format. Try again.");
            } else {
                agreementToGet = rentalManager.getOne(id);
                if (agreementToGet == null) {
                    System.out.println("Rental Agreement does not exist. Try again.");
                }
            }
        } while (!validator.isValidRentalAgreementIDFormat(id) || agreementToGet == null);

        // Print the agreement details if found
        if (agreementToGet != null) {
            String tabs = "\t".repeat(17);
            System.out.println(tabs + "Rental Agreement Details");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            System.out.printf("%-18s %-12s %-12s %-12s %-12s %-30s %-12s %-12s %-10s %-10s%n",
                    "Rental Agreement ID", "Property ID", "Main Tenant ID", "Host ID", "Owner ID", "Sub Tenant IDs", "Lease Period", "Contract Date", "Renting Fee", "Status");
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            agreementToGet.printAgreement();
            System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        }
    }


    @Override
    public void handleGetAll(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap) {
        List<RentalAgreement> allAgreements = rentalManager.getAll();

        if (allAgreements.isEmpty()) {
            System.out.println("No rental agreements found.");
            return;
        }

        // Print header
        String tabs = "\t".repeat(17);
        System.out.println(tabs + "All Rental Agreements");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-18s %-12s %-12s %-12s %-12s %-30s %-12s %-12s %-10s %-10s%n",
                "Rental Agreement ID", "Property ID", "Main Tenant ID", "Host ID", "Owner ID", "Sub Tenant IDs", "Lease Period", "Contract Date", "Renting Fee", "Status");
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");

        // Print each agreement
        for (RentalAgreement agreement : allAgreements) {
            agreement.printAgreement();
        }

        // Print footer
        System.out.println("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("Total Rental Agreements: %d%n", allAgreements.size());
    }

}
