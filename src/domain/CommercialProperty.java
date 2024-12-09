package domain;

import java.util.List;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public class CommercialProperty extends Property{
    private String businessType;
    private int parkingSpace;
    private double squareFootage;

    public CommercialProperty() {
        super();
        this.businessType = "Default";
        this.parkingSpace = 0;
        this.squareFootage = 0.0;
    }

    public CommercialProperty(String id, String address, Double price, PropertyStatus status, Owner owner){
        super(id, address, price, status, owner);
        this.businessType = "Default";
        this.parkingSpace = 0;
        this.squareFootage = 0.0;
    }

    public CommercialProperty(String id, String address, Double price, PropertyStatus status, Owner owner,
                              List<Host> hostList, String businessType, int parkingSpace, double squareFootage) {
        super(id, address, price, status, owner, hostList);
        this.businessType = businessType;
        this.parkingSpace = parkingSpace;
        this.squareFootage = squareFootage;
    }

    public CommercialProperty(String id, String address, Double price, PropertyStatus status, Owner owner,
                              String businessType, int parkingSpace, double squareFootage) {
        super(id, address, price, status, owner);
        this.businessType = businessType;
        this.parkingSpace = parkingSpace;
        this.squareFootage = squareFootage;
    }

    public String getBusinessType() {return businessType;}
    public int getParkingSpace() {return parkingSpace;}
    public double getSquareFootage() {return squareFootage;}
    public void setBusinessType(String businessType) {this.businessType = businessType;}
    public void setParkingSpace(int parkingSpace) {this.parkingSpace = parkingSpace;}
    public void setSquareFootage(double squareFootage) {this.squareFootage = squareFootage;}


    public void printCommercialProperty() {
        // Prepare values for display
        String propertyId = (getId() != null) ? getId() : "N/A";
        String address = (getAddress() != null) ? getAddress() : "N/A";
        String price = (getPrice() != null) ? String.format("%.2f", getPrice()) : "N/A";
        String status = (getStatus() != null) ? getStatus().toString() : "N/A";
        String ownerId = (getOwner() != null && getOwner().getId() != null) ? getOwner().getId() : "N/A";

        String hostIds = (getHostList() != null && !getHostList().isEmpty())
                ? getHostList().stream()
                .map(Host::getId)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        String businessTypeStr = (businessType != null) ? "Business Type: " + businessType : "Business Type: N/A";
        String parkingSpacesStr = "Parking Space: " + parkingSpace;
        String squareFootageStr = "Square Footage: " + String.format("%.2f", squareFootage);

        // Print formatted commercial property details
        System.out.printf("%-10s %-20s %-10s %-10s %-10s %-20s %-25s %-25s %-25s%n",
                propertyId, address, price, status, ownerId, hostIds,
                businessTypeStr, parkingSpacesStr, squareFootageStr);
    }

    public void printCommercialProperty(StringBuilder content) {
        // Prepare values for display
        String propertyId = (getId() != null) ? getId() : "N/A";
        String address = (getAddress() != null) ? getAddress() : "N/A";
        String price = (getPrice() != null) ? String.format("%.2f", getPrice()) : "N/A";
        String status = (getStatus() != null) ? getStatus().toString() : "N/A";
        String ownerId = (getOwner() != null && getOwner().getId() != null) ? getOwner().getId() : "N/A";

        String hostIds = (getHostList() != null && !getHostList().isEmpty())
                ? getHostList().stream()
                .map(Host::getId)
                .reduce((id1, id2) -> id1 + "," + id2)
                .orElse("N/A")
                : "N/A";

        String businessTypeStr = (businessType != null) ? "Business Type: " + businessType : "Business Type: N/A";
        String parkingSpacesStr = "Parking Space: " + parkingSpace;
        String squareFootageStr = "Square Footage: " + String.format("%.2f", squareFootage);

        // Print formatted commercial property details
        content.append(String.format("%-10s %-20s %-10s %-10s %-10s %-20s %-25s %-25s %-25s%n",
                propertyId, address, price, status, ownerId, hostIds,
                businessTypeStr, parkingSpacesStr, squareFootageStr));
    }

}
