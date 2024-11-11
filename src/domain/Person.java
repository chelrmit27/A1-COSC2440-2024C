package domain;
import java.util.Date;

public abstract class Person {
    private String id;
    private String name;
    private Date dob;
    private String phoneNumber;

    public Person(){
        this.id = "Default";
        this.name = "Default";
        this.dob = new Date();
        this.phoneNumber = "Default";
    }

    public Person(String id, String name, Date dob, String phoneNumber){
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
    }

    public String getId() { return id;}
    public void setId(String id) { this.id = id;}
    public String getName() { return name;}
    public void setName(String name) { this.name = name;}
    public Date getDob() { return dob;}
    public void setDob(Date dob) { this.dob = dob;}
    public String getPhoneNumber() { return phoneNumber;}
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber;}

}
