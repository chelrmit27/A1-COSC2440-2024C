package rentalmanager;

import domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class RentalManagerImpl implements RentalManager {
    private HashMap<String, RentalAgreement> rentalHashMap;

    public RentalManagerImpl() {
        // Initialize rentalHashMap to prevent null issues
        this.rentalHashMap = new HashMap<>();
    }

    @Override
    public void addAgreement(RentalAgreement rentalAgreement) {
        if (this.rentalHashMap == null) {
            this.rentalHashMap = new HashMap<>();
        }
        this.rentalHashMap.put(rentalAgreement.getId(), rentalAgreement);
    }

    @Override
    public void addAll(HashMap<String, RentalAgreement> rentalHashMap) {
        this.rentalHashMap = rentalHashMap;
    }

    @Override
    public boolean updateAgreement(RentalAgreement rentalAgreement) {
        if (this.rentalHashMap.containsKey(rentalAgreement.getId())) {
            this.rentalHashMap.put(rentalAgreement.getId(), rentalAgreement);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAgreement(String rentalId) {
        RentalAgreement agreement = this.rentalHashMap.get(rentalId);
        if (agreement != null) {
            this.rentalHashMap.remove(rentalId);
            return true;
        }
        return false;
    }

    @Override
    public RentalAgreement getOne(String rentalId) {
        RentalAgreement agreement = this.rentalHashMap.get(rentalId);
        if (agreement != null) {
            return agreement;
        }
        return null;
    }

    @Override
    public List<RentalAgreement> getAll(){
        List<RentalAgreement> rentalAgreements = new ArrayList<>(rentalHashMap.values());
        return rentalAgreements;
    }

    /**
     * Retrieves a list of rental agreements associated with a specific owner's name.
     * <p>
     * This method iterates through the provided rental agreements and checks the associated property for the owner.
     * If the owner's name matches the provided name (case-insensitive), the rental agreement is added to the result list.
     * </p>
     *
     * @param ownerName the name of the owner to filter rental agreements by
     * @param ownerHashMap a map containing owners, where the key is the owner ID and the value is the {@link Owner} object
     * @param rentalHashMap a map containing rental agreements, where the key is the rental agreement ID and the value is the {@link RentalAgreement} object
     * @return a list of {@link RentalAgreement} objects associated with the specified owner's name
     */
    @Override
    public List<RentalAgreement> getByOwnerName(String ownerName, HashMap<String, Owner> ownerHashMap, HashMap<String, RentalAgreement> rentalHashMap) {
        List<RentalAgreement> rentalToGet = new ArrayList<>();

        for (RentalAgreement rental : rentalHashMap.values()) {
            Property property = rental.getProperty();
            if (property != null){
                Owner owner = property.getOwner();
                if (owner != null && owner.getName().equalsIgnoreCase(ownerName)){
                    rentalToGet.add(rental);
                }
            }
        }

        return rentalToGet;
    }

    /**
     * Retrieves a list of rental agreements associated with a specific property address.
     * <p>
     * This method iterates through the provided rental agreements and checks the associated property.
     * If the property's address matches the provided address, the rental agreement is added to the result list.
     * </p>
     *
     * @param address the address of the property to filter rental agreements by
     * @param propertyHashMap a map containing properties, where the key is the property ID and the value is the {@link Property} object
     * @param rentalHashMap a map containing rental agreements, where the key is the rental agreement ID and the value is the {@link RentalAgreement} object
     * @return a list of {@link RentalAgreement} objects associated with the specified property address
     */
    @Override
    public List<RentalAgreement> getByPropertyAddress(String address, HashMap<String, Property> propertyHashMap, HashMap<String, RentalAgreement> rentalHashMap){
        List<RentalAgreement> rentalToGet = new ArrayList<>();

        for (RentalAgreement rental : rentalHashMap.values()) {
            Property property = rental.getProperty();
            if (property != null && property.getAddress().equals(address)) {
                rentalToGet.add(rental);
            }
        }

        return rentalToGet;
    }

    /**
     * Retrieves a list of rental agreements filtered by their rental status.
     * <p>
     * This method matches the provided status string to a valid {@link RentalStatus} enumeration.
     * It then iterates through the provided rental agreements and adds those with a matching status to the result list.
     * </p>
     *
     * @param statusStr the rental status as a string (e.g., "NEW", "ACTIVE", "COMPLETED")
     * @param rentalHashMap a map containing rental agreements, where the key is the rental agreement ID and the value is the {@link RentalAgreement} object
     * @return a list of {@link RentalAgreement} objects that match the specified status; an empty list if no matches are found or if the status is invalid
     */
    @Override
    public List<RentalAgreement> getByStatus(String statusStr, HashMap<String, RentalAgreement> rentalHashMap){
        List<RentalAgreement> rentalToGet = new ArrayList<>();
        RentalStatus status = null;

        try{
            status = RentalStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e){
            System.err.println("Invalid status: " + statusStr);
            return rentalToGet;
        }

        for (RentalAgreement rental : rentalHashMap.values()) {
            if (rental.getStatus().equals(status)) {
                rentalToGet.add(getOne(rental.getId()));
            }
        }
        return rentalToGet;
    }

}
