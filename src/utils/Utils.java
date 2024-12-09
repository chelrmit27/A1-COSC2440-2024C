package utils;

import domain.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Supplier;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class Utils {
    static final InputValidator validator = new InputValidator();
    static final Scanner scanner = new Scanner(System.in);
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    static public Property getPropertyById(HashMap<String, HashMap<String, ?>> dataMap) {
        HashMap<String, Property> propertyMap = (HashMap<String, Property>) dataMap.get("properties");
        Property property = null;
        do {
            System.out.print("> Enter Property ID: ");
            String propertyId = scanner.nextLine().trim();
            property = propertyMap.get(propertyId);
            if (property == null) {
                System.out.println("Property ID does not exist. Please try again.");
            }
        } while (property == null);
        return property;
    }

    static public Tenant getTenantById(HashMap<String, HashMap<String, ?>> dataMap) {
        HashMap<String, Tenant> tenantMap = (HashMap<String, Tenant>) dataMap.get("tenants");
        Tenant tenant = null;
        do {
            System.out.print("> Enter Tenant ID: ");
            String tenantId = scanner.nextLine().trim();
            tenant = tenantMap.get(tenantId);
            if (tenant == null) {
                System.out.println("Tenant ID does not exist. Please try again.");
            }
        } while (tenant == null);
        return tenant;
    }

    static public Host getHostById(HashMap<String, HashMap<String, ?>> dataMap) {
        HashMap<String, Host> hostMap = (HashMap<String, Host>) dataMap.get("hosts");
        Host host = null;
        do {
            System.out.print("> Enter Host ID: ");
            String hostId = scanner.nextLine().trim();
            host = hostMap.get(hostId);
            if (host == null) {
                System.out.println("Host ID does not exist. Please try again.");
            }
        } while (host == null);
        return host;
    }

    static public Owner getOwnerById(HashMap<String, HashMap<String, ?>> dataMap) {
        HashMap<String, Owner> ownerMap = (HashMap<String, Owner>) dataMap.get("owners");
        Owner owner = null;
        do {
            System.out.print("> Enter Owner ID: ");
            String ownerId = scanner.nextLine().trim();
            owner = ownerMap.get(ownerId);
            if (owner == null) {
                System.out.println("Owner ID does not exist. Please try again.");
            }
        } while (owner == null);
        return owner;
    }

    static public Payment getPaymentById(HashMap<String, HashMap<String, ?>> dataMap) {
        HashMap<String, Payment> paymentMap = (HashMap<String, Payment>) dataMap.get("payments");
        Payment payment = null;
        do {
            System.out.print("> Enter Payment ID: ");
            String paymentId = scanner.nextLine().trim();
            payment = paymentMap.get(paymentId);
            if (payment == null) {
                System.out.println("Owner ID does not exist. Please try again.");
            }
        } while (payment == null);
        return payment;
    }

    static public List<Tenant> getSubTenantsById(HashMap<String, HashMap<String, ?>> dataMap) {
        HashMap<String, Tenant> tenantMap = (HashMap<String, Tenant>) dataMap.get("tenants");
        List<Tenant> subTenants = new ArrayList<>();
        System.out.print("> Enter Sub-Tenant IDs (comma-separated, or leave empty if none): ");
        String subTenantIds = scanner.nextLine().trim();
        if (!subTenantIds.isEmpty()) {
            String[] subTenantIdArray = subTenantIds.split(",");
            for (String subTenantId : subTenantIdArray) {
                Tenant subTenant = tenantMap.get(subTenantId.trim());
                if (subTenant != null) {
                    subTenants.add(subTenant);
                } else {
                    System.out.println("Sub-Tenant ID " + subTenantId.trim() + " does not exist and will be skipped.");
                }
            }
        }
        return subTenants;
    }

    public static <T> List<T> createList(Supplier<T> creator) {
        List<T> list = new ArrayList<>();
        String continueAdding;

        do {
            T item = creator.get();
            if (item != null) {
                list.add(item);
            }

            System.out.print("Do you want to add another item? (yes/no): ");
            continueAdding = scanner.nextLine().trim().toLowerCase();

        } while (continueAdding.equals("yes"));

        return list;
    }

    public static String getInput(String prompt) {
        String input;
        do {
            System.out.print("> Enter " + prompt + ": ");
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("WARNING: " + prompt + " cannot be empty. Try again.");
            }
        } while (input.isEmpty());
        return input;
    }

    public static Date getDateInput(String prompt) {
        Date date = null;
        SimpleDateFormat primaryFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        SimpleDateFormat fallbackFormat = new SimpleDateFormat("dd/MM/yyyy");

        do {
            System.out.print("> Enter " + prompt + ": ");
            String dateStr = scanner.nextLine().trim();

            try {
                date = primaryFormat.parse(dateStr);
            } catch (ParseException e1) {
                try {
                    date = fallbackFormat.parse(dateStr);
                } catch (ParseException e2) {
                    System.out.println("WARNING: Invalid date format. Try again.");
                }
            }
        } while (date == null);

        return date;
    }

    public static String getValidatedPhoneNumber() {
        String phoneNumber;
        do {
            System.out.print("> Enter Phone Number: ");
            phoneNumber = scanner.nextLine().trim();
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
        scanner.nextLine();
        return value;
    }

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

}
