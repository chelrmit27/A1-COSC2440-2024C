package domain;

import java.util.Comparator;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class Payment{
    private String id;
    private Double amount;
    private Date date;
    private String paymentMethod;
    private Tenant mainTenant;
    private RentalAgreement rentalAgreement;

    public Payment(){
        this.id = "Default";
        this.rentalAgreement = null;
        this.mainTenant = null;
        this.amount = 0.0;
        this.date = null;
        this.paymentMethod = "Default";
    }

    public Payment(String id, Double amount, Date date, String paymentMethod) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.paymentMethod = paymentMethod;
    }

    public Payment(String id, Double amount, Date date, String paymentMethod, RentalAgreement rentalAgreement) {
        this.id = id;
        this.rentalAgreement = rentalAgreement;
        this.amount = amount;
        this.date = date;
        this.paymentMethod = paymentMethod;
    }

    public Payment(String id, Double amount, Date date, String paymentMethod, Tenant mainTenant, RentalAgreement rentalAgreement) {
        this.id = id;
        this.rentalAgreement = rentalAgreement;
        this.mainTenant = mainTenant;
        this.amount = amount;
        this.date = date;
        this.paymentMethod = paymentMethod;
    }

    public String getId() {return id;}
    public RentalAgreement getRentalAgreement() {return rentalAgreement;}
    public Tenant getMainTenant() {return mainTenant;}
    public Double getAmount() {return amount;}
    public Date getDate() {return date;}
    public String getPaymentMethod() {return paymentMethod;}
    public void setId(String id) {this.id = id;}
    public void setRentalAgreement(RentalAgreement rentalAgreement){this.rentalAgreement = rentalAgreement;}
    public void setMainTenant(Tenant mainTenant) {this.mainTenant = mainTenant;}
    public void setAmount(Double amount) {this.amount = amount;}
    public void setDate(Date date) {this.date = date;}
    public void setPaymentMethod(String paymentMethod) {this.paymentMethod = paymentMethod;}

    public static Comparator<Payment> sortByIdComparator() {
        return Comparator.comparing(Payment::getId);
    }

    public static Comparator<Payment> sortByDateComparator() {
        return Comparator.comparing(Payment::getDate);
    }

    public static Comparator<Payment> sortByPriceComparator() {
        return Comparator.comparing(Payment::getAmount);
    }

    public void printPayment() {
        // Prepare values for display
        String paymentId = (id != null) ? id : "N/A";
        String amountStr = (amount != null) ? String.format("%.2f", amount) : "N/A";
        String dateStr = (date != null) ? new SimpleDateFormat("dd/MM/yyyy").format(date) : "N/A";
        String paymentMethodStr = (paymentMethod != null) ? paymentMethod : "N/A";
        String mainTenantId = (mainTenant != null && mainTenant.getId() != null) ? mainTenant.getId() : "N/A";
        String rentalAgreementId = (rentalAgreement != null && rentalAgreement.getId() != null) ? rentalAgreement.getId() : "N/A";

        // Print formatted payment details
        System.out.printf("%-10s %-12s %-15s %-20s %-15s %-15s%n",
                paymentId, amountStr, dateStr, paymentMethodStr, mainTenantId, rentalAgreementId);
    }

    public void printPayment(StringBuilder content) {
        // Prepare values for display
        String paymentId = (id != null) ? id : "N/A";
        String amountStr = (amount != null) ? String.format("%.2f", amount) : "N/A";
        String dateStr = (date != null) ? new SimpleDateFormat("dd/MM/yyyy").format(date) : "N/A";
        String paymentMethodStr = (paymentMethod != null) ? paymentMethod : "N/A";
        String mainTenantId = (mainTenant != null && mainTenant.getId() != null) ? mainTenant.getId() : "N/A";
        String rentalAgreementId = (rentalAgreement != null && rentalAgreement.getId() != null) ? rentalAgreement.getId() : "N/A";

        // Print formatted payment details
        content.append(String.format("%-10s %-12s %-15s %-20s %-15s %-15s%n",
                paymentId, amountStr, dateStr, paymentMethodStr, mainTenantId, rentalAgreementId));
    }

}
