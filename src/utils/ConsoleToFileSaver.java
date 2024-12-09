package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import domain.*;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class ConsoleToFileSaver {

    private String promptToSave() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Do you want to save this report to a .txt file? (yes/no): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("yes")) {
            System.out.print("Enter the name of the .txt file (without extension): ");
            String fileName = scanner.nextLine().trim();

            // Ensure the file has a .txt extension
            if (!fileName.endsWith(".txt")) {
                fileName += ".txt";
            }

            return fileName; // Return the chosen file name
        }

        System.out.println("No file will be created.");
        return null; // User opted not to save
    }

    private String generateContent(List<Object> objectsList) {
        Object firstObject = objectsList.get(0);
        StringBuilder content = new StringBuilder();

        if (firstObject instanceof RentalAgreement) {
            content.append(generateRentalAgreementReport(objectsList));
        } else if (firstObject instanceof Owner) {
            content.append(generateOwnerReport(objectsList));
        } else if (firstObject instanceof Host) {
            content.append(generateHostReport(objectsList));
        } else if (firstObject instanceof Tenant) {
            content.append(generateTenantReport(objectsList));
        } else if (firstObject instanceof Payment) {
            content.append(generatePaymentReport(objectsList));
        } else if (firstObject instanceof Property) {
            content.append(generatePropertyReport(objectsList));
        } else {
            System.out.println("Unsupported type.");
        }

        return content.toString();
    }

    public void saveReportToFile(List<Object> objectsList) {
        if (objectsList == null || objectsList.size() == 0) {
            return;
        }

        String fileName = promptToSave();
        if (fileName == null) {
            return;
        }

        String content = generateContent(objectsList);

        // Save the content to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content.toString());
            System.out.println("Report saved to " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }

    }

    private String generateRentalAgreementReport(List<Object> objectsList) {
        StringBuilder content = new StringBuilder();
        content.append(String.format("%-18s %-12s %-15s %-30s %-12s %-15s %-15s %-10s%n",
                "Agreement ID", "Property ID", "Main Tenant", "Sub Tenants", "Period", "Contract Date", "Renting Fee", "Status"));
        content.append("-----------------------------------------------------------------------------------------------------------------------------------\n");

        for (Object obj : objectsList) {
            ((RentalAgreement) obj).printAgreement(content);
        }
        return content.toString();
    }

    private String generateOwnerReport(List<Object> objectsList) {
        StringBuilder content = new StringBuilder();
        content.append(String.format("%-10s %-20s %-15s %-15s %-30s%n", "ID", "Name", "Date of Birth", "Phone Number", "Owned Properties"));
        content.append("--------------------------------------------------------------------------------\n");
        for (Object obj : objectsList) {
            ((Owner) obj).printOwner(content);
        }
        return content.toString();
    }

    private String generateHostReport(List<Object> objectsList) {
        StringBuilder content = new StringBuilder();
        content.append(String.format("%-10s %-20s %-15s %-15s %-30s %-30s%n", "ID", "Name", "Date of Birth", "Phone Number", "Managed Properties", "Cooperating Owners"));
        content.append("-----------------------------------------------------------------------------------------------------------------\n");
        for (Object obj : objectsList) {
            ((Host) obj).printHost(content);
        }
        return content.toString();
    }

    private String generateTenantReport(List<Object> objectsList) {
        StringBuilder content = new StringBuilder();
        content.append(String.format("%-10s %-20s %-15s %-15s %-30s %-30s%n", "ID", "Name", "Date of Birth", "Phone Number", "Rental Agreements", "Payments"));
        content.append("-------------------------------------------------------------------------------------------------------------\n");
        for (Object obj : objectsList) {
            ((Tenant) obj).printTenant(content);
        }
        return content.toString();
    }

    private String generatePaymentReport(List<Object> objectsList) {
        StringBuilder content = new StringBuilder();
        content.append(String.format("%-10s %-12s %-15s %-20s %-15s %-15s%n",
                "ID", "Amount", "Date", "Payment Method", "Main Tenant", "Agreement ID"));
        content.append("-----------------------------------------------------------------------------------------\n");
        for (Object obj : objectsList) {
            ((Payment) obj).printPayment(content);
        }
        return content.toString();
    }

    private String generatePropertyReport(List<Object> objectsList) {
        StringBuilder content = new StringBuilder();
        content.append(String.format("%-10s %-20s %-10s %-10s %-10s %-20s %-25s %-25s %-25s%n",
                "ID", "Address", "Price", "Status", "Owner", "Host List", "Attribute 1", "Attribute 2", "Attribute 3"));
        content.append("------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        for (Object obj : objectsList) {
            if (obj instanceof CommercialProperty){
                ((CommercialProperty) obj).printCommercialProperty(content);
            }
            else if (obj instanceof ResidentialProperty){
                ((ResidentialProperty) obj).printResidentialProperty(content);
            }
        }
        return content.toString();
    }
}
