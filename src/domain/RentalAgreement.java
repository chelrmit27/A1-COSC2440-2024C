package domain;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.text.SimpleDateFormat;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class RentalAgreement {
    private String id;
    private Property property;
    private Tenant mainTenant;
    private List<Tenant> subTenants;
    private LeasePeriod period;
    private Date contractDate;
    private Double rentingFee;
    private RentalStatus status;

    public RentalAgreement() {
        this.id = null;
        this.property = null;
        this.mainTenant = null;
        this.subTenants = new ArrayList<>();
        this.period = null;
        this.contractDate = null;
        this.rentingFee = null;
        this.status = RentalStatus.NEW;
    }

    public RentalAgreement(String id, Property property,
                           Tenant mainTenant, List<Tenant> subTenants,
                           LeasePeriod period, Date contractDate, Double rentingFee, RentalStatus status){
        this.id = id;
        this.property = property;
        this.mainTenant = mainTenant;
        this.subTenants = (subTenants != null) ? subTenants : new ArrayList<>();
        this.period = period;
        this.contractDate = contractDate;
        this.rentingFee = rentingFee;
        this.status = status;
    }

    public String getId() { return id; }
    public Property getProperty() { return property; }
    public Tenant getMainTenant() { return mainTenant; }
    public List<Tenant> getSubTenants() { return subTenants; }
    public LeasePeriod getLeasePeriod() { return period; }
    public LeasePeriod getPeriod() { return period; }
    public Date getContractDate() { return contractDate; }
    public Double getRentingFee() { return rentingFee; }
    public RentalStatus getStatus() { return status; }

    public void setId(String id) { this.id = id; }
    public void setProperty(Property property) { this.property = property; }
    public void setMainTenant(Tenant mainTenant) { this.mainTenant = mainTenant; }
    public void setSubTenants(List<Tenant> subTenants) { this.subTenants = subTenants; }
    public void setPeriod(LeasePeriod period) { this.period = period; }
    public void setContractDate(Date contractDate) { this.contractDate = contractDate; }
    public void setRentingFee(Double rentingFee) { this.rentingFee = rentingFee; }
    public void setStatus(RentalStatus status) { this.status = status; }

    public void printAgreement() {
        // Prepare values for display
        String propertyId = (property != null) ? property.getId() : "N/A";
        String mainTenantId = (mainTenant != null) ? mainTenant.getId() : "N/A";

        // Convert subTenant IDs to a comma-separated string, or "N/A" if the list is null or empty
        String subTenantIds = subTenants != null && !subTenants.isEmpty()
                ? subTenants.stream()
                .map(Tenant::getId)
                .filter(Objects::nonNull)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        String periodStr = (period != null) ? period.toString() : "N/A";
        String contractDateStr = (contractDate != null) ? new SimpleDateFormat("dd/MM/yyyy").format(contractDate) : "N/A";
        double rentingFeeValue = (rentingFee != null) ? rentingFee : 0.0;
        String statusStr = (status != null) ? status.toString() : "N/A";

        // Print formatted agreement details
        System.out.printf("%-18s %-12s %-15s %-30s %-12s %-15s %-15.2f %-10s%n",
                id, propertyId, mainTenantId, subTenantIds, periodStr, contractDateStr, rentingFeeValue, statusStr);
    }

    public void printAgreement(StringBuilder content) {
        // Prepare values for display
        String propertyId = (property != null) ? property.getId() : "N/A";
        String mainTenantId = (mainTenant != null) ? mainTenant.getId() : "N/A";

        // Convert subTenant IDs to a comma-separated string, or "N/A" if the list is null or empty
        String subTenantIds = subTenants != null && !subTenants.isEmpty()
                ? subTenants.stream()
                .map(Tenant::getId)
                .filter(Objects::nonNull)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        String periodStr = (period != null) ? period.toString() : "N/A";
        String contractDateStr = (contractDate != null) ? new SimpleDateFormat("dd/MM/yyyy").format(contractDate) : "N/A";
        double rentingFeeValue = (rentingFee != null) ? rentingFee : 0.0;
        String statusStr = (status != null) ? status.toString() : "N/A";

        // Print formatted agreement details
        content.append(String.format("%-18s %-12s %-15s %-30s %-12s %-15s %-15.2f %-10s%n",
                id, propertyId, mainTenantId, subTenantIds, periodStr, contractDateStr, rentingFeeValue, statusStr));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RentalAgreement that = (RentalAgreement) obj;
        return id != null && id.equals(that.id); // Compare based on ID
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
