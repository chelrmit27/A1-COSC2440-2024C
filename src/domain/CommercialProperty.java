package domain;

import java.util.List;

public class CommercialProperty extends Property {
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

    public CommercialProperty(String id, String address, Double price, PropertyStatus status, Owner owner, List<Host> hostList){
        super(id, address, price, status, owner, hostList);
        this.businessType = "Default";
        this.parkingSpace = 0;
        this.squareFootage = 0.0;
    }

    public CommercialProperty(String id, String address, Double price, PropertyStatus status, Owner owner, List<Host> hostList,
                              String businessType, int parkingSpace, double squareFootage) {
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
}
