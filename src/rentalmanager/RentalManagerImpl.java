package rentalmanager;

import domain.RentalAgreement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RentalManagerImpl implements RentalManager {
    private HashMap<String, RentalAgreement> rentalHashMap;

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
}
