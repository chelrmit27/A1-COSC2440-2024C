package utils;

import java.util.regex.Pattern;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Supplier;
import domain.*;

import utils.*;

public class Utils {
    static final InputValidator validator = new InputValidator();
    static final Scanner scanner = new Scanner(System.in);
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static Property createProperty(HashMap<String, HashMap<String, ?>> dataMap) {
        HashMap<String, Property> propertyHashMap = (HashMap<String, Property>) dataMap.get("properties");

        // Determine property type
        String propertyType;
        do {
            propertyType = getInput("Property Type (Residential, Commercial)");
            if (!propertyType.equalsIgnoreCase("Residential") && !propertyType.equalsIgnoreCase("Commercial")) {
                System.out.println("Invalid Property Type. Try again.");
            }
        } while (!propertyType.equalsIgnoreCase("Residential") && !propertyType.equalsIgnoreCase("Commercial"));

        // Validate Property ID based on type
        String id;
        if (propertyType.equalsIgnoreCase("Residential")) {
            do {
                id = getInput("Property ID (RP***)");
                if (!validator.isValidResidentialPropertyID(id)) {
                    System.out.println("WARNING: Invalid Residential Property ID format. Try again.");
                } else if (!validator.isUniquePropertyID(id, propertyHashMap)) {
                    System.out.println("WARNING: Duplicate Property ID. Try again.");
                }
            } while (!validator.isValidResidentialPropertyID(id) || !validator.isUniquePropertyID(id, propertyHashMap));
        } else {
            do {
                id = getInput("Property ID (CP***)");
                if (!validator.isValidCommercialPropertyID(id)) {
                    System.out.println("WARNING: Invalid Commercial Property ID format. Try again.");
                } else if (!validator.isUniquePropertyID(id, propertyHashMap)) {
                    System.out.println("WARNING: Duplicate Property ID. Try again.");
                }
            } while (!validator.isValidCommercialPropertyID(id) || !validator.isUniquePropertyID(id, propertyHashMap));
        }

        // Address
        String address;
        do {
            address = getInput("Address");
            if (!validator.isValidAddress(address)) {
                System.out.println("WARNING: Invalid Address. Try again.");
            }
        } while (!validator.isValidAddress(address));

        // Price
        double price = getPositiveDouble("Price");

        // Status
        PropertyStatus status = getEnumInput(PropertyStatus.class, "Status (AVAILABLE, RENTED, UNDER_MAINTENANCE)");

        // Owner and Hosts
        Owner owner = createOwner(dataMap);
        List<Host> hostList = createList(() -> createHost(dataMap));

        if (propertyType.equalsIgnoreCase("Residential")) {
            // Number of Bedrooms
            int numOfBedrooms = (int) getPositiveDouble("Number of Bedrooms");

            // Garden Availability
            Boolean garden = getYesNoInput("Garden Availability (yes/no)");

            // Pet Friendliness
            Boolean petFriendly = getYesNoInput("Pet Friendliness (yes/no)");

            return new ResidentialProperty(id, address, price, status, owner, hostList, numOfBedrooms, garden, petFriendly);
        } else {
            // Business Type
            String businessType = getInput("Business Type");

            // Parking Space
            int parkingSpaces = (int) getPositiveDouble("Parking Spaces");

            // Square Footage
            double squareFootage = getPositiveDouble("Square Footage");

            return new CommercialProperty(id, address, price, status, owner, hostList, businessType, parkingSpaces, squareFootage);
        }
    }


    // Helper method for yes/no input
    private static Boolean getYesNoInput(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.next().trim().toLowerCase();
            if (input.equals("yes")) return true;
            if (input.equals("no")) return false;
            System.out.println("WARNING: Invalid input. Please enter 'yes' or 'no'.");
        } while (true);
    }

    public static Owner createOwner(HashMap<String, HashMap<String, ?>> dataMap) {
        // Necessary HashMaps
        HashMap<String, Owner> ownerHashMap = (HashMap<String, Owner>) dataMap.get("owners");

        // Owner ID
        String id;
        do {
            id = getInput("Owner ID (O***)");
            if (!validator.isValidOwnerIDFormat(id)) {
                System.out.println("WARNING: Invalid Owner ID format. Try again.");
            } else if (!validator.isUniqueOwnerID(id, ownerHashMap)) {
                System.out.println("WARNING: Duplicate Owner ID. Try again.");
            }
        } while (!validator.isValidOwnerIDFormat(id) || !validator.isUniqueOwnerID(id, ownerHashMap));

        // Name
        String name = getInput("Owner Name");

        // Date of Birth
        Date dob = getDateInput("Owner DOB (dd/MM/yyyy)");

        // Phone Number
        String phoneNumber = getValidatedPhoneNumber();

        // Owned Properties
        List<Property> ownedProperties = createList(() -> createProperty(dataMap));

        // Rental Agreements
        List<RentalAgreement> rentalAgreements = createList(() -> createRentalAgreement(dataMap));

        return new Owner(id, name, dob, phoneNumber, ownedProperties, rentalAgreements);
    }


    public static Host createHost(HashMap<String, HashMap<String, ?>> dataMap) {
        // Necessary HashMaps
        HashMap<String, Host> hostHashMap = (HashMap<String, Host>) dataMap.get("hosts");

        // Host ID
        String id;
        do {
            id = getInput("Host ID (H***)");
            if (!validator.isValidHostIDFormat(id)) {
                System.out.println("WARNING: Invalid Host ID format. Try again.");
            } else if (!validator.isUniqueHostID(id, hostHashMap)) {
                System.out.println("WARNING: Duplicate Host ID. Try again.");
            }
        } while (!validator.isValidHostIDFormat(id) || !validator.isUniqueHostID(id, hostHashMap));

        // Name
        String name = getInput("Host Name");

        // Date of Birth
        Date dob = getDateInput("Host DOB (dd/MM/yyyy)");

        // Phone Number
        String phoneNumber = getValidatedPhoneNumber();

        // Managed Properties
        List<Property> managedProperties = createList(() -> createProperty(dataMap));

        // Cooperating Owners
        List<Owner> cooperatingOwners = createList(() -> createOwner(dataMap));

        // Rental Agreements
        List<RentalAgreement> rentalAgreements = createList(() -> createRentalAgreement(dataMap));

        return new Host(id, name, dob, phoneNumber, managedProperties, cooperatingOwners, rentalAgreements);
    }


    public static Tenant createTenant(HashMap<String, HashMap<String, ?>> dataMap) {
        // Necessary HashMaps
        HashMap<String, Tenant> tenantHashMap = (HashMap<String, Tenant>) dataMap.get("tenants");

        // Tenant ID
        String id;
        do {
            id = getInput("Tenant ID (T***)");
            if (!validator.isValidTenantIDFormat(id)) {
                System.out.println("WARNING: Invalid Tenant ID format. Try again.");
            } else if (!validator.isUniqueTenantID(id, tenantHashMap)) {
                System.out.println("WARNING: Duplicate Tenant ID. Try again.");
            }
        } while (!validator.isValidTenantIDFormat(id) || !validator.isUniqueTenantID(id, tenantHashMap));

        // Tenant Name
        String name = getInput("Tenant Name");

        // Date of Birth
        Date dob = getDateInput("Tenant DOB (dd/MM/yyyy)");

        // Phone Number
        String phoneNumber = getValidatedPhoneNumber();

        // Rental Agreements
        List<RentalAgreement> rentalAgreements = createList(() -> createRentalAgreement(dataMap));

        // Payment Records
        List<Payment> paymentRecords = createList(() -> createPayment(dataMap));

        return new Tenant(id, name, dob, phoneNumber, rentalAgreements, paymentRecords);
    }


    public static RentalAgreement createRentalAgreement(HashMap<String, HashMap<String, ?>> dataMap) {
        // Necessary HashMaps
        HashMap<String, RentalAgreement> rentalHashMap = (HashMap<String, RentalAgreement>) dataMap.get("rental_agreements");

        // Rental Agreement ID
        String id;
        do {
            id = getInput("Rental Agreement ID (RA***)");
            if (!validator.isValidRentalAgreementIDFormat(id)) {
                System.out.println("WARNING: Invalid Rental Agreement ID format. Try again.");
            } else if (!validator.isUniqueRentalAgreementID(id, rentalHashMap)) {
                System.out.println("WARNING: Duplicate Rental Agreement ID. Try again.");
            }
        } while (!validator.isValidRentalAgreementIDFormat(id) || !validator.isUniqueRentalAgreementID(id, rentalHashMap));

        // Property, Owner, Host, and Main Tenant
        Property property = createProperty(dataMap);
        Owner owner = createOwner(dataMap);
        Host host = createHost(dataMap);
        Tenant mainTenant = createTenant(dataMap);

        // SubTenant List
        List<Tenant> subTenants = createList(() -> createTenant(dataMap));

        // Period (String)
        String period = getInput("Period");

        // Contract Date
        Date contractDate = getDateInput("Contract Date (dd/MM/yyyy)");

        // Renting Fee
        double rentingFee = getPositiveDouble("Renting Fee");

        // Rental Status
        RentalStatus status = getEnumInput(RentalStatus.class, "Rental Status (NEW, ACTIVE, COMPLETED)");

        return new RentalAgreement(id, property, owner, host, mainTenant, subTenants, period, contractDate, rentingFee, status);
    }


    public static Payment createPayment(HashMap<String, HashMap<String, ?>> dataMap) {
        // Necessary HashMaps
        HashMap<String, Payment> paymentHashMap = (HashMap<String, Payment>) dataMap.get("payments");

        // Payment ID
        String id;
        do {
            id = getInput("Payment ID (PAY***)");
            if (!validator.isValidPaymentIDFormat(id)) {
                System.out.println("WARNING: Invalid Payment ID format. Try again.");
            } else if (!validator.isUniquePaymentID(id, paymentHashMap)) {
                System.out.println("WARNING: Duplicate Payment ID. Try again.");
            }
        } while (!validator.isValidPaymentIDFormat(id) || !validator.isUniquePaymentID(id, paymentHashMap));

        // Rental Agreement and Main Tenant
        RentalAgreement rentalAgreement = createRentalAgreement(dataMap);
        Tenant mainTenant = createTenant(dataMap);

        // Payment Amount
        double amount = getPositiveDouble("Payment Amount");

        // Payment Date
        Date date = getDateInput("Payment Date (dd/MM/yyyy)");

        // Payment Method
        String paymentMethod = getInput("Payment Method");

        return new Payment(id, rentalAgreement, mainTenant, amount, date, paymentMethod);
    }


    // Generic method to create a list of any type T
    public static <T> List<T> createList(Supplier<T> creator) {
        List<T> list = new ArrayList<>();
        String continueAdding;

        do {
            T item = creator.get(); // Use the supplier to create a new item
            if (item != null) {
                list.add(item);
            }

            // Ask if the user wants to add another item
            System.out.print("Do you want to add another item? (yes/no): ");
            continueAdding = scanner.nextLine().trim().toLowerCase();

        } while (continueAdding.equals("yes"));

        return list;
    }

    // Helper methods for input validation
    public static String getInput(String prompt) {
        String input;
        do {
            System.out.print("> Enter " + prompt + ": ");
            input = scanner.nextLine().trim();  // Trim whitespace here
            if (input.isEmpty()) {
                System.out.println("WARNING: " + prompt + " cannot be empty. Try again.");
            }
        } while (input.isEmpty());
        return input;
    }

    public static Date getDateInput(String prompt) {
        Date date = null;
        do {
            System.out.print("> Enter " + prompt + ": ");
            String dateStr = scanner.nextLine().trim();  // Trim whitespace here
            try {
                date = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("WARNING: Invalid date format. Try again.");
            }
        } while (date == null);
        return date;
    }

    public static String getValidatedPhoneNumber() {
        String phoneNumber;
        do {
            System.out.print("> Enter Phone Number: ");
            phoneNumber = scanner.nextLine().trim();  // Trim whitespace here
            if (!validator.isValidPhoneNumber(phoneNumber)) {
                System.out.println("WARNING: Invalid phone number format. Try again.");
            }
        } while (!validator.isValidPhoneNumber(phoneNumber));
        return phoneNumber;
    }

    public static double getPositiveDouble(String prompt) {
        double value;
        do {
            System.out.print("> Enter " + prompt + ": ");
            value = scanner.nextDouble();
            if (value <= 0) {
                System.out.println("WARNING: " + prompt + " must be positive. Try again.");
            }
        } while (value <= 0);
        scanner.nextLine(); // clear scanner buffer
        return value;
    }

    public static <E extends Enum<E>> E getEnumInput(Class<E> enumType, String prompt) {
        E value = null;
        do {
            System.out.print("> Enter " + prompt + ": ");
            try {
                value = Enum.valueOf(enumType, scanner.nextLine().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("WARNING: Invalid " + prompt + ". Try again.");
            }
        } while (value == null);
        return value;
    }

    /*
    public void exampleUsage() {
    HashMap<String, HashMap<String, ?>> dataMap = new HashMap<>(); // Assuming dataMap is populated

    // Create a list of Hosts
    List<Host> hostList = createList(() -> createHost(dataMap));

    // Similarly, you can create lists for other entities:
    // List<Tenant> tenantList = createList(() -> createTenant(dataMap));
    // List<Property> propertyList = createList(() -> createProperty(dataMap));

    // Print the host list to verify
    System.out.println("Hosts created:");
    for (Host host : hostList) {
        System.out.println(host);
    }
}
     */

}
