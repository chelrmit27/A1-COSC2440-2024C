package rentalmanager;

import domain.RentalAgreement;

import java.util.HashMap;
import java.util.List;

public interface RentalManager {
    public void addAgreement(RentalAgreement agreement);
    public void addAll(HashMap<String, RentalAgreement> rentalHashMap);
    public boolean updateAgreement(RentalAgreement agreement);
    public boolean deleteAgreement(String rentalId);
    public RentalAgreement getOne(String rentalId);
    public List<RentalAgreement> getAll();
}
