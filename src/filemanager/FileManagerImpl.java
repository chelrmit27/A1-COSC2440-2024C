package filemanager;

import rentalmanager.RentalManager;
import domain.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.text.*;

public class FileManagerImpl implements FileManager {
    private final Path DATA_DIRECTORY = Paths.get("data");
    private final Path RENTAL_AGREEMENTS_DIRECTORY = DATA_DIRECTORY.resolve("rental_agreements.txt");
    private final Path PAYMENT_DIRECTORY = DATA_DIRECTORY.resolve("payments.txt");
    private final Path TENANT_DIRECTORY = DATA_DIRECTORY.resolve("tenants.txt");
    private final Path HOST_DIRECTORY = DATA_DIRECTORY.resolve("hosts.txt");
    private final Path PROPERTY_DIRECTORY = DATA_DIRECTORY.resolve("properties.txt");
    private final Path OWNER_DIRECTORY = DATA_DIRECTORY.resolve("owners.txt");

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    HashMap<String, RentalAgreement> rentalHashMap = new HashMap<>();
    HashMap<String, Payment> paymentHashMap = new HashMap<>();
    HashMap<String, Tenant> tenantHashMap = new HashMap<>();
    HashMap<String, Host> hostHashMap = new HashMap<>();
    HashMap<String, Property> propertyHashMap = new HashMap<>();
    HashMap<String, Owner> ownerHashMap = new HashMap<>();

    private void ensureDataDirectoryExists() throws IOException {
        if (!Files.exists(DATA_DIRECTORY)) {
            Files.createDirectories(DATA_DIRECTORY); // Create the directory if it doesn't exist
            System.out.println("Data directory created: " + DATA_DIRECTORY.toAbsolutePath());
        }
    }

    @Override
    public HashMap<String, HashMap<String, ?>> loadFiles() throws IOException {
        ensureDataDirectoryExists();

        loadRentalAgreementFromFile();
        loadPaymentFromFile();
        loadTenantFromFile();
        loadHostFromFile();
        loadPropertyFromFile();
        loadOwnerFromFile();

        HashMap<String, HashMap<String, ?>> dataMap = new HashMap<>();
        dataMap.put("rental_agreements", rentalHashMap);
        dataMap.put("payments", paymentHashMap);
        dataMap.put("tenants", tenantHashMap);
        dataMap.put("hosts", hostHashMap);
        dataMap.put("properties", propertyHashMap);
        dataMap.put("owners", ownerHashMap);

        return dataMap;
    }

    @Override
    public void saveFile(HashMap<String, HashMap<String, ?>> dataMap) throws IOException{
        ensureDataDirectoryExists();

        saveRentalAgreementToFile((HashMap<String, RentalAgreement>) dataMap.get("rental_agreements"));
        savePaymentToFile((HashMap<String, Payment>) dataMap.get("payments"));
        saveTenantToFile((HashMap<String, Tenant>) dataMap.get("tenants"));
        saveHostToFile((HashMap<String, Host>) dataMap.get("hosts"));
        savePropertyToFile((HashMap<String, Property>) dataMap.get("properties"));
        saveOwnerToFile((HashMap<String, Owner>) dataMap.get("owners"));

    }

    /*
        rental_agreement.txt: RentalAgreementID | PropertyID | MainTenantID | HostID | OwnerID | SubTenantIDs (comma-separated) | LeasePeriod | ContractDate | RentingFee | Status
        payments.txt: PaymentID | RentalAgreementID | TenantID | Amount | PaymentDate | PaymentMethod
        tenants.txt: TenantID | FullName | DateOfBirth | ContactInfo | RentalAgreementIDs (comma-separated) | PaymentRecords (comma-separated)
        hosts.txt: HostID | FullName | DateOfBirth | ContactInfo | ManagedProperties (comma-separated) | RentalAgreementIDs (comma-separated) | CooperatingOwnerIDs (comma-separated)
        properties.txt: PropertyID | PropertyType | Address | Pricing | Status | OwnerID | HostIDs (comma-separated) | SpecificAttributes
        owners.txt: OwnerID | FullName | DateOfBirth | ContactInfo | OwnedProperties (comma-separated)

     */

    // Helper methods of loadFile()

    // rental_agreement.txt: RentalAgreementID | PropertyID | MainTenantID | HostID | OwnerID | SubTenantIDs (comma-separated) | LeasePeriod | ContractDate | RentingFee | Status
    public void loadRentalAgreementFromFile() throws IOException {
        if (!Files.exists(RENTAL_AGREEMENTS_DIRECTORY)) {
            System.out.println("Rental Agreements directory not found. Skipping...");
            return;
        }
        if (Files.size(RENTAL_AGREEMENTS_DIRECTORY) == 0){
            System.out.println("Rental Agreements directory is empty. Skipping...");
            return;
        }


    }

    // payments.txt: PaymentID | RentalAgreementID | TenantID | Amount | PaymentDate | PaymentMethod
    public void loadPaymentFromFile() throws IOException {
        if (!Files.exists(PAYMENT_DIRECTORY)) {
            System.out.println("Payment directory not found. Skipping...");
            return;
        }
        if (Files.size(PAYMENT_DIRECTORY) == 0){
            System.out.println("Payment directory is empty. Skipping...");
            return;
        }
    }

    // tenants.txt: TenantID | FullName | DateOfBirth | ContactInfo | RentalAgreementIDs (comma-separated) | PaymentRecords (comma-separated)
    public void loadTenantFromFile() throws IOException {
        if (!Files.exists(TENANT_DIRECTORY)) {
            System.out.println("Tenant directory not found. Skipping...");
            return;
        }
        if (Files.size(TENANT_DIRECTORY) == 0){
            System.out.println("Tenant directory is empty. Skipping...");
            return;
        }
    }

    //  hosts.txt: HostID | FullName | DateOfBirth | ContactInfo | ManagedProperties (comma-separated) | RentalAgreementIDs (comma-separated) | CooperatingOwnerIDs (comma-separated)
    public void loadHostFromFile() throws IOException {
        if (!Files.exists(HOST_DIRECTORY)) {
            System.out.println("Host directory not found. Skipping...");
            return;
        }
        if (Files.size(HOST_DIRECTORY) == 0){
            System.out.println("Host directory is empty. Skipping...");
            return;
        }
    }

    // properties.txt: PropertyID | PropertyType | Address | Pricing | Status | OwnerID | HostIDs (comma-separated) | SpecificAttributes
    public void loadPropertyFromFile() throws IOException {
        if (!Files.exists(PROPERTY_DIRECTORY)) {
            System.out.println("Property directory not found. Skipping...");
            return;
        }
        if (Files.size(PROPERTY_DIRECTORY) == 0){
            System.out.println("Property directory is empty. Skipping...");
            return;
        }
    }

    // owners.txt: OwnerID | FullName | DateOfBirth | ContactInfo | OwnedProperties (comma-separated)
    public void loadOwnerFromFile() throws IOException {
        if (!Files.exists(OWNER_DIRECTORY)) {
            System.out.println("Owner directory not found. Skipping...");
            return;
        }
        if (Files.size(OWNER_DIRECTORY) == 0){
            System.out.println("Owner directory is empty. Skipping...");
            return;
        }
    }

    // Helper methods of saveFile()

    // rental_agreement.txt: RentalAgreementID | PropertyID | MainTenantID | HostID | OwnerID | SubTenantIDs (comma-separated) | LeasePeriod | ContractDate | RentingFee | Status
    public void saveRentalAgreementToFile(HashMap<String, RentalAgreement> rentalHashMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RENTAL_AGREEMENTS_DIRECTORY.toFile()))) {
            for (RentalAgreement rentalAgreement : rentalHashMap.values()) {
                writer.write(rentalAgreement.getId() + " | " + rentalAgreement.getProperty().getId() + " | " + rentalAgreement.getMainTenant() + " | " + rentalAgreement.getHost().getId()
                        + " | " + rentalAgreement.getOwner().getId() + " | ");
                if (rentalAgreement.getSubTenants() != null) {
                    StringBuilder line = new StringBuilder();
                    for (Tenant tenant : rentalAgreement.getSubTenants()) {
                        line.append(tenant.getId()).append(",");
                    }
                    writer.write(line.toString());
                }
                writer.write(" | " + rentalAgreement.getPeriod() + " | " + rentalAgreement.getContractDate() + " | " + rentalAgreement.getRentingFee() + " | " + rentalAgreement.getStatus());
                writer.newLine();
            }
            writer.flush();
        }
    }

    // payments.txt: PaymentID | RentalAgreementID | TenantID | Amount | PaymentDate | PaymentMethod
    public void savePaymentToFile(HashMap<String, Payment> paymentHashMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYMENT_DIRECTORY.toFile()))) {
            for (Payment payment : paymentHashMap.values()) {
                writer.write(payment.getId() + " | " + payment.getRentalAgreement().getId() + " | " + payment.getMainTenant().getId()
                        + " | " + payment.getAmount() + " | " + payment.getDate() + " | " + payment.getPaymentMethod());
                writer.newLine();
            }
            writer.flush();
        }
    }

    // tenants.txt: TenantID | FullName | DateOfBirth | PhoneNumber | RentalAgreementIDs (comma-separated) | PaymentRecords (comma-separated)
    public void saveTenantToFile(HashMap<String, Tenant> tenantHashMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TENANT_DIRECTORY.toFile()))) {
            for (Tenant tenant : tenantHashMap.values()) {
                writer.write(tenant.getId() + " | " + tenant.getName() + " | " + tenant.getDob() + " | " + tenant.getPhoneNumber()
                        + " | ");
                if (tenant.getRentalAgreements() != null) {
                    StringBuilder line = new StringBuilder();
                    for (RentalAgreement rentalAgreement : tenant.getRentalAgreements()) {
                        line.append(rentalAgreement.getId()).append(",");
                    }
                }
                writer.write(" | ");
                if (tenant.getPaymentRecords() != null) {
                    StringBuilder line = new StringBuilder();
                    for (Payment payment : tenant.getPaymentRecords()) {
                        line.append(payment.getId()).append(",");
                    }
                }
                writer.newLine();
            }
            writer.flush();
        }
    }


    // hosts.txt: HostID | FullName | DateOfBirth | PhoneNumber | ManagedProperties (comma-separated) | RentalAgreementIDs (comma-separated) | CooperatingOwnerIDs (comma-separated)
    public void saveHostToFile(HashMap<String, Host> hostHashMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HOST_DIRECTORY.toFile()))) {
            for (Host host : hostHashMap.values()) {
                writer.write(host.getId() + " | " + host.getName() + " | " + host.getDob() + " | " + host.getPhoneNumber()
                        + " | ");
                if (host.getManagedProperty() != null) {
                    StringBuilder line = new StringBuilder();
                    for (Property property : host.getManagedProperty()) {
                        line.append(property.getId()).append(",");
                    }
                }
                writer.write(" | ");
                if (host.getRentalAgreements() != null) {
                    StringBuilder line = new StringBuilder();
                    for (RentalAgreement rentalAgreement : host.getRentalAgreements()) {
                        line.append(rentalAgreement.getId()).append(",");
                    }
                }
                writer.write(" | ");
                if (host.getCooperatingOwners() != null) {
                    StringBuilder line = new StringBuilder();
                    for (Person cooperatingOwner : host.getCooperatingOwners()) {
                        line.append(cooperatingOwner.getId()).append(",");
                    }
                }
                writer.newLine();
            }
            writer.flush();
        }
    }

    // properties.txt: PropertyID | PropertyType | Address | Pricing | Status | OwnerID | HostIDs (comma-separated) | SpecificAttributes
    public void savePropertyToFile(HashMap<String, Property> propertyHashMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROPERTY_DIRECTORY.toFile()))) {
            for (Property property : propertyHashMap.values()) {
                if (property instanceof CommercialProperty){
                    writer.write(property.getId() + " | " + "CommercialProperty" + property.getAddress() + " | " + property.getPrice() + " | "
                            + property.getStatus() + " | " + property.getOwner().getId() + " | ");
                    if (property.getHostList() != null) {
                        StringBuilder line = new StringBuilder();
                        for (Host host : property.getHostList()) {
                            line.append(host.getId()).append(",");
                        }
                    }
                    writer.write(" | BusinessType:" + ((CommercialProperty) property).getBusinessType()
                            + " | ParkingSpace:" + ((CommercialProperty) property).getParkingSpace()
                            + " | SquareFootage:" + ((CommercialProperty) property).getSquareFootage());
                }
                else if (property instanceof ResidentialProperty){
                    writer.write(property.getId() + " | " + "ResidentialProperty" + property.getAddress() + " | " + property.getPrice() + " | "
                            + property.getStatus() + " | " + property.getOwner().getId() + " | ");
                    if (property.getHostList() != null) {
                        StringBuilder line = new StringBuilder();
                        for (Host host : property.getHostList()) {
                            line.append(host.getId()).append(",");
                        }
                    }
                    writer.write(" | Bedrooms:" + ((ResidentialProperty) property).getNumOfBedrooms()
                            + " | Garden:" + ((ResidentialProperty) property).isGardenAvailability()
                            + " | PetFriendly:" + ((ResidentialProperty) property).isPetFriendliness());
                }
                writer.newLine();
            }
            writer.flush();
        }
    }

    // owners.txt: OwnerID | FullName | DateOfBirth | ContactInfo | OwnedProperties (comma-separated)
    public void saveOwnerToFile(HashMap<String, Owner> ownerHashMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OWNER_DIRECTORY.toFile()))) {
            for (Owner owner : ownerHashMap.values()) {
                writer.write(owner.getId() + " | " + owner.getName() + " | " + owner.getDob() + " | " + owner.getPhoneNumber());
                if (owner.getOwnedProperties() != null ) {
                    StringBuilder line = new StringBuilder();
                    for (Property property : owner.getOwnedProperties()) {
                        line.append(property.getId()).append(",");
                    }
                }
                writer.newLine();
            }
            writer.flush();
        }
    }
}
