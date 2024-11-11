package domain;

import java.util.Date;

public class Payment {
    private String id;
    private RentalAgreement rentalAgreement;
    private Tenant mainTenant;
    private Double amount;
    private Date date;
    private String paymentMethod;

    public Payment(){
        this.id = "Default";
        this.rentalAgreement = null;
        this.mainTenant = null;
        this.amount = 0.0;
        this.date = null;
        this.paymentMethod = "Default";
    }
    public Payment(String id, RentalAgreement rentalAgreement, Tenant mainTenant, Double amount, Date date, String paymentMethod) {
        this.id = id;
        this.rentalAgreement = rentalAgreement;
        this.mainTenant = mainTenant;
        this.amount = amount;
        this.date = date;
        this.paymentMethod = paymentMethod;
    }

    public String getId() {return id;}
    public void setId(String id) {this.id = id;}
    public RentalAgreement getRentalAgreement() {return rentalAgreement;}
    public void setRentalAgreement(){this.rentalAgreement = this.rentalAgreement;}
    public Tenant getMainTenant() {return mainTenant;}
    public void setMainTenant(Tenant mainTenant) {this.mainTenant = mainTenant;}
    public Double getAmount() {return amount;}
    public void setAmount(Double amount) {this.amount = amount;}
    public Date getDate() {return date;}
    public void setDate(Date date) {this.date = date;}
    public String getPaymentMethod() {return paymentMethod;}
    public void setPaymentMethod(String paymentMethod) {this.paymentMethod = paymentMethod;}
}
