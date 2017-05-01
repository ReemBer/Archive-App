package Server.Parser;

import PersonalData.Address;
import PersonalData.Person;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by Tarsevich Vladislav on 01.05.2017.
 * This class represent Stream API XML
 */
public class PersonStAXParser extends XmlParser<Person>
{
    private static final String prefix = "D:\\WORKSPACE\\Archive\\src\\Server\\Persons\\";
    private static final String XSDSchema = prefix + "PersonValidSchema.xsd";

    private PersonStAXHandler handler;

    public static final Logger logger = Logger.getLogger(PersonStAXParser.class);

    public PersonStAXParser()
    {
        super(XSDSchema);

        handler = new PersonStAXHandler();
    }

    /**
     * Method, that parse the Person XML File and returns the User object
     * @param xmlFile path to Person XML File
     * @return Person object
     * @throws PersonParsingException cannot use features, that provide parsing User XML File
     * @throws IllegalPersonXMLFormatException The User XML File does not match pattern
     */
    @Override
    public Person parse(String xmlFile) throws PersonParsingException, IllegalPersonXMLFormatException
    {
        try
        {
            //existence check
            exist(xmlFile);

            //valid check
            validate(xmlFile);

            //parsing
            handler.extract(xmlFile);

            return handler.getPerson();
        }
        catch (SAXException e)
        {
            e.printStackTrace();
            logger.fatal(e);
            throw new IllegalPersonXMLFormatException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            logger.fatal(e);
            throw new PersonParsingException(e);
        }
    }
}

class PersonStAXHandler
{
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Address address = new Address();
    private String jobPlace;
    private int    experience;

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

    public void extract(String xmlFile) throws FileNotFoundException, XMLStreamException
    {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(new FileReader(xmlFile));
        while (eventReader.hasNext())
        {
            XMLEvent event = eventReader.nextEvent();
            switch (event.getEventType())
            {
                case XMLStreamConstants.START_ELEMENT:
                {
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();

                    onFirstName = qName.equalsIgnoreCase("firstName");
                    onLastName = qName.equalsIgnoreCase("lastName");
                    onEmail = qName.equalsIgnoreCase("email");
                    onPhoneNumber = qName.equalsIgnoreCase("phoneNumber");
                    onCountry = qName.equalsIgnoreCase("country");
                    onCity = qName.equalsIgnoreCase("city");
                    onStreet = qName.equalsIgnoreCase("street");
                    onHouseNumber = qName.equalsIgnoreCase("houseNumber");
                    onApartmentNumber = qName.equalsIgnoreCase("apartmentNumber");
                    onJobPlace = qName.equalsIgnoreCase("jobPlace");
                    onExperience = qName.equalsIgnoreCase("experience");

                    break;
                }
                case XMLStreamConstants.CHARACTERS:
                {
                    Characters characters = event.asCharacters();
                    String content = characters.getData();
                    if(onFirstName)
                    {
                        firstName = content;
                    }
                    else if(onLastName)
                    {
                        lastName = content;
                    }
                    else if(onEmail)
                    {
                        email = content;
                    }
                    else if(onPhoneNumber)
                    {
                        phoneNumber = content;
                    }
                    else if(onCountry)
                    {
                        address.setCountry(content);
                    }
                    else if(onCity)
                    {
                        address.setCity(content);
                    }
                    else if(onStreet)
                    {
                        address.setStreet(content);
                    }
                    else if(onHouseNumber)
                    {
                        address.setHouseNumber(Integer.parseInt(content));
                    }
                    else if(onApartmentNumber)
                    {
                        address.setApartmentNumber(Integer.parseInt(content));
                    }
                    else if(onJobPlace)
                    {
                        jobPlace = content;
                    }
                    else if(onExperience)
                    {
                        experience = Integer.parseInt(content);
                    }
                    break;
                }
            }
        }
    }

    public Person getPerson()
    {
        Person person = new Person();

        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmail(email);
        person.setPhoneNumber(phoneNumber);
        person.setAddress(address);
        person.setJobPlace(jobPlace);
        person.setExperience(experience);

        return person;
    }
}
