package domain;

public class CommercialProperty extends Property {
    private String businessType;
    private String parkingSpace;
    private String squareFootage;

    public CommercialProperty() {
        super();
        this.businessType = "Default";
        this.parkingSpace = "Default";
        this.squareFootage = "Default";
    }

    public CommercialProperty(String id, String address, Double price, PropertyStatus status, Owner owner){
        super(id, address, price, status, owner);
        this.businessType = "Default";
        this.parkingSpace = "Default";
        this.squareFootage = "Default";
    }

    public CommercialProperty(String id, String address, Double price, PropertyStatus status, Owner owner,
                              String businessType, String parkingSpace, String squareFootage) {
        super(id, address, price, status, owner);
        this.businessType = businessType;
        this.parkingSpace = parkingSpace;
        this.squareFootage = squareFootage;
    }

    public String getBusinessType() {return businessType;}
    public String getParkingSpace() {return parkingSpace;}
    public String getSquareFootage() {return squareFootage;}
    public void setBusinessType(String businessType) {this.businessType = businessType;}
    public void setParkingSpace(String parkingSpace) {this.parkingSpace = parkingSpace;}
    public void setSquareFootage(String squareFootage) {this.squareFootage = squareFootage;}
}
