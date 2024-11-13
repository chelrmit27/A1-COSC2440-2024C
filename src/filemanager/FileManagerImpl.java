package filemanager;

import rentalmanager.RentalManager;
import domain.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.text.*;
import java.lang.IllegalArgumentException;

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

        try (BufferedReader reader = Files.newBufferedReader(RENTAL_AGREEMENTS_DIRECTORY)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" \\| ");
                String rentalAgreementId = tokens[0];
                String propertyId = tokens[1];
                String mainTenantId = tokens[2];
                String hostId = tokens[3];
                String ownerId = tokens[4];
                String[] subTenantsIds = tokens[5].split(",");
                String leasePeriod = tokens[6];
                Date contractDate = dateFormat.parse(tokens[7]);
                double rentingFee = Double.parseDouble(tokens[8]);
                RentalStatus status = RentalStatus.valueOf(tokens[9]);

                List<Tenant> subTenants = new ArrayList<>();
                for (String subTenantId : subTenantsIds) {
                    Tenant subTenant = tenantHashMap.get(subTenantId.trim());
                    if (subTenant != null) {
                        subTenants.add(subTenant);
                    }
                }

                RentalAgreement rentalAgreement = new RentalAgreement(rentalAgreementId,
                        propertyHashMap.get(propertyId),
                        ownerHashMap.get(ownerId),
                        hostHashMap.get(hostId),
                        tenantHashMap.get(mainTenantId),
                        subTenants,
                        leasePeriod,
                        contractDate,
                        rentingFee,
                        status);

                rentalHashMap.put(rentalAgreementId, rentalAgreement);

            }
        } catch (ParseException e) {
            e.printStackTrace();
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

        try (BufferedReader reader = Files.newBufferedReader(PAYMENT_DIRECTORY)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" \\| ");
                String paymentId = tokens[0];
                String rentalAgreementId = tokens[1];
                String tenantId = tokens[2];
                double amount = Double.parseDouble(tokens[3]);
                Date paymentDate = dateFormat.parse(tokens[4]);
                String paymentMethod = tokens[5];

                Payment payment = new Payment(paymentId,
                        rentalHashMap.get(rentalAgreementId),
                        tenantHashMap.get(tenantId),
                        amount,
                        paymentDate,
                        paymentMethod);

                paymentHashMap.put(paymentId, payment);
            }
        } catch (ParseException e) {
            e.printStackTrace();
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

        try (BufferedReader reader = Files.newBufferedReader(TENANT_DIRECTORY)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" \\| ");
                String tenantId = tokens[1];
                String name = tokens[2];
                Date dob = dateFormat.parse(tokens[3]);
                String phoneNumber = tokens[4];
                String[] rentalAgreementIds = tokens[5].split(",");
                String[] paymentRecords = tokens[6].split(",");

                List<RentalAgreement> rentalAgreementsList = new ArrayList<>();
                for (String rentalAgreementId : rentalAgreementIds) {
                    RentalAgreement rentalAgreement = rentalHashMap.get(rentalAgreementId);
                    if (rentalAgreement != null) {
                        rentalAgreementsList.add(rentalAgreement);
                    }
                }

                List<Payment> paymentRecordsList = new ArrayList<>();
                for (String paymentRecord : paymentRecords) {
                    Payment payment = paymentHashMap.get(paymentRecord);
                    if (payment != null) {
                        paymentRecordsList.add(payment);
                    }
                }

                Tenant tenant = new Tenant(tenantId,
                        name,
                        dob,
                        phoneNumber,
                        rentalAgreementsList,
                        paymentRecordsList);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //  hosts.txt: HostID | FullName | DateOfBirth | ContactInfo | ManagedPropertyIds (comma-separated) | RentalAgreementIDs (comma-separated) | CooperatingOwnerIDs (comma-separated)
    public void loadHostFromFile() throws IOException {
        if (!Files.exists(HOST_DIRECTORY)) {
            System.out.println("Host directory not found. Skipping...");
            return;
        }
        if (Files.size(HOST_DIRECTORY) == 0){
            System.out.println("Host directory is empty. Skipping...");
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(HOST_DIRECTORY)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" \\| ");
                String hostId = tokens[1];
                String name = tokens[2];
                Date dob = dateFormat.parse(tokens[3]);
                String phoneNumber = tokens[4];
                String[] managedPropertyIds = tokens[5].split(",");
                String[] rentalAgreementIds = tokens[6].split(",");
                String[] cooperatingOwnerIds = tokens[7].split(",");

                List<Property> propertyList = new ArrayList<>();
                for (String managedPropertyId : managedPropertyIds) {
                    Property property = propertyHashMap.get(managedPropertyId);
                    if (property != null) {
                        propertyList.add(property);
                    }
                }

                List<RentalAgreement> rentalAgreementsList = new ArrayList<>();
                for (String rentalAgreementId : rentalAgreementIds) {
                    RentalAgreement rentalAgreement = rentalHashMap.get(rentalAgreementId);
                    if (rentalAgreement != null) {
                        rentalAgreementsList.add(rentalAgreement);
                    }
                }

                List<Owner> ownerList = new ArrayList<>();
                for (String cooperatingOwnerId : cooperatingOwnerIds) {
                    Owner owner = ownerHashMap.get(cooperatingOwnerId);
                    if (owner != null) {
                        ownerList.add(owner);
                    }
                }

                Host host = new Host(hostId,
                        name,
                        dob,
                        phoneNumber,
                        propertyList,
                        ownerList,
                        rentalAgreementsList);

            }
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    // properties.txt: PropertyID | PropertyType | Address | Pricing | Status | OwnerID | HostIDs (comma-separated) | SpecificAttributes
    public void loadPropertyFromFile() throws IOException {
        if (!Files.exists(PROPERTY_DIRECTORY) || Files.size(PROPERTY_DIRECTORY) == 0) {
            System.out.println("Property file not found or is empty. Skipping...");
            return;
        }

        try (BufferedReader reader = Files.newBufferedReader(PROPERTY_DIRECTORY)){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" \\| ");
                String propertyId = tokens[0];
                String propertyType = tokens[1];
                String address = tokens[2];
                double price = Double.parseDouble(tokens[3]);
                PropertyStatus status = PropertyStatus.valueOf(tokens[4]);
                String ownerId = tokens[5];
                String[] hostIds = tokens[6].split(",");
                String specificAttributes = tokens[7];

                List<Host> hostList = new ArrayList<>();
                for (String hostId : hostIds) {
                    Host host = hostHashMap.get(hostId);
                    if (host != null) {
                        hostList.add(host);
                    }
                }

                Property property = null;
                if ("ResidentialProperty".equals(propertyType)) {
                    int bedrooms = Integer.parseInt(getAttributeValue("Bedrooms", specificAttributes));
                    boolean garden = Boolean.parseBoolean(getAttributeValue("Garden", specificAttributes));
                    boolean petFriendly = Boolean.parseBoolean(getAttributeValue("PetFriendly", specificAttributes));
                    property = new ResidentialProperty(propertyId, address, price, status, ownerHashMap.get(ownerId), hostList, bedrooms, garden, petFriendly);
                } else if ("CommercialProperty".equals(propertyType)) {
                    String businessType = getAttributeValue("BusinessType", specificAttributes);
                    int parkingSpace = Integer.parseInt(getAttributeValue("ParkingSpace", specificAttributes));
                    double squareFootage = Double.parseDouble(getAttributeValue("SquareFootage", specificAttributes));
                    property = new CommercialProperty(propertyId, address, price, status, ownerHashMap.get(ownerId), hostList, businessType, parkingSpace, squareFootage);
                }

                propertyHashMap.put(propertyId, property);
            }
        } catch (NumberFormatException e) {
            System.err.println("Number format error while loading property: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid argument error while loading property: " + e.getMessage());
        }
    }


    // Helper method for loadPropertyFromFile()
    private String getAttributeValue(String key, String data) {
        for (String attribute : data.split(",")) {
            String[] kv = attribute.split(":");
            if (kv.length == 2 && kv[0].equalsIgnoreCase(key)) {
                return kv[1];
            }
        }
        throw new IllegalArgumentException("Attribute not found: " + key);
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

        try (BufferedReader reader = Files.newBufferedReader(OWNER_DIRECTORY)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" \\| ");
                String ownerId = tokens[0];
                String name = tokens[1];
                Date dob = dateFormat.parse(tokens[2]);
                String phoneNumber = tokens[3];
                String[] ownedPropertyIds = tokens[4].split(",");
                String[] rentalAgreementIds = tokens[5].split(",");

                List<Property> ownedPropertyList = new ArrayList<>();
                for (String ownedPropertyId : ownedPropertyIds) {
                    Property property = propertyHashMap.get(ownedPropertyId);
                    if (property != null) {
                        ownedPropertyList.add(property);
                    }
                }

                List<RentalAgreement> rentalAgreementList = new ArrayList<>();
                for (String rentalAgreementId : rentalAgreementIds) {
                    RentalAgreement rentalAgreement = rentalHashMap.get(rentalAgreementId);
                    if (rentalAgreement != null) {
                        rentalAgreementList.add(rentalAgreement);
                    }
                }

                Owner owner = new Owner(ownerId,
                        name,
                        dob,
                        phoneNumber,
                        ownedPropertyList,
                        rentalAgreementList);

                ownerHashMap.put(ownerId, owner);

            }
        } catch (ParseException e) {
            e.printStackTrace();
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
                    writer.write(line.toString());
                }
                writer.write(" | ");
                if (tenant.getPaymentRecords() != null) {
                    StringBuilder line = new StringBuilder();
                    for (Payment payment : tenant.getPaymentRecords()) {
                        line.append(payment.getId()).append(",");
                    }
                    writer.write(line.toString());
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
                    writer.write(line.toString());
                }
                writer.write(" | ");
                if (host.getRentalAgreements() != null) {
                    StringBuilder line = new StringBuilder();
                    for (RentalAgreement rentalAgreement : host.getRentalAgreements()) {
                        line.append(rentalAgreement.getId()).append(",");
                    }
                    writer.write(line.toString());
                }
                writer.write(" | ");
                if (host.getCooperatingOwners() != null) {
                    StringBuilder line = new StringBuilder();
                    for (Person cooperatingOwner : host.getCooperatingOwners()) {
                        line.append(cooperatingOwner.getId()).append(",");
                    }
                    writer.write(line.toString());
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
                if (property instanceof CommercialProperty) {
                    CommercialProperty commercialProperty = (CommercialProperty) property;
                    writer.write(commercialProperty.getId() + " | CommercialProperty | " + commercialProperty.getAddress() + " | " +
                            commercialProperty.getPrice() + " | " + commercialProperty.getStatus() + " | " + commercialProperty.getOwner().getId() + " | ");
                    if (commercialProperty.getHostList() != null) {
                        StringBuilder hostIds = new StringBuilder();
                        for (Host host : commercialProperty.getHostList()) {
                            hostIds.append(host.getId()).append(",");
                        }
                        writer.write(hostIds.toString());
                    }
                    writer.write(" | BusinessType:" + commercialProperty.getBusinessType() +
                            " | ParkingSpace:" + commercialProperty.getParkingSpace() +
                            " | SquareFootage:" + commercialProperty.getSquareFootage());
                } else if (property instanceof ResidentialProperty) {
                    ResidentialProperty residentialProperty = (ResidentialProperty) property;
                    writer.write(residentialProperty.getId() + " | ResidentialProperty | " + residentialProperty.getAddress() + " | " +
                            residentialProperty.getPrice() + " | " + residentialProperty.getStatus() + " | " + residentialProperty.getOwner().getId() + " | ");
                    if (residentialProperty.getHostList() != null) {
                        StringBuilder hostIds = new StringBuilder();
                        for (Host host : residentialProperty.getHostList()) {
                            hostIds.append(host.getId()).append(",");
                        }
                        writer.write(hostIds.toString());
                    }
                    writer.write(" | Bedrooms:" + residentialProperty.getNumOfBedrooms() +
                            " | Garden:" + residentialProperty.isGardenAvailability() +
                            " | PetFriendly:" + residentialProperty.isPetFriendliness());
                }
                writer.newLine();
            }
            writer.flush();
        }
    }


    // owners.txt: OwnerID | FullName | DateOfBirth | ContactInfo | OwnedProperties (comma-separated) | RentalAgreementIds (comma-separated)
    public void saveOwnerToFile(HashMap<String, Owner> ownerHashMap) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(OWNER_DIRECTORY.toFile()))) {
            for (Owner owner : ownerHashMap.values()) {
                writer.write(owner.getId() + " | " + owner.getName() + " | " + owner.getDob() + " | " + owner.getPhoneNumber());
                if (owner.getOwnedProperties() != null ) {
                    StringBuilder line = new StringBuilder();
                    for (Property property : owner.getOwnedProperties()) {
                        line.append(property.getId()).append(",");
                    }
                    writer.write(line.toString());
                }
                if (owner.getRentalAgreements() != null) {
                    StringBuilder line = new StringBuilder();
                    for (RentalAgreement rentalAgreement : owner.getRentalAgreements()) {
                        line.append(rentalAgreement.getId()).append(",");
                    }
                    writer.write(line.toString());
                }
                writer.newLine();
            }
            writer.flush();
        }
    }
}
