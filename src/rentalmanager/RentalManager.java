package rentalmanager;

import domain.Owner;
import domain.Property;
import domain.RentalAgreement;

import java.util.HashMap;
import java.util.List;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public interface RentalManager {
    public void addAgreement(RentalAgreement agreement);
    public void addAll(HashMap<String, RentalAgreement> rentalHashMap);
    public boolean updateAgreement(RentalAgreement agreement);
    public boolean deleteAgreement(String rentalId);
    public RentalAgreement getOne(String rentalId);
    public List<RentalAgreement> getAll();
    public List<RentalAgreement> getByOwnerName(String ownerName, HashMap<String, Owner> ownerHashMap, HashMap<String, RentalAgreement> rentalHashMap);
    public List<RentalAgreement> getByPropertyAddress(String address, HashMap<String, Property> propertyHashMap, HashMap<String, RentalAgreement> rentalHashMap);
    public List<RentalAgreement> getByStatus(String status, HashMap<String, RentalAgreement> rentalHashMap);
}
