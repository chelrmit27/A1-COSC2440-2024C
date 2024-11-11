package domain;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Owner extends Person {
    private  List<Property> ownedProperties;

    public Owner(){
        super();
        ownedProperties = new ArrayList<Property>();
    }

    public Owner(String id, String name, Date dob, String phoneNumber){
        super(id, name, dob, phoneNumber);
        ownedProperties = new ArrayList<Property>();
    }

    public Owner(String id, String name, Date dob, String phoneNumber, List<Property> ownedProperties){
        super(id, name, dob, phoneNumber);
        this.ownedProperties = ownedProperties;
    }

    public List<Property> getOwnedProperties() {return ownedProperties;}
}
