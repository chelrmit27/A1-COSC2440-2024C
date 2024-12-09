package domain;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.text.SimpleDateFormat;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class Tenant extends Person{
    private List<RentalAgreement> rentalAgreements;
    private List<Payment> paymentRecords;

    public Tenant() {
        super();
        rentalAgreements = new ArrayList<RentalAgreement>();
        paymentRecords = new ArrayList<Payment>();
    }

    public Tenant(String id, String name, Date dob, String phoneNumber){
        super(id, name, dob, phoneNumber);
        rentalAgreements = new ArrayList<RentalAgreement>();
        paymentRecords = new ArrayList<Payment>();
    }

    public Tenant(String id, String name, Date dob, String phoneNumber,
                  List<RentalAgreement> rentalAgreements){
        super(id, name, dob, phoneNumber);
        this.rentalAgreements = rentalAgreements;
        paymentRecords = new ArrayList<Payment>();
    }

    public Tenant(String id, String name, Date dob, String phoneNumber,
                  List<RentalAgreement> rentalAgreements,
                  List<Payment> paymentRecords) {
        super(id, name, dob, phoneNumber);
        this.rentalAgreements = rentalAgreements != null ? rentalAgreements : new ArrayList<>();
        this.paymentRecords = paymentRecords != null ? paymentRecords : new ArrayList<>();
    }

    public List<RentalAgreement> getRentalAgreements() {return rentalAgreements;}
    public List<Payment> getPaymentRecords() {return paymentRecords;}
    public void setRentalAgreements(List<RentalAgreement> rentalAgreements) {this.rentalAgreements = rentalAgreements;}
    public void setPaymentRecords(List<Payment> paymentRecords) {this.paymentRecords = paymentRecords;}

    public void printTenant() {
        // Prepare values for display
        String tenantId = (getId() != null) ? getId() : "N/A";
        String tenantName = (getName() != null) ? getName() : "N/A";
        String dobStr = (getDob() != null) ? new SimpleDateFormat("dd/MM/yyyy").format(getDob()) : "N/A";
        String phoneNumber = (getPhoneNumber() != null) ? getPhoneNumber() : "N/A";

        // Convert rental agreement IDs to a comma-separated string, or "N/A" if the list is null or empty
        String rentalAgreementIds = rentalAgreements != null && !rentalAgreements.isEmpty()
                ? rentalAgreements.stream()
                .map(RentalAgreement::getId)
                .filter(Objects::nonNull)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        // Convert payment IDs to a comma-separated string, or "N/A" if the list is null or empty
        String paymentIds = paymentRecords != null && !paymentRecords.isEmpty()
                ? paymentRecords.stream()
                .map(Payment::getId)
                .filter(Objects::nonNull)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        // Print formatted tenant details
        System.out.printf("%-10s %-20s %-15s %-15s %-30s %-30s%n",
                tenantId, tenantName, dobStr, phoneNumber, rentalAgreementIds, paymentIds);
    }

    public void printTenant(StringBuilder content) {
        // Prepare values for display
        String tenantId = (getId() != null) ? getId() : "N/A";
        String tenantName = (getName() != null) ? getName() : "N/A";
        String dobStr = (getDob() != null) ? new SimpleDateFormat("dd/MM/yyyy").format(getDob()) : "N/A";
        String phoneNumber = (getPhoneNumber() != null) ? getPhoneNumber() : "N/A";

        // Convert rental agreement IDs to a comma-separated string, or "N/A" if the list is null or empty
        String rentalAgreementIds = rentalAgreements != null && !rentalAgreements.isEmpty()
                ? rentalAgreements.stream()
                .map(RentalAgreement::getId)
                .filter(Objects::nonNull)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        // Convert payment IDs to a comma-separated string, or "N/A" if the list is null or empty
        String paymentIds = paymentRecords != null && !paymentRecords.isEmpty()
                ? paymentRecords.stream()
                .map(Payment::getId)
                .filter(Objects::nonNull)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        // Print formatted tenant details
        content.append(String.format("%-10s %-20s %-15s %-15s %-30s %-30s%n",
                tenantId, tenantName, dobStr, phoneNumber, rentalAgreementIds, paymentIds));
    }

}
