package filemanager;

import domain.*;
import utils.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.text.*;
import java.lang.IllegalArgumentException;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class FileManagerImpl implements FileManager {
    private final Path DATA_DIRECTORY = Paths.get("data");
    private final Path RENTAL_AGREEMENTS_DIRECTORY = DATA_DIRECTORY.resolve("rental_agreements.txt");
    private final Path PAYMENT_DIRECTORY = DATA_DIRECTORY.resolve("payments.txt");
    private final Path TENANT_DIRECTORY = DATA_DIRECTORY.resolve("tenants.txt");
    private final Path HOST_DIRECTORY = DATA_DIRECTORY.resolve("hosts.txt");
    private final Path PROPERTY_DIRECTORY = DATA_DIRECTORY.resolve("properties.txt");
    private final Path OWNER_DIRECTORY = DATA_DIRECTORY.resolve("owners.txt");

    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    HashMap<String, RentalAgreement> rentalHashMap = new HashMap<>();
    HashMap<String, Payment> paymentHashMap = new HashMap<>();
    HashMap<String, Tenant> tenantHashMap = new HashMap<>();
    HashMap<String, Host> hostHashMap = new HashMap<>();
    HashMap<String, Property> propertyHashMap = new HashMap<>();
    HashMap<String, Owner> ownerHashMap = new HashMap<>();

    private void ensureDataDirectoryAndFilesExist() throws IOException {
        // Ensure the 'data' directory exists
        if (!Files.exists(DATA_DIRECTORY)) {
            Files.createDirectories(DATA_DIRECTORY);
        }

        // List of all file paths to check and create if not exist
        List<Path> dataFiles = Arrays.asList(
                RENTAL_AGREEMENTS_DIRECTORY,
                PAYMENT_DIRECTORY,
                TENANT_DIRECTORY,
                HOST_DIRECTORY,
                PROPERTY_DIRECTORY,
                OWNER_DIRECTORY
        );

        // Loop through each file path and create it if it doesn't exist
        for (Path filePath : dataFiles) {
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                System.out.println("Created missing file: " + filePath);
            }
        }
    }

    @Override
    public void deleteAllDataFiles() {
        try {
            clearFile(OWNER_DIRECTORY);
            clearFile(HOST_DIRECTORY);
            clearFile(TENANT_DIRECTORY);
            clearFile(PROPERTY_DIRECTORY);
            clearFile(RENTAL_AGREEMENTS_DIRECTORY);
            clearFile(PAYMENT_DIRECTORY);
        } catch (IOException e) {
            System.err.println("Error clearing data files: " + e.getMessage());
        }
    }

    private void clearFile(Path filePath) throws IOException {
        if (Files.exists(filePath)) {
            Files.newBufferedWriter(filePath).close();
        } else {
            Files.createFile(filePath);
        }
    }

    public HashMap<String, HashMap<String, ?>> loadFiles() throws IOException {
        ensureDataDirectoryAndFilesExist();

        loadPropertyFromFile(propertyHashMap, ownerHashMap, hostHashMap);
        loadOwnerFromFile(ownerHashMap, propertyHashMap);
        loadHostFromFile(hostHashMap, propertyHashMap, ownerHashMap);
        loadPropertyFromFile(propertyHashMap, ownerHashMap, hostHashMap);
        loadOwnerFromFile(ownerHashMap, propertyHashMap);
        loadHostFromFile(hostHashMap, propertyHashMap, ownerHashMap);

        loadRentalAgreementFromFile(rentalHashMap, propertyHashMap, tenantHashMap);
        loadPaymentFromFile(paymentHashMap, rentalHashMap, tenantHashMap);
        loadTenantFromFile(tenantHashMap, rentalHashMap, paymentHashMap);
        loadRentalAgreementFromFile(rentalHashMap, propertyHashMap, tenantHashMap);
        loadPaymentFromFile(paymentHashMap, rentalHashMap, tenantHashMap);
        loadTenantFromFile(tenantHashMap, rentalHashMap, paymentHashMap);

        HashMap<String, HashMap<String, ?>> dataMap = new HashMap<>();
        dataMap.put("owners", ownerHashMap);
        dataMap.put("properties", propertyHashMap);
        dataMap.put("hosts", hostHashMap);
        dataMap.put("tenants", tenantHashMap);
        dataMap.put("rental_agreements", rentalHashMap);
        dataMap.put("payments", paymentHashMap);

        return dataMap;
    }

    @Override
    public void saveFile(HashMap<String, HashMap<String, ?>> dataMap) throws IOException {
        ensureDataDirectoryAndFilesExist();

        saveRentalAgreementToFile((HashMap<String, RentalAgreement>) dataMap.get("rental_agreements"));
        savePaymentToFile((HashMap<String, Payment>) dataMap.get("payments"));
        saveTenantToFile((HashMap<String, Tenant>) dataMap.get("tenants"));
        saveHostToFile((HashMap<String, Host>) dataMap.get("hosts"));
        savePropertyToFile((HashMap<String, Property>) dataMap.get("properties"));
        saveOwnerToFile((HashMap<String, Owner>) dataMap.get("owners"));
    }

    public void loadRentalAgreementFromFile(
            HashMap<String, RentalAgreement> rentalHashMap,
            HashMap<String, Property> propertyHashMap,
            HashMap<String, Tenant> tenantHashMap) throws IOException {

        if (!Files.exists(RENTAL_AGREEMENTS_DIRECTORY)) {
            System.out.println("Rental Agreements directory not found");
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(RENTAL_AGREEMENTS_DIRECTORY)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" \\| ");
                String rentalAgreementId = tokens[0].trim();

                Property property = (tokens.length > 1 && !tokens[1].trim().isEmpty()) ? propertyHashMap.get(tokens[1].trim()) : null;
                Tenant mainTenant = (tokens.length > 2 && !tokens[2].trim().isEmpty()) ? tenantHashMap.get(tokens[2].trim()) : null;

                List<Tenant> subTenants = new ArrayList<>();
                if (tokens.length > 3 && !tokens[3].trim().isEmpty()) {
                    String[] subTenantIds = tokens[3].split(",");
                    for (String subTenantId : subTenantIds) {
                        Tenant subTenant = tenantHashMap.get(subTenantId.trim());
                        if (subTenant != null) {
                            subTenants.add(subTenant);
                        }
                    }
                }

                LeasePeriod leasePeriod = LeasePeriod.DAILY;
                if (tokens.length > 4 && !tokens[4].trim().isEmpty()) {
                    try {
                        leasePeriod = LeasePeriod.valueOf(tokens[4].trim().toUpperCase());
                    } catch (IllegalArgumentException ignored) {
                    }
                }

                Date contractDate = null;
                if (tokens.length > 5 && !tokens[5].trim().isEmpty()) {
                    try {
                        contractDate = dateFormat.parse(tokens[5].trim());
                    } catch (ParseException ignored) {
                    }
                }

                double rentingFee = (tokens.length > 6 && !tokens[6].trim().isEmpty()) ? Double.parseDouble(tokens[6].trim()) : 0.0;

                RentalStatus status = RentalStatus.NEW;
                if (tokens.length > 7 && !tokens[7].trim().isEmpty()) {
                    try {
                        status = RentalStatus.valueOf(tokens[7].trim().toUpperCase());
                    } catch (IllegalArgumentException ignored) {
                    }
                }

                RentalAgreement rentalAgreement = new RentalAgreement(
                        rentalAgreementId, property, mainTenant, subTenants, leasePeriod, contractDate, rentingFee, status);

                rentalHashMap.put(rentalAgreementId, rentalAgreement);
            }
        }
    }

    public void loadPaymentFromFile(HashMap<String, Payment> paymentHashMap,
                                    HashMap<String, RentalAgreement> rentalHashMap,
                                    HashMap<String, Tenant> tenantHashMap) throws IOException {
        if (!Files.exists(PAYMENT_DIRECTORY)) {
            System.out.println("Payment directory not found");
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(PAYMENT_DIRECTORY)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" \\| ");
                String paymentId = tokens[0];

                double amount;
                try {
                    amount = (tokens.length > 1 && !tokens[1].trim().isEmpty())
                            ? Double.parseDouble(tokens[1].trim())
                            : 0.0;
                } catch (NumberFormatException ignored) {
                    continue;
                }

                Date paymentDate = null;
                if (tokens.length > 2 && !tokens[2].trim().isEmpty()) {
                    try {
                        paymentDate = dateFormat.parse(tokens[2].trim());
                    } catch (ParseException ignored) {
                    }
                }

                String paymentMethod = (tokens.length > 3) ? tokens[3].trim() : "";

                Tenant mainTenant = (tokens.length > 4 && !tokens[4].trim().isEmpty()) ? tenantHashMap.get(tokens[4].trim()) : null;

                RentalAgreement rentalAgreement = (tokens.length > 5 && !tokens[5].trim().isEmpty()) ? rentalHashMap.get(tokens[5].trim()) : null;

                Payment payment = new Payment(paymentId, amount, paymentDate, paymentMethod, mainTenant, rentalAgreement);

                paymentHashMap.put(paymentId, payment);
            }
        }
    }

    public void loadTenantFromFile(HashMap<String, Tenant> tenantHashMap,
                                   HashMap<String, RentalAgreement> rentalHashMap,
                                   HashMap<String, Payment> paymentHashMap) throws IOException {
        if (!Files.exists(TENANT_DIRECTORY)) {
            System.out.println("Tenant directory not found");
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(TENANT_DIRECTORY)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" \\| ");
                if (tokens.length < 4) {
                    continue;
                }

                String tenantId = tokens[0].trim();
                String fullName = tokens[1].trim();

                Date dob = null;
                if (tokens.length > 2 && !tokens[2].trim().isEmpty()) {
                    try {
                        dob = dateFormat.parse(tokens[2].trim());
                    } catch (ParseException ignored) {
                    }
                }

                String contactInfo = (tokens.length > 3) ? tokens[3].trim() : "";

                List<Payment> paymentRecords = new ArrayList<>();
                if (tokens.length > 4 && !tokens[4].trim().isEmpty()) {
                    String[] paymentIds = tokens[4].split(",");
                    for (String paymentId : paymentIds) {
                        Payment payment = paymentHashMap.get(paymentId.trim());
                        if (payment != null) {
                            paymentRecords.add(payment);
                        }
                    }
                }

                List<RentalAgreement> rentalAgreements = new ArrayList<>();
                if (tokens.length > 5 && !tokens[5].trim().isEmpty()) {
                    String[] rentalAgreementIds = tokens[5].split(",");
                    for (String agreementId : rentalAgreementIds) {
                        RentalAgreement rentalAgreement = rentalHashMap.get(agreementId.trim());
                        if (rentalAgreement != null) {
                            rentalAgreements.add(rentalAgreement);
                        }
                    }
                }

                Tenant tenant = new Tenant(tenantId, fullName, dob, contactInfo, rentalAgreements, paymentRecords);
                tenantHashMap.put(tenantId, tenant);
            }
        }
    }

    public void loadOwnerFromFile(HashMap<String, Owner> ownerHashMap,
                                  HashMap<String, Property> propertyHashMap) throws IOException {
        if (!Files.exists(OWNER_DIRECTORY)) {
            System.out.println("Owner directory not found");
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(OWNER_DIRECTORY)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" \\| ");
                String ownerId = tokens[0].trim();
                String fullName = tokens[1].trim();

                Date dob = null;
                if (tokens.length > 2 && !tokens[2].trim().isEmpty()) {
                    try {
                        dob = dateFormat.parse(tokens[2].trim());
                    } catch (ParseException ignored) {
                    }
                }

                String contactInfo = (tokens.length > 3) ? tokens[3].trim() : "";

                List<Property> ownedProperties = new ArrayList<>();
                if (tokens.length > 4 && !tokens[4].trim().isEmpty()) {
                    String[] propertyIds = tokens[4].split(",");
                    for (String propertyId : propertyIds) {
                        Property property = propertyHashMap.get(propertyId.trim());
                        if (property != null) {
                            ownedProperties.add(property);
                        }
                    }
                }

                Owner owner = new Owner(ownerId, fullName, dob, contactInfo, ownedProperties);
                ownerHashMap.put(ownerId, owner);
            }
        }
    }

    public void loadHostFromFile(
            HashMap<String, Host> hostHashMap,
            HashMap<String, Property> propertyHashMap,
            HashMap<String, Owner> ownerHashMap) throws IOException {

        if (!Files.exists(HOST_DIRECTORY)) {
            System.out.println("Host directory not found");
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(HOST_DIRECTORY)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" \\| ");
                String hostId = tokens[0].trim();
                String fullName = tokens[1].trim();

                Date dob = null;
                if (tokens.length > 2 && !tokens[2].trim().isEmpty()) {
                    try {
                        dob = dateFormat.parse(tokens[2].trim());
                    } catch (ParseException ignored) {
                    }
                }

                String contactInfo = (tokens.length > 3) ? tokens[3].trim() : "";

                List<Owner> cooperatingOwners = new ArrayList<>();
                if (tokens.length > 4 && !tokens[4].trim().isEmpty()) {
                    String[] ownerIds = tokens[4].split(",");
                    for (String ownerId : ownerIds) {
                        Owner owner = ownerHashMap.get(ownerId.trim());
                        if (owner != null) {
                            cooperatingOwners.add(owner);
                        }
                    }
                }

                List<Property> managedProperties = new ArrayList<>();
                if (tokens.length > 5 && !tokens[5].trim().isEmpty()) {
                    String[] propertyIds = tokens[5].split(",");
                    for (String propertyId : propertyIds) {
                        Property property = propertyHashMap.get(propertyId.trim());
                        if (property != null) {
                            managedProperties.add(property);
                        }
                    }
                }

                Host host = new Host(hostId, fullName, dob, contactInfo, cooperatingOwners, managedProperties);
                hostHashMap.put(hostId, host);
            }
        }
    }

    public void loadPropertyFromFile(
            HashMap<String, Property> propertyHashMap,
            HashMap<String, Owner> ownerHashMap,
            HashMap<String, Host> hostHashMap) throws IOException {

        if (!Files.exists(PROPERTY_DIRECTORY)) {
            System.out.println("Property Directory not found");
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(PROPERTY_DIRECTORY)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" \\| ");
                String propertyId = tokens[0].trim();
                String address = tokens[1].trim();

                double pricing;
                try {
                    pricing = Double.parseDouble(tokens[2].trim());
                } catch (NumberFormatException ignored) {
                    continue;
                }

                PropertyStatus status;
                try {
                    status = PropertyStatus.valueOf(tokens[3].trim().toUpperCase());
                } catch (IllegalArgumentException ignored) {
                    continue;
                }

                Owner owner = (tokens.length > 4 && !tokens[4].trim().isEmpty()) ? ownerHashMap.get(tokens[4].trim()) : null;

                List<Host> hostList = new ArrayList<>();
                if (tokens.length > 5 && !tokens[5].trim().isEmpty()) {
                    String[] hostIds = tokens[5].split(",");
                    for (String hostId : hostIds) {
                        Host host = hostHashMap.get(hostId.trim());
                        if (host != null) {
                            hostList.add(host);
                        }
                    }
                }

                String specificAttributes = tokens.length > 6 ? tokens[6].trim() : "";
                Property property;

                if (InputValidator.isValidResidentialPropertyID(propertyId)) {
                    int bedrooms = Integer.parseInt(getAttributeValueOrDefault("Bedrooms", specificAttributes, "0"));
                    boolean garden = Boolean.parseBoolean(getAttributeValueOrDefault("Garden", specificAttributes, "false"));
                    boolean petFriendly = Boolean.parseBoolean(getAttributeValueOrDefault("PetFriendly", specificAttributes, "false"));

                    property = new ResidentialProperty(propertyId, address, pricing, status, owner, hostList, bedrooms, garden, petFriendly);

                } else if (InputValidator.isValidCommercialPropertyID(propertyId)) {
                    String businessType = getAttributeValueOrDefault("BusinessType", specificAttributes, "Unknown");
                    int parkingSpace = Integer.parseInt(getAttributeValueOrDefault("ParkingSpace", specificAttributes, "0"));
                    double squareFootage = Double.parseDouble(getAttributeValueOrDefault("SquareFootage", specificAttributes, "0.0"));

                    property = new CommercialProperty(propertyId, address, pricing, status, owner, hostList, businessType, parkingSpace, squareFootage);

                } else {
                    continue;
                }

                propertyHashMap.put(propertyId, property);
            }
        }
    }

    private String getAttributeValueOrDefault(String key, String data, String defaultValue) {
        for (String attribute : data.split(",")) {
            String[] kv = attribute.split(":");
            if (kv.length == 2 && kv[0].trim().equalsIgnoreCase(key)) {
                return kv[1].trim();
            }
        }
        return defaultValue;
    }

    public void saveRentalAgreementToFile(HashMap<String, RentalAgreement> rentalHashMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RENTAL_AGREEMENTS_DIRECTORY.toFile()))) {
            for (RentalAgreement rentalAgreement : rentalHashMap.values()) {
                writer.write(rentalAgreement.getId() + " | ");

                writer.write((rentalAgreement.getProperty() != null) ? rentalAgreement.getProperty().getId() : "");
                writer.write(" | ");

                writer.write((rentalAgreement.getMainTenant() != null) ? rentalAgreement.getMainTenant().getId() : "");
                writer.write(" | ");

                if (rentalAgreement.getSubTenants() != null && !rentalAgreement.getSubTenants().isEmpty()) {
                    StringBuilder subTenantIds = new StringBuilder();
                    for (Tenant tenant : rentalAgreement.getSubTenants()) {
                        subTenantIds.append(tenant.getId()).append(",");
                    }
                    subTenantIds.setLength(subTenantIds.length() - 1);
                    writer.write(subTenantIds.toString());
                }
                writer.write(" | ");

                writer.write((rentalAgreement.getPeriod() != null) ? rentalAgreement.getPeriod().toString() : LeasePeriod.DAILY.toString());
                writer.write(" | ");

                writer.write((rentalAgreement.getContractDate() != null) ? dateFormat.format(rentalAgreement.getContractDate()) : "");
                writer.write(" | ");

                writer.write((rentalAgreement.getRentingFee() != null) ? rentalAgreement.getRentingFee().toString() : "0.0");
                writer.write(" | ");

                writer.write((rentalAgreement.getStatus() != null) ? rentalAgreement.getStatus().toString() : RentalStatus.NEW.toString());
                writer.newLine();
            }
            writer.flush();
        }
    }

    public void savePaymentToFile(HashMap<String, Payment> paymentHashMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYMENT_DIRECTORY.toFile()))) {
            for (Payment payment : paymentHashMap.values()) {
                writer.write(payment.getId() + " | ");

                writer.write(payment.getAmount() + " | " +
                        (payment.getDate() != null ? dateFormat.format(payment.getDate()) : "") + " | " +
                        payment.getPaymentMethod() + " | ");

                writer.write((payment.getMainTenant() != null) ? payment.getMainTenant().getId() : "");
                writer.write(" | ");

                writer.write((payment.getRentalAgreement() != null) ? payment.getRentalAgreement().getId() : "");
                writer.newLine();
            }
            writer.flush();
        }
    }

    public void saveTenantToFile(HashMap<String, Tenant> tenantHashMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TENANT_DIRECTORY.toFile()))) {
            for (Tenant tenant : tenantHashMap.values()) {
                writer.write(tenant.getId() + " | " + tenant.getName() + " | " +
                        (tenant.getDob() != null ? dateFormat.format(tenant.getDob()) : "") + " | " +
                        tenant.getPhoneNumber() + " | ");

                if (tenant.getPaymentRecords() != null && !tenant.getPaymentRecords().isEmpty()) {
                    String paymentIds = tenant.getPaymentRecords().stream()
                            .map(Payment::getId)
                            .reduce((a, b) -> a + "," + b)
                            .orElse("");
                    writer.write(paymentIds);
                    writer.write(" | ");
                }

                if (tenant.getRentalAgreements() != null && !tenant.getRentalAgreements().isEmpty()) {
                    String rentalAgreementIds = tenant.getRentalAgreements().stream()
                            .map(RentalAgreement::getId)
                            .reduce((a, b) -> a + "," + b)
                            .orElse("");
                    writer.write(rentalAgreementIds);
                }
                writer.newLine();
            }
            writer.flush();
        }
    }

    public void saveHostToFile(HashMap<String, Host> hostHashMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HOST_DIRECTORY.toFile()))) {
            for (Host host : hostHashMap.values()) {
                writer.write(host.getId() + " | " + host.getName() + " | " +
                        (host.getDob() != null ? dateFormat.format(host.getDob()) : "") + " | " +
                        host.getPhoneNumber() + " | ");

                if (host.getCooperatingOwners() != null && !host.getCooperatingOwners().isEmpty()) {
                    String cooperatingOwnerIds = host.getCooperatingOwners().stream()
                            .map(Owner::getId)
                            .reduce((a, b) -> a + "," + b)
                            .orElse("");
                    writer.write(cooperatingOwnerIds);
                }
                writer.write(" | ");

                if (host.getManagedProperties() != null && !host.getManagedProperties().isEmpty()) {
                    String managedPropertyIds = host.getManagedProperties().stream()
                            .map(Property::getId)
                            .reduce((a, b) -> a + "," + b)
                            .orElse("");
                    writer.write(managedPropertyIds);
                }
                writer.newLine();
            }
            writer.flush();
        }
    }

    public void savePropertyToFile(HashMap<String, Property> propertyHashMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROPERTY_DIRECTORY.toFile()))) {
            for (Property property : propertyHashMap.values()) {
                writer.write(property.getId() + " | ");

                if (InputValidator.isValidResidentialPropertyID(property.getId())) {
                    ResidentialProperty residentialProperty = (ResidentialProperty) property;

                    writer.write(residentialProperty.getAddress() + " | " +
                            residentialProperty.getPrice() + " | " +
                            residentialProperty.getStatus() + " | " +
                            (residentialProperty.getOwner() != null ? residentialProperty.getOwner().getId() : "") + " | ");

                    if (residentialProperty.getHostList() != null && !residentialProperty.getHostList().isEmpty()) {
                        StringBuilder hostIds = new StringBuilder();
                        for (Host host : residentialProperty.getHostList()) {
                            hostIds.append(host.getId()).append(",");
                        }
                        hostIds.setLength(hostIds.length() - 1);
                        writer.write(hostIds.toString());
                    }
                    writer.write(" | ");

                    writer.write("Bedrooms:" + residentialProperty.getNumOfBedrooms() + "," +
                            "Garden:" + residentialProperty.isGardenAvailability() + "," +
                            "PetFriendly:" + residentialProperty.isPetFriendliness());

                } else if (InputValidator.isValidCommercialPropertyID(property.getId())) {
                    CommercialProperty commercialProperty = (CommercialProperty) property;

                    writer.write(commercialProperty.getAddress() + " | " +
                            commercialProperty.getPrice() + " | " +
                            commercialProperty.getStatus() + " | " +
                            (commercialProperty.getOwner() != null ? commercialProperty.getOwner().getId() : "") + " | ");

                    if (commercialProperty.getHostList() != null && !commercialProperty.getHostList().isEmpty()) {
                        StringBuilder hostIds = new StringBuilder();
                        for (Host host : commercialProperty.getHostList()) {
                            hostIds.append(host.getId()).append(",");
                        }
                        hostIds.setLength(hostIds.length() - 1);
                        writer.write(hostIds.toString());
                    }
                    writer.write(" | ");

                    writer.write("BusinessType:" + commercialProperty.getBusinessType() + "," +
                            "ParkingSpace:" + commercialProperty.getParkingSpace() + "," +
                            "SquareFootage:" + commercialProperty.getSquareFootage());
                }
                writer.newLine();
            }
            writer.flush();
        }
    }

    public void saveOwnerToFile(HashMap<String, Owner> ownerHashMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OWNER_DIRECTORY.toFile()))) {
            for (Owner owner : ownerHashMap.values()) {
                writer.write(owner.getId() + " | " + owner.getName() + " | " +
                        (owner.getDob() != null ? dateFormat.format(owner.getDob()) : "") + " | " +
                        owner.getPhoneNumber());
                writer.write(" | ");

                if (owner.getOwnedProperties() != null && !owner.getOwnedProperties().isEmpty()) {
                    String ownedProperties = owner.getOwnedProperties().stream()
                            .map(Property::getId)
                            .reduce((a, b) -> a + "," + b)
                            .orElse("");
                    writer.write(ownedProperties);
                }
                writer.newLine();
            }
            writer.flush();
        }
    }
}
