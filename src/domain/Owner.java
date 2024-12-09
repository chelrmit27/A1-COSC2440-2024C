package domain;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.text.SimpleDateFormat;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class Owner extends Person{
    private List<Property> ownedProperties;

    public Owner(){
        super();
        ownedProperties = new ArrayList<>();
    }

    public Owner(String id, String name, Date dob, String phoneNumber){
        super(id, name, dob, phoneNumber);
        ownedProperties = new ArrayList<>();
    }

    public Owner(String id, String name, Date dob, String phoneNumber,
                 List<Property> ownedProperties) {
        super(id, name, dob, phoneNumber);
        this.ownedProperties = ownedProperties != null ? ownedProperties : new ArrayList<>();
    }

    public List<Property> getOwnedProperties() {return ownedProperties;}
    public void setOwnedProperties(List<Property> ownedProperties) {this.ownedProperties = ownedProperties;}

    public void printOwner() {
        // Prepare values for display
        String ownerId = (getId() != null) ? getId() : "N/A";
        String ownerName = (getName() != null) ? getName() : "N/A";
        String dobStr = (getDob() != null) ? new SimpleDateFormat("dd/MM/yyyy").format(getDob()) : "N/A";
        String phoneNumber = (getPhoneNumber() != null) ? getPhoneNumber() : "N/A";

        // Convert owned property IDs to a comma-separated string, or "N/A" if the list is null or empty
        String propertyIds = ownedProperties != null && !ownedProperties.isEmpty()
                ? ownedProperties.stream()
                .map(Property::getId)
                .filter(Objects::nonNull)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        // Print formatted owner details
        System.out.printf("%-10s %-20s %-15s %-15s %-30s%n",
                ownerId, ownerName, dobStr, phoneNumber, propertyIds);
    }

    public void printOwner(StringBuilder content) {
        // Prepare values for display
        String ownerId = (getId() != null) ? getId() : "N/A";
        String ownerName = (getName() != null) ? getName() : "N/A";
        String dobStr = (getDob() != null) ? new SimpleDateFormat("dd/MM/yyyy").format(getDob()) : "N/A";
        String phoneNumber = (getPhoneNumber() != null) ? getPhoneNumber() : "N/A";

        // Convert owned property IDs to a comma-separated string, or "N/A" if the list is null or empty
        String propertyIds = ownedProperties != null && !ownedProperties.isEmpty()
                ? ownedProperties.stream()
                .map(Property::getId)
                .filter(Objects::nonNull)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        // Print formatted owner details
        content.append(String.format("%-10s %-20s %-15s %-15s %-30s%n",
                ownerId, ownerName, dobStr, phoneNumber, propertyIds));
    }

}
