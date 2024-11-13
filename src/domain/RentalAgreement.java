package domain;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class RentalAgreement {
    private String id;
    private Property property;
    private Owner owner;
    private Host host;
    private Tenant mainTenant;
    private List<Tenant> subTenants;
    private String period;
    private Date contractDate;
    private Double rentingFee;
    private RentalStatus status;

    public RentalAgreement() {
        this.id = null;
        this.property = null;
        this.owner = null;
        this.host = null;
        this.mainTenant = null;
        this.subTenants = new ArrayList<Tenant>();
        this.period = null;
        this.contractDate = null;
        this.rentingFee = null;
        this.status = null;
    }

    public RentalAgreement(String id, Property property, Owner owner, Host host, Tenant mainTenant, List<Tenant> subTenants, String period, Date contractDate, Double rentingFee, RentalStatus status) {
        this.id = id;
        this.property = property;
        this.owner = owner;
        this.host = null;
        this.mainTenant = mainTenant;
        this.subTenants = subTenants;
        this.period = period;
        this.contractDate = contractDate;
        this.rentingFee = rentingFee;
        this.status = null;
    }

    public String getId(){return id;}
    public Property getProperty(){return property;}
    public Owner getOwner(){return owner;}
    public Host getHost(){return host;}
    public Tenant getMainTenant(){return mainTenant;}
    public List<Tenant> getSubTenants(){return subTenants;}
    public String getPeriod(){return period;}
    public Date getContractDate(){return contractDate;}
    public Double getRentingFee(){return rentingFee;}
    public RentalStatus getStatus(){return status;}
    public void setId(String id){this.id = id;}
    public void setProperty(Property property){this.property = property;}
    public void setOwner(Owner owner){this.owner = owner;}
    public void setHost(Host host){this.host = host;}
    public void setMainTenant(Tenant mainTenant){this.mainTenant = mainTenant;}
    public void setSubTenants(List<Tenant> subTenants){this.subTenants = subTenants;}
    public void setPeriod(String period){this.period = period;}
    public void setContractDate(Date contractDate){this.contractDate = contractDate;}
    public void setRentingFee(Double rentingFee){this.rentingFee = rentingFee;}
    public void setStatus(RentalStatus status){this.status = status;}

    public void printAgreement() {
        // Using nested getters to retrieve IDs from related objects
        System.out.printf("%-18s %-12s %-12s %-12s %-12s %-30s %-12s %-12s %-10.2f %-10s%n",
                getId(),
                property != null ? property.getId() : "N/A",
                mainTenant != null ? mainTenant.getId() : "N/A",
                host != null ? host.getId() : "N/A",
                owner != null ? owner.getId() : "N/A",
                formatSubTenantIds(), // Helper method to format sub-tenants
                getPeriod(),
                new SimpleDateFormat("dd/MM/yyyy").format(getContractDate()),
                getRentingFee(),
                getStatus());
    }

    // Helper method to format sub-tenant IDs
    private String formatSubTenantIds() {
        if (subTenants == null || subTenants.isEmpty()) {
            return "N/A";
        }
        List<String> subTenantIds = new ArrayList<>();
        for (Tenant tenant : subTenants) {
            subTenantIds.add(tenant.getId());
        }
        return String.join(",", subTenantIds); // Converts list to a comma-separated string
    }
}
