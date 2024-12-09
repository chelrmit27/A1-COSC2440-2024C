package domain;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.text.SimpleDateFormat;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class Host extends Person{
    private List<Property> managedProperties;
    private List<Owner> cooperatingOwners;

    public Host() {
        super();
        managedProperties = new ArrayList<Property>();
        cooperatingOwners = new ArrayList<Owner>();
    }

    public Host(String id, String name, Date dob, String phoneNumber) {
        super(id, name, dob, phoneNumber);
        managedProperties = new ArrayList<Property>();
        cooperatingOwners = new ArrayList<Owner>();
    }

    public Host(String id, String name, Date dob, String phoneNumber,
                List<Owner> cooperatingOwners) {
        super(id, name, dob, phoneNumber);
        this.managedProperties =  new ArrayList<>();
        this.cooperatingOwners = cooperatingOwners != null ? cooperatingOwners : new ArrayList<>();
    }

    public Host(String id, String name, Date dob, String phoneNumber,
                List<Owner> cooperatingOwners,
                List<Property> managedProperties) {
        super(id, name, dob, phoneNumber);
        this.managedProperties = managedProperties;
        this.cooperatingOwners = cooperatingOwners;
    }

    public List<Property> getManagedProperties() {return managedProperties;}
    public List<Owner> getCooperatingOwners() {return cooperatingOwners;}
    public void setManagedProperties(List<Property> managedProperty) {this.managedProperties = managedProperty;}
    public void setCooperatingOwners(List<Owner> cooperatingOwners) {this.cooperatingOwners = cooperatingOwners;}

    public void printHost() {
        // Prepare values for display
        String hostId = (getId() != null) ? getId() : "N/A";
        String hostName = (getName() != null) ? getName() : "N/A";
        String dobStr = (getDob() != null) ? new SimpleDateFormat("dd/MM/yyyy").format(getDob()) : "N/A";
        String phoneNumber = (getPhoneNumber() != null) ? getPhoneNumber() : "N/A";

        // Convert managed property IDs to a comma-separated string, or "N/A" if the list is null or empty
        String managedPropertyIds = managedProperties != null && !managedProperties.isEmpty()
                ? managedProperties.stream()
                .map(Property::getId)
                .filter(Objects::nonNull)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        // Convert cooperating owner IDs to a comma-separated string, or "N/A" if the list is null or empty
        String cooperatingOwnerIds = cooperatingOwners != null && !cooperatingOwners.isEmpty()
                ? cooperatingOwners.stream()
                .map(Owner::getId)
                .filter(Objects::nonNull)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        // Print formatted host details
        System.out.printf("%-10s %-20s %-15s %-15s %-30s %-30s%n",
                hostId, hostName, dobStr, phoneNumber, managedPropertyIds, cooperatingOwnerIds);
    }

    public void printHost(StringBuilder content) {
        // Prepare values for display
        String hostId = (getId() != null) ? getId() : "N/A";
        String hostName = (getName() != null) ? getName() : "N/A";
        String dobStr = (getDob() != null) ? new SimpleDateFormat("dd/MM/yyyy").format(getDob()) : "N/A";
        String phoneNumber = (getPhoneNumber() != null) ? getPhoneNumber() : "N/A";

        // Convert managed property IDs to a comma-separated string, or "N/A" if the list is null or empty
        String managedPropertyIds = managedProperties != null && !managedProperties.isEmpty()
                ? managedProperties.stream()
                .map(Property::getId)
                .filter(Objects::nonNull)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        // Convert cooperating owner IDs to a comma-separated string, or "N/A" if the list is null or empty
        String cooperatingOwnerIds = cooperatingOwners != null && !cooperatingOwners.isEmpty()
                ? cooperatingOwners.stream()
                .map(Owner::getId)
                .filter(Objects::nonNull)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        // Print formatted host details
        content.append(String.format("%-10s %-20s %-15s %-15s %-30s %-30s%n",
                hostId, hostName, dobStr, phoneNumber, managedPropertyIds, cooperatingOwnerIds));
    }

}
