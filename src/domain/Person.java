package domain;
import java.util.Comparator;
import java.util.Date;

/**
 * @author <Tran Tu Tam - s3999159>
 */

public abstract class Person{
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
    public String getName() { return name;}
    public Date getDob() { return dob;}
    public String getPhoneNumber() { return phoneNumber;}
    public void setId(String id) { this.id = id;}
    public void setName(String name) { this.name = name;}
    public void setDob(Date dob) { this.dob = dob;}
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber;}

    public static Comparator<Person> sortByIdComparator() {
        return Comparator.comparing(Person::getId);
    }

    public static Comparator<Person> sortByNameComparator() {
        return Comparator.comparing(Person::getName);
    }

    public static Comparator<Person> sortByDOBComparator() {
        return Comparator.comparing(Person::getDob);
    }
}
