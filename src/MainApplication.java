import rentalmanager.*;
import domain.*;
import filemanager.*;

import java.util.*;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class MainApplication {
    private HashMap<String, HashMap<String, ?>> dataMap;
    private RentalManager rentalManager;
    private FileManager fileManager;

    public MainApplication(HashMap<String, HashMap<String, ?>> dataMap,
                           RentalManager rentalManager,
                           FileManager fileManager) {
        this.dataMap = dataMap;
        this.rentalManager = rentalManager;
        this.fileManager = fileManager;
    }

    public void start(){
        MenuManager.displayMenu();
        MenuManager.handleUserInput(rentalManager, fileManager, dataMap);
    }

    public static void main(String[] args) {
        HashMap<String, HashMap<String, ?>> dataMap = new HashMap<>();
        RentalManager rentalManager = new RentalManagerImpl();
        FileManager fileManager = new FileManagerImpl();

        try{
            dataMap = fileManager.loadFiles();
        } catch(Exception e){
            e.printStackTrace();
        }

        HashMap<String, RentalAgreement> rentalHashMap = (HashMap<String, RentalAgreement>) dataMap.get("rental_agreements");
        rentalManager.addAll(rentalHashMap);
        MainApplication app = new MainApplication(dataMap, rentalManager, fileManager);
        app.start();
    }

}
