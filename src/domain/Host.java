package domain;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Host extends Person {
    private List<Property> managedProperty;
    private List<Owner> cooperatingOwners;
    private List<RentalAgreement> rentalAgreements;

    public Host() {
        super();
        managedProperty = new ArrayList<Property>();
        cooperatingOwners = new ArrayList<Owner>();
        rentalAgreements = new ArrayList<RentalAgreement>();
    }

    public Host(String id, String name, Date dob, String phoneNumber) {
        super(id, name, dob, phoneNumber);
        managedProperty = new ArrayList<Property>();
        cooperatingOwners = new ArrayList<Owner>();
        rentalAgreements = new ArrayList<RentalAgreement>();
    }

    public Host(String id, String name, Date dob, String phoneNumber,
                List<Property> managedProperty,
                List<Owner> cooperatingOwners,
                List<RentalAgreement> rentalAgreements) {
        super(id, name, dob, phoneNumber);
        this.managedProperty = managedProperty;
        this.cooperatingOwners = cooperatingOwners;
        this.rentalAgreements = rentalAgreements;
    }

    public List<Property> getManagedProperty() {return managedProperty;}
    public List<Owner> getCooperatingOwners() {return cooperatingOwners;}
    public List<RentalAgreement> getRentalAgreements() {return rentalAgreements;}
    public void setManagedProperty(List<Property> managedProperty) {this.managedProperty = managedProperty;}
    public void setCooperatingOwners(List<Owner> cooperatingOwners) {this.cooperatingOwners = cooperatingOwners;}
    public void setRentalAgreements(List<RentalAgreement> rentalAgreements) {this.rentalAgreements = rentalAgreements;}
}
