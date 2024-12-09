package domain;

import java.util.List;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class ResidentialProperty extends Property{
    private int numOfBedrooms;
    private boolean gardenAvailability;
    private boolean petFriendliness;

    public ResidentialProperty(){
        super();
        numOfBedrooms = 0;
        gardenAvailability = false;
        petFriendliness = false;
    }

    public ResidentialProperty(String id, String address, Double price, PropertyStatus status, Owner owner){
        super(id, address, price, status, owner);
        numOfBedrooms = 0;
        gardenAvailability = false;
        petFriendliness = false;
    }

    public ResidentialProperty(String id, String address, Double price, PropertyStatus status, Owner owner,
                               List<Host> hostList){
        super(id, address, price, status, owner, hostList);
        numOfBedrooms = 0;
        gardenAvailability = false;
        petFriendliness = false;
    }

    public ResidentialProperty(String id, String address, Double price, PropertyStatus status, Owner owner,
                               int numOfBedrooms, boolean gardenAvailability, boolean petFriendliness){
        super(id, address, price, status, owner);
        this.numOfBedrooms = numOfBedrooms;
        this.gardenAvailability = gardenAvailability;
        this.petFriendliness = petFriendliness;
    }

    public ResidentialProperty(String id, String address, Double price, PropertyStatus status, Owner owner,
                               List<Host> listHost, int numOfBedrooms, boolean gardenAvailability, boolean petFriendliness){
        super(id, address, price, status, owner, listHost);
        this.numOfBedrooms = numOfBedrooms;
        this.gardenAvailability = gardenAvailability;
        this.petFriendliness = petFriendliness;
    }

    public int getNumOfBedrooms() { return numOfBedrooms; }
    public boolean isGardenAvailability() { return gardenAvailability; }
    public boolean isPetFriendliness() { return petFriendliness; }
    public void setNumOfBedrooms(int numOfBedrooms) {
        this.numOfBedrooms = numOfBedrooms;
    }
    public void setGardenAvailability(boolean gardenAvailability) {
        this.gardenAvailability = gardenAvailability;
    }
    public void setPetFriendliness(boolean petFriendliness) {
        this.petFriendliness = petFriendliness;
    }

    public void printResidentialProperty() {
        // Prepare values for display
        String propertyId = (getId() != null) ? getId() : "N/A";
        String address = (getAddress() != null) ? getAddress() : "N/A";
        String priceStr = (getPrice() != null) ? String.format("%.2f", getPrice()) : "N/A";
        String statusStr = (getStatus() != null) ? getStatus().toString() : "N/A";
        String ownerId = (getOwner() != null && getOwner().getId() != null) ? getOwner().getId() : "N/A";

        String hostIds = (getHostList() != null && !getHostList().isEmpty())
                ? getHostList().stream()
                .map(Host::getId)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        String numOfBedroomsStr = "Number Bedrooms: " + numOfBedrooms;
        String gardenAvailabilityStr = "Garden Availability: " + (gardenAvailability ? "Yes" : "No");
        String petFriendlinessStr = "Pet Friendliness: " + (petFriendliness ? "Yes" : "No");

        // Print formatted residential property details
        System.out.printf("%-10s %-20s %-10s %-10s %-10s %-20s %-25s %-25s %-25s%n",
                propertyId, address, priceStr, statusStr, ownerId, hostIds,
                numOfBedroomsStr, gardenAvailabilityStr, petFriendlinessStr);
    }

    public void printResidentialProperty(StringBuilder content) {
        // Prepare values for display
        String propertyId = (getId() != null) ? getId() : "N/A";
        String address = (getAddress() != null) ? getAddress() : "N/A";
        String priceStr = (getPrice() != null) ? String.format("%.2f", getPrice()) : "N/A";
        String statusStr = (getStatus() != null) ? getStatus().toString() : "N/A";
        String ownerId = (getOwner() != null && getOwner().getId() != null) ? getOwner().getId() : "N/A";

        String hostIds = (getHostList() != null && !getHostList().isEmpty())
                ? getHostList().stream()
                .map(Host::getId)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        String numOfBedroomsStr = "Number Bedrooms: " + numOfBedrooms;
        String gardenAvailabilityStr = "Garden Availability: " + (gardenAvailability ? "Yes" : "No");
        String petFriendlinessStr = "Pet Friendliness: " + (petFriendliness ? "Yes" : "No");

        // Print formatted residential property details
        content.append(String.format("%-10s %-20s %-10s %-10s %-10s %-20s %-25s %-25s %-25s%n",
                propertyId, address, priceStr, statusStr, ownerId, hostIds,
                numOfBedroomsStr, gardenAvailabilityStr, petFriendlinessStr));
    }

}
