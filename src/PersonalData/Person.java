package PersonalData;

import java.io.Serializable;

/**
 * Created by Tarasevich Vladislav on 25.04.2017.
 * @author ReemBer (GitHub)
 * This class is designed to create and store personal data
 */
public class Person implements Serializable
{
    private static final String DEFAULT_STRING_VALUE = "NO DATA";
    private static final int DEFAULT_INTEGER_VALUE   = 0;

    private String  firstName;
    private String  lastName;
    private String  email;
    private String  phoneNumber;
    private Address address;
    private String  jobPlace;
    private int     experience;

    public Person()
    {
        firstName = lastName = email = phoneNumber = jobPlace = DEFAULT_STRING_VALUE;
        address = new Address();
        experience = DEFAULT_INTEGER_VALUE;
    }

    public Person(String firstName, String lastName, String email, String phoneNumber, Address address, String jobPlace, int experience)
    {
        this.firstName   = firstName;
        this.lastName    = lastName;
        this.email       = email;
        this.phoneNumber = phoneNumber;
        this.address     = address;
        this.jobPlace    = jobPlace;
        this.experience  = experience;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAddress() {
        return address;
    }

    public String getJobPlace() {
        return jobPlace;
    }

    public int getExperience() {
        return experience;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setJobPlace(String jobPlace) {
        this.jobPlace = jobPlace;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }
}
