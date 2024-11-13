package domain;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Owner extends Person {
    private List<Property> ownedProperties;
    private List<RentalAgreement> rentalAgreements;

    public Owner(){
        super();
        ownedProperties = new ArrayList<Property>();
        rentalAgreements = new ArrayList<RentalAgreement>();
    }

    public Owner(String id, String name, Date dob, String phoneNumber){
        super(id, name, dob, phoneNumber);
        ownedProperties = new ArrayList<Property>();
        rentalAgreements = new ArrayList<RentalAgreement>();
    }

    public Owner(String id, String name, Date dob, String phoneNumber, List<Property> ownedProperties, List<RentalAgreement> rentalAgreements){
        super(id, name, dob, phoneNumber);
        this.ownedProperties = ownedProperties;
        this.rentalAgreements = rentalAgreements;
    }

    public List<Property> getOwnedProperties() {return ownedProperties;}
    public List<RentalAgreement> getRentalAgreements() {return rentalAgreements;}
    public void setOwnedProperties(List<Property> ownedProperties) {
        this.ownedProperties = ownedProperties;
    }
    public void setRentalAgreements(List<RentalAgreement> rentalAgreements) {
        this.rentalAgreements = rentalAgreements;
    }
}
