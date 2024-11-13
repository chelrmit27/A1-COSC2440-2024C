package domain;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Tenant extends Person{
    private List<RentalAgreement> rentalAgreements;
    private List<Property> currentRentalProperties;
    private List<Payment> paymentRecords;

    public Tenant() {
        super();
        rentalAgreements = new ArrayList<RentalAgreement>();
        currentRentalProperties = new ArrayList<Property>();
        paymentRecords = new ArrayList<Payment>();
    }

    public Tenant(String id, String name, Date dob, String phoneNumber){
        super(id, name, dob, phoneNumber);
        rentalAgreements = new ArrayList<RentalAgreement>();
        currentRentalProperties = new ArrayList<Property>();
        paymentRecords = new ArrayList<Payment>();
    }

    public Tenant(String id, String name, Date dob, String phoneNumber, List<RentalAgreement> rentalAgreements){
        super(id, name, dob, phoneNumber);
        this.rentalAgreements = rentalAgreements;
        currentRentalProperties = new ArrayList<Property>();
        paymentRecords = new ArrayList<Payment>();
    }

    public Tenant(String id, String name, Date dob, String phoneNumber, List<RentalAgreement> rentalAgreements, List<Payment> paymentRecords){
        super(id, name, dob, phoneNumber);
        this.rentalAgreements = rentalAgreements;
        currentRentalProperties = new ArrayList<Property>();
        this.paymentRecords = paymentRecords;
    }

    public boolean rentProperty(Property property){
        // checking is done somewhere else
        currentRentalProperties.add(property);
        return true;
    }

    public boolean removeProperty(Property property){
        // checking is done somewhere else
        currentRentalProperties.remove(property);
        return true;
    }

    public List<RentalAgreement> getRentalAgreements() {return rentalAgreements;}
    public List<Property> getCurrentRentalProperties() {return currentRentalProperties;}
    public List<Payment> getPaymentRecords() {return paymentRecords;}

    public boolean addRentalAgreement(RentalAgreement rentalAgreement){
        // checking later
        rentalAgreements.add(rentalAgreement);
        return true;
    }
    public boolean removeRentalAgreement(RentalAgreement rentalAgreement){
        // checking later
        rentalAgreements.remove(rentalAgreement);
        return true;
    }
}
