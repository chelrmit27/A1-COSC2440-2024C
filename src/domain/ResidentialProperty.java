package domain;

import java.util.List;

public class ResidentialProperty extends Property {
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

    public ResidentialProperty(String id, String address, Double price, PropertyStatus status, Owner owner, List<Host> hostList){
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
}
