package adminOperatorManager;

import rentalmanager.RentalManager;
import java.util.HashMap;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public interface AdminOperatorManager {
    void handleDeleteAllData(HashMap<String, HashMap<String, ?>> dataMap);
    void handleAddAgreement(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap);
    void handleUpdateAgreement(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap);
    void handleDeleteAgreement(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap);
    void handleGetOne(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap);
    void handleGetAll(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap);
    void handleGetBy(RentalManager rentalManager, HashMap<String, HashMap<String, ?>> dataMap);
    void handleGetAssociatedEntities(HashMap<String, HashMap<String, ?>> dataMap);
}
