package domain;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public abstract class Property{
    private String id;
    private String address;
    private Double price;
    private PropertyStatus status;
    private Owner owner;
    private List<Host> hostList;

    public Property(){
        this.id = "Default";
        this.address = "Default";
        this.price = 0.0;
        this.status = null;
        this.owner = null;
        this.hostList = new ArrayList<Host>();
    }

    public Property(String id, String address, Double price, PropertyStatus status, Owner owner){
        this.id = id;
        this.address = address;
        this.price = price;
        this.status = status;
        this.owner = owner;
        this.hostList = new ArrayList<Host>();
    }

    public Property(String id, String address, Double price, PropertyStatus status, Owner owner, List<Host> hostList){
        this.id = id;
        this.address = address;
        this.price = price;
        this.status = status;
        this.owner = owner;
        this.hostList = hostList;
    }

    public String getId() {return id;}
    public String getAddress() {return address;}
    public Double getPrice() {return price;}
    public PropertyStatus getStatus() {return status;}
    public Owner getOwner() {return owner;}
    public List<Host> getHostList() {return hostList;}
    public void setId(String id) {this.id = id;}
    public void setAddress(String address) {this.address = address;}
    public void setPrice(Double price) {this.price = price;}
    public void setStatus(PropertyStatus status) {this.status = status;}
    public void setOwner(Owner owner) {this.owner = owner;}
    public void setHostList(List<Host> hostList) {this.hostList = hostList;}

    public static Comparator<Property> sortByIdComparator() {
        return Comparator.comparing(Property::getId);
    }

    public static Comparator<Property> sortByPriceComparator() {
        return Comparator.comparing(Property::getPrice);
    }

}
