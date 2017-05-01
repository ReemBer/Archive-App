package Server.Parser;

import PersonalData.Address;
import PersonalData.Person;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Tarasevich Vladislav on 01.05.2017.
 * This class represents the Simple API for XML model
 */
public class PersonSAXParser extends XmlParser<Person>
{
    PersonHandler handler = new PersonHandler();

    @Override
    public Person parse(String xmlFile) throws Exception
    {
            
        return null;
    }
}

class PersonHandler extends DefaultHandler
{
    private String  firstName;
    private String  lastName;
    private String  email;
    private String  phoneNumber;

    private String  country;
    private String  city;
    private String  street;
    private int     houseNumber;
    private int     apartmentNumber;

    private String  jobPlace;
    private int     experience;

    private boolean onFirstName;
    private boolean onLastName;
    private boolean onEmail;
    private boolean onPhoneNumber;

    private boolean onCountry;
    private boolean onCity;
    private boolean onStreet;
    private boolean onHouseNumber;
    private boolean onApartmentNumber;

    private boolean onJobPlace;
    private boolean onExperience;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        onFirstName = qName.equals("firstName");
        onLastName = qName.equals("lastName");
        onEmail = qName.equals("email");
        onPhoneNumber = qName.equals("phoneNumber");
        onCountry = qName.equals("country");
        onCity = qName.equals("city");
        onStreet = qName.equals("street");
        onHouseNumber = qName.equals("houseNumber");
        onApartmentNumber = qName.equals("apartmentNumber");
        onJobPlace = qName.equals("jobPlace");
        onExperience = qName.equals("experience");
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        if(onFirstName)
        {
            firstName = new String(ch, start, length);
        }
        else if(onLastName)
        {
            lastName = new String(ch, start, length);
        }
        else if(onEmail)
        {
            email = new String(ch, start, length);
        }
        else if (onPhoneNumber)
        {
            phoneNumber = new String(ch, start, length);
        }
        else if(onCountry)
        {
            country = new String(ch, start, length);
        }
        else if(onCity)
        {
            city = new String(ch, start, length);
        }
        else if(onStreet)
        {
            street = new String(ch, start, length);
        }
        else if(onHouseNumber)
        {
            String hn = new String(ch, start, length);
            houseNumber = Integer.parseInt(hn);
        }
        else if(onApartmentNumber)
        {
            String an = new String(ch, start, length);
            apartmentNumber = Integer.parseInt(an);
        }
        else if (onJobPlace)
        {
            jobPlace = new String(ch, start, length);
        }
        else if(onExperience)
        {
            String exp = new String(ch, start, length);
            experience = Integer.parseInt(exp);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    public Person getPerson()
    {
        Person person = new Person();
        Address address = new Address();

        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
        address.setCountry(country);
        address.setCity(city);
        address.setStreet(street);
        address.setHouseNumber(houseNumber);
        address.setApartmentNumber(apartmentNumber);
        person.setAddress(address);
        person.setJobPlace(jobPlace);
        person.setExperience(experience);

        return person;
    }
}
