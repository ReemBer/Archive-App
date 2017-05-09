package PersonalData;

import java.io.Serializable;

/**
 * Created by Tarasevich Vladislav on 25.04.2017.
 * @author name ReemBer
 * This class is used to storing home address of some Person.
 */
public class Address implements Serializable
{
    private final String DEFAULT_STRING_VALUE  = "NO DATA";
    private final int    DEFAULT_INTEGER_VALUE = -1;

    private String      country;
    private String         city;
    private String       street;
    private int     houseNumber;
    private int apartmentNumber;

    public Address()
    {
        country = city = street = DEFAULT_STRING_VALUE;
        houseNumber = apartmentNumber = DEFAULT_INTEGER_VALUE;
    }

    public Address(String country, String city, String street, int houseNumber, int apartmentNumber)
    {
        this.country         = country;
        this.city            = city;
        this.street          = street;
        this.houseNumber     = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setApartmentNumber(int apartmentNumber) {
        this.apartmentNumber = apartmentNumber;
    }
}
