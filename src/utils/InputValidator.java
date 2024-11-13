package utils;

import java.util.regex.Pattern;
import java.util.*;
import domain.*;

public class InputValidator {
    private static final Pattern RENTAL_AGREEMENT_ID_PATTERN = Pattern.compile("^RA\\d{3}$");
    private static final Pattern RESIDENTIAL_PROPERTY_ID_PATTERN = Pattern.compile("^RP\\d{3}$");
    private static final Pattern COMMERCIAL_PROPERTY_ID_PATTERN = Pattern.compile("^CP\\d{3}$");
    private static final Pattern TENANT_ID_PATTERN = Pattern.compile("^T\\d{3}$");
    private static final Pattern HOST_ID_PATTERN = Pattern.compile("^H\\d{3}$");
    private static final Pattern OWNER_ID_PATTERN = Pattern.compile("^O\\d{3}$");
    private static final Pattern PAYMENT_ID_PATTERN = Pattern.compile("^PAY\\d{3}$");
    private static final Pattern BASIC_ADDRESS_PATTERN = Pattern.compile("^[a-zA-Z0-9.,\\s]+$");


    public static boolean isValidPhoneNumber(String phoneNumber) {
        String phonePattern = "^\\+?[0-9-]+$";
        return phoneNumber != null && Pattern.matches(phonePattern, phoneNumber);
    }

    /**
     * Validate the format of a RentalAgreementID.
     */
    public static boolean isValidRentalAgreementIDFormat(String rentalAgreementId) {
        return RENTAL_AGREEMENT_ID_PATTERN.matcher(rentalAgreementId).matches();
    }

    /**
     * Check if RentalAgreementID is unique.
     */
    public static boolean isUniqueRentalAgreementID(String rentalAgreementId, HashMap<String, RentalAgreement> rentalHashMap) {
        return !rentalHashMap.containsKey(rentalAgreementId);
    }

    /**
     * Validate the format of a PropertyID.
     */
    public static boolean isValidResidentialPropertyID(String propertyId) {
        return RESIDENTIAL_PROPERTY_ID_PATTERN.matcher(propertyId).matches();
    }

    public static boolean isValidCommercialPropertyID(String propertyId) {
        return COMMERCIAL_PROPERTY_ID_PATTERN.matcher(propertyId).matches();
    }

    /**
     * Check if PropertyID is unique.
     */
    public static boolean isUniquePropertyID(String propertyId, HashMap<String, Property> propertyHashMap) {
        return !propertyHashMap.containsKey(propertyId);
    }

    /**
     * Validate the format of a TenantID.
     */
    public static boolean isValidTenantIDFormat(String tenantId) {
        return TENANT_ID_PATTERN.matcher(tenantId).matches();
    }

    /**
     * Check if TenantID is unique.
     */
    public static boolean isUniqueTenantID(String tenantId, HashMap<String, Tenant> tenantHashMap) {
        return !tenantHashMap.containsKey(tenantId);
    }

    /**
     * Validate the format of a HostID.
     */
    public static boolean isValidHostIDFormat(String hostId) {
        return HOST_ID_PATTERN.matcher(hostId).matches();
    }

    /**
     * Check if HostID is unique.
     */
    public static boolean isUniqueHostID(String hostId, HashMap<String, Host> hostHashMap) {
        return !hostHashMap.containsKey(hostId);
    }

    /**
     * Validate the format of an OwnerID.
     */
    public static boolean isValidOwnerIDFormat(String ownerId) {
        return OWNER_ID_PATTERN.matcher(ownerId).matches();
    }

    /**
     * Check if OwnerID is unique.
     */
    public static boolean isUniqueOwnerID(String ownerId, HashMap<String, Owner> ownerHashMap) {
        return !ownerHashMap.containsKey(ownerId);
    }

    /**
     * Validate the format of a PaymentID.
     */
    public static boolean isValidPaymentIDFormat(String paymentId) {
        return PAYMENT_ID_PATTERN.matcher(paymentId).matches();
    }

    /**
     * Check if PaymentID is unique.
     */
    public static boolean isUniquePaymentID(String paymentId, HashMap<String, Payment> paymentHashMap) {
        return !paymentHashMap.containsKey(paymentId);
    }

    // Additional utility methods to validate lists of IDs if needed
    /**
     * Validate Referential Integrity for SubTenantIDs (i.e., all subtenant IDs exist in the tenantHashMap).
     */
    public static boolean isValidSubTenantIDs(List<String> subTenantIds, HashMap<String, Tenant> tenantHashMap) {
        for (String subTenantId : subTenantIds) {
            if (!tenantHashMap.containsKey(subTenantId)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validate Referential Integrity for HostIDs in Property (i.e., all host IDs exist in the hostHashMap).
     */
    public static boolean isValidHostIDs(List<String> hostIds, HashMap<String, Host> hostHashMap) {
        for (String hostId : hostIds) {
            if (!hostHashMap.containsKey(hostId)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidAddress(String address) {
        // Check if the address is null or empty
        if (address == null || address.trim().isEmpty()) {
            System.out.println("Address cannot be empty.");
            return false;
        }

        // Check length of the address
        if (address.length() < 5 || address.length() > 100) {
            System.out.println("Address must be between 5 and 100 characters.");
            return false;
        }

        // Check basic character requirements
        if (!BASIC_ADDRESS_PATTERN.matcher(address).matches()) {
            System.out.println("Address contains invalid characters.");
            return false;
        }

        return true;
    }

}
