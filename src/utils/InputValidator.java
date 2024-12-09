package utils;

import domain.*;

import java.util.regex.Pattern;
import java.util.*;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class InputValidator {
    public static final Pattern RENTAL_AGREEMENT_ID_PATTERN = Pattern.compile("^RA\\d{3}$");
    public static final Pattern RESIDENTIAL_PROPERTY_ID_PATTERN = Pattern.compile("^RP\\d{3}$");
    public static final Pattern COMMERCIAL_PROPERTY_ID_PATTERN = Pattern.compile("^CP\\d{3}$");
    public static final Pattern TENANT_ID_PATTERN = Pattern.compile("^T\\d{3}$");
    public static final Pattern HOST_ID_PATTERN = Pattern.compile("^H\\d{3}$");
    public static final Pattern OWNER_ID_PATTERN = Pattern.compile("^O\\d{3}$");
    public static final Pattern PAYMENT_ID_PATTERN = Pattern.compile("^PAY\\d{3}$");
    public static final Pattern BASIC_ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9.,\\s]+$");

    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phonePattern = "^\\+?[0-9-]+$";
        return phoneNumber != null && Pattern.matches(phonePattern, phoneNumber);
    }

    public static boolean isValidRentalAgreementIDFormat(String rentalAgreementId) {
        return RENTAL_AGREEMENT_ID_PATTERN.matcher(rentalAgreementId).matches();
    }

    public static boolean isUniqueRentalAgreementID(String rentalAgreementId, HashMap<String, RentalAgreement> rentalHashMap) {
        return !rentalHashMap.containsKey(rentalAgreementId);
    }

    public static boolean isValidResidentialPropertyID(String propertyId) {
        return RESIDENTIAL_PROPERTY_ID_PATTERN.matcher(propertyId).matches();
    }

    public static boolean isValidCommercialPropertyID(String propertyId) {
        return COMMERCIAL_PROPERTY_ID_PATTERN.matcher(propertyId).matches();
    }

    public static boolean isUniquePropertyID(String propertyId, HashMap<String, Property> propertyHashMap) {
        return !propertyHashMap.containsKey(propertyId);
    }

    public static boolean isValidTenantIDFormat(String tenantId) {
        return TENANT_ID_PATTERN.matcher(tenantId).matches();
    }

    public static boolean isUniqueTenantID(String tenantId, HashMap<String, Tenant> tenantHashMap) {
        return !tenantHashMap.containsKey(tenantId);
    }

    public static boolean isValidHostIDFormat(String hostId) {
        return HOST_ID_PATTERN.matcher(hostId).matches();
    }

    public static boolean isUniqueHostID(String hostId, HashMap<String, Host> hostHashMap) {
        return !hostHashMap.containsKey(hostId);
    }

    public static boolean isValidOwnerIDFormat(String ownerId) {
        return OWNER_ID_PATTERN.matcher(ownerId).matches();
    }

    public static boolean isUniqueOwnerID(String ownerId, HashMap<String, Owner> ownerHashMap) {
        return !ownerHashMap.containsKey(ownerId);
    }

    public static boolean isValidPaymentIDFormat(String paymentId) {
        return PAYMENT_ID_PATTERN.matcher(paymentId).matches();
    }

    public static boolean isUniquePaymentID(String paymentId, HashMap<String, Payment> paymentHashMap) {
        return !paymentHashMap.containsKey(paymentId);
    }

    public static boolean isValidSubTenantIDs(List<String> subTenantIds, HashMap<String, Tenant> tenantHashMap) {
        for (String subTenantId : subTenantIds) {
            if (!tenantHashMap.containsKey(subTenantId)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidHostIDs(List<String> hostIds, HashMap<String, Host> hostHashMap) {
        for (String hostId : hostIds) {
            if (!hostHashMap.containsKey(hostId)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidAddress(String address) {
        if (address == null || address.trim().isEmpty()) {
            System.out.println("Address cannot be empty.");
            return false;
        }

        if (address.length() < 5 || address.length() > 100) {
            System.out.println("Address must be between 5 and 100 characters.");
            return false;
        }

        if (!BASIC_ADDRESS_PATTERN.matcher(address).matches()) {
            System.out.println("Address contains invalid characters.");
            return false;
        }
        return true;
    }

}
