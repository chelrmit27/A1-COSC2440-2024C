package domain;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

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

    public RentalAgreement(String id, Property property, Owner owner, Host host, Tenant mainTenant, List<Tenant> subTenants, String period, Date contractDate, Double rentingFee) {
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
    public Person getOwner(){return owner;}
    public Host getHost(){return host;}
    public Tenant getMainTenant(){return mainTenant;}
    public List<Tenant> getSubTenants(){return subTenants;}
    public String getPeriod(){return period;}
    public Date getContractDate(){return contractDate;}
    public Double getRentingFee(){return rentingFee;}
    public RentalStatus getStatus(){return status;}
    public void setId(String id){this.id = id;}
    public void setOwner(Owner owner){this.owner = owner;}
    public void setHost(Host host){this.host = host;}
    public void setMainTenant(Tenant mainTenant){this.mainTenant = mainTenant;}
    public void setSubTenants(List<Tenant> subTenants){this.subTenants = subTenants;}
    public void setPeriod(String period){this.period = period;}
    public void setContractDate(Date contractDate){this.contractDate = contractDate;}
    public void setRentingFee(Double rentingFee){this.rentingFee = rentingFee;}
    public void setStatus(RentalStatus status){this.status = status;}


}
