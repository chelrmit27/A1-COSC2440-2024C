package adminOperatorManager;

import rentalmanager.RentalManager;
import java.util.HashMap;

public interface AdminOperatorManager {
    void handleAddAgreement(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap);
    void handleUpdateAgreement(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap);
    void handleDeleteAgreement(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap);
    void handleGetOne(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap);
    void handleGetAll(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap);

}
