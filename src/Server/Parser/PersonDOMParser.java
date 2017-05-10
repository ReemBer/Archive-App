package Server.Parser;

import PersonalData.Address;
import PersonalData.Person;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

/**
 * Created by Tarasevich Vladislav on 01.05.2017.
 * A class, that used to creating, editing, deleting, and parsing Personal Data XML files
 */
public class PersonDOMParser extends DOMXmlParser<Person>
{
    private static final String prefix = "D:\\WORKSPACE\\Archive\\src\\Server\\Persons\\";
    private static final String suffix = ".xml";

    private static final String XSDSchema = prefix + "PersonValidSchema.xsd";

    public  static final Logger logger = Logger.getLogger(PersonDOMParser.class);

    public PersonDOMParser()
    {
        super(XSDSchema);
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
        Person result = new Person();
        try
        {
            //existence check
            exist(xmlFile);

            //valid check
            validate(xmlFile);

            //parsing
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            //get first name
            Node firstName = document.getElementsByTagName("firstName").item(0);
            result.setFirstName(firstName.getTextContent());

            //get last name
            Node lastName = document.getElementsByTagName("lastName").item(0);
            result.setLastName(lastName.getTextContent());

            //get email
            Node email = document.getElementsByTagName("email").item(0);
            result.setEmail(email.getTextContent());

            //get phone number
            Node phoneNumber = document.getElementsByTagName("phoneNumber").item(0);
            result.setPhoneNumber(phoneNumber.getTextContent());

            //get address
            NodeList addressList = document.getElementsByTagName("address").item(0).getChildNodes();
            Node country = addressList.item(0);
            Node city = addressList.item(1);
            Node street = addressList.item(2);
            Node houseNumber = addressList.item(3);
            Node apartmentNumber = addressList.item(4);
            Address address = new Address();
            address.setCountry(country.getTextContent());
            address.setCity(city.getTextContent());
            address.setStreet(street.getTextContent());
            address.setHouseNumber(Integer.parseInt(houseNumber.getTextContent()));
            address.setApartmentNumber(Integer.parseInt(apartmentNumber.getTextContent()));
            result.setAddress(address);

            //get job place
            Node jobPlace = document.getElementsByTagName("jobPlace").item(0);
            result.setJobPlace(jobPlace.getTextContent());

            //get experience
            Node experience = document.getElementsByTagName("experience").item(0);
            result.setExperience(Integer.parseInt(experience.getTextContent()));
        }
        catch (SAXException e)
        {
            e.printStackTrace();
            logger.fatal(e);
            throw new IllegalPersonXMLFormatException(e);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            logger.fatal(ex);
            throw new PersonParsingException(ex);
        }


        return result;
    }

    /**
     * Creating new Person XML File
     * @param object A Person object, that will be writing in the XML FIle
     * @throws PersonExistException such Person already exist
     * @throws PersonCreatingException cannot use the XML Creating Features
     */
    @Override
    public void create(Person object) throws PersonExistException, PersonCreatingException
    {
        try
        {
            if(object == null) throw new NullPointerException();

            //cannot create person -- such person already exist
            try
            {
                if(exist(personFileName(object))) throw new PersonExistException();
            }
            catch (FileNotFoundException e){}


            //creating new xml document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            //creating elements
            Element person = document.createElement("person");
            Element firstName = document.createElement("firstName");
            Element lastName  = document.createElement("lastName");
            Element email = document.createElement("email");
            Element phoneNumber = document.createElement("phoneNumber");
            Element address = document.createElement("address");
            Element country = document.createElement("country");
            Element city = document.createElement("city");
            Element street = document.createElement("street");
            Element houseNumber = document.createElement("houseNumber");
            Element apartmentNumber = document.createElement("apartmentNumber");
            Element jobPlace = document.createElement("jobPlace");
            Element experience = document.createElement("experience");

            //creating document tree
            address.appendChild(country);
            address.appendChild(city);
            address.appendChild(street);
            address.appendChild(houseNumber);
            address.appendChild(apartmentNumber);

            person.appendChild(firstName);
            person.appendChild(lastName);
            person.appendChild(email);
            person.appendChild(phoneNumber);
            person.appendChild(address);
            person.appendChild(jobPlace);
            person.appendChild(experience);

            document.appendChild(person);

            //set content of file
            firstName.setTextContent(object.getFirstName());
            lastName.setTextContent(object.getLastName());
            email.setTextContent(object.getEmail());
            phoneNumber.setTextContent(object.getPhoneNumber());
            country.setTextContent(object.getAddress().getCountry());
            city.setTextContent(object.getAddress().getCity());
            street.setTextContent(object.getAddress().getStreet());
            houseNumber.setTextContent(Integer.toString(object.getAddress().getHouseNumber()));
            apartmentNumber.setTextContent(Integer.toString(object.getAddress().getApartmentNumber()));
            jobPlace.setTextContent(object.getJobPlace());
            experience.setTextContent(Integer.toString(object.getExperience()));

            //writing document into the file
            writeDocument(document, personFileName(object));
        }
        catch (PersonExistException e)
        {
            throw new PersonExistException(e);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            logger.fatal(ex);
            throw new PersonCreatingException(ex);
        }
    }

    /**
     * Deleting old Person XML File and creating new User XML File
     * @param oldObject will be deleted
     * @param newObject will be created
     * @throws PersonCreatingException cannot use the XML Creating Features
     * @throws PersonExistException such Person already exist
     */
    @Override
    public void edit(Person oldObject, Person newObject)
            throws PersonCreatingException, PersonExistException
    {
        delete(oldObject);
        create(newObject);
    }

    /**
     * Deleting Person XML File
     * @param object Person, whose XML File will be deleted
     */
    @Override
    public void delete(Person object)
    {
            File del = new File(personFileName(object));
            del.delete();
    }


    /**
     * generating file name, that correspond with such Person
     * @param person a Person object for generating
     * @return Person XML File name
     */
    private String personFileName(Person person)
    {
        return prefix + person.getFirstName() + person.getLastName() + suffix;
    }

    /**
     *  Creating the Person XML File and writing the Person Document into this file
     * @param document The Person document, that will be written
     * @param filePath path to the Person XML File
     * @throws TransformerFactoryConfigurationError
     * @throws IOException
     * @throws TransformerException
     */
    private void writeDocument(Document document, final String filePath)
            throws TransformerFactoryConfigurationError, IOException, TransformerException, PersonCreatingException
    {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(document);
        File outFile = new File(filePath);
        boolean newFile = outFile.createNewFile();
        if(!newFile) throw new PersonCreatingException(new FileNotFoundException(outFile.getName()));
        FileOutputStream out = new FileOutputStream(outFile);

        StreamResult result = new StreamResult(out);
        transformer.transform(source, result);

        out.close();
    }
}
