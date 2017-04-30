package Server.Parser;

import User.User;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Tarasevich Vladislav (ReemBer) on 30.04.2017.
 * A class, that used to creating and parsing User XML files
 */
public class UserParser extends DOMXmlParser<User>
{
    private static final String XSDSchema = "UserValidSchema.xsd";
    private static final String prefix = "D:\\WORKSPACE\\Archive\\src\\Server\\Users\\";
    private static final String suffix = ".xml";

    public static final Logger logger = Logger.getLogger(UserParser.class);

    /**
     * Method, that parse the User XML File and returns the User object
     * @param xmlFile path to User XML File
     * @return User object
     * @throws UserParsingException cannot use features, that provide parsing User XML File
     * @throws IllegalUserXMLFormatException The User XML File does not match pattern
     */
    @Override
    public User parse(String xmlFile) throws UserParsingException, IllegalUserXMLFormatException
    {
        User result = new User();
        try
        {
            //existence check
            exist(xmlFile); // throws FileNotFoundException

            //valid check
            validate(xmlFile);

            //parsing
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);
            document.getDocumentElement().normalize();

            //get name
            NodeList nameList = document.getElementsByTagName("Name");
            Node nameNode = nameList.item(0);
            String name = nameNode.getTextContent();

            //get Password
            NodeList passwordList = document.getElementsByTagName("Password");
            Node passwordNode = passwordList.item(0);
            String password = passwordNode.getTextContent();

            //get Access
            NodeList accessList = document.getElementsByTagName("Access");
            Node accessNode = accessList.item(0);
            String accessContent = accessNode.getTextContent();
            byte access = (byte) Integer.parseInt(accessContent);

            result.setName(name);
            result.setPassword(password);
            result.setAccess(access);

        }
        catch (IOException e)
        {
            e.printStackTrace();
            logger.fatal(e);
            throw new UserParsingException();
        }
        catch (NullPointerException ex)
        {
            ex.printStackTrace();
            logger.fatal(ex);
            throw new UserParsingException();
        }
        catch (SAXException exc)
        {
            exc.printStackTrace();
            logger.fatal(exc);
            throw new IllegalUserXMLFormatException();
        }
        catch (ParserConfigurationException excep)
        {
            excep.printStackTrace();
            logger.fatal(excep);
            throw new UserParsingException();
        }

        return result;
    }

    /**
     * Creating new User XML File
     * @param object A User object, that will be writing in the XML file
     * @throws UserExistException such User already exist
     * @throws UserCreatingException cannot use the XML Creating Features
     */
    @Override
    public void create(User object) throws UserExistException, UserCreatingException
    {
        try
        {
            if(object == null) throw new NullPointerException();

            //cannot create new user -- such user already exist
            if(exist(userFileName(object))) throw new UserExistException();

            //creating new xml document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            //creating elements
            Element user = document.createElement("User");
            Element name = document.createElement("Name");
            Element access = document.createElement("Access");
            Element password = document.createElement("Password");

            //creating document tree
            user.appendChild(name);
            user.appendChild(access);
            user.appendChild(password);
            document.appendChild(user);

            //set content of file
            name.setTextContent(object.getName());
            access.setTextContent(Byte.toString(object.getAccess()));
            password.setTextContent(object.getPassword());

            //writing document into the file
            writeDocument(document, userFileName(object));
        }
        catch (TransformerException e)
        {
            e.printStackTrace();
            logger.fatal(e);
            throw new UserCreatingException();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            logger.fatal(ex);
            throw new UserCreatingException();
        }
        catch (ParserConfigurationException exc)
        {
            exc.printStackTrace();
            logger.fatal(exc);
            throw new UserCreatingException();
        }
    }

    /**
     * Deleting old User XML File and creating new User XML File
     * @param oldObject will be deleted
     * @param newObject will be create
     * @throws UserCreatingException cannot use the XML Creating Features
     * @throws UserExistException such User already exist
     */
    @Override
    public void edit(User oldObject, User newObject) throws UserCreatingException, UserExistException
    {
        delete(oldObject);
        create(newObject);
    }

    /**
     * Deliting User XML File
     * @param object User, whose XML File will be deleted
     */
    @Override
    public void delete(User object)
    {
        try
        {
            exist(userFileName(object));

            File del = new File(userFileName(object));
            del.delete();
        }
        catch (FileNotFoundException e)
        {
            logger.info("Trying to delete no existence file");
        }
    }

    /**
     * Checking File existing
     * @param fileName checking file
     * @return true, if such file exist
     * @throws FileNotFoundException such file not found
     * @throws NullPointerException fileName parameter is null pointer
     */
    private boolean exist(String fileName) throws FileNotFoundException, NullPointerException
    {
        if(fileName == null) throw new NullPointerException();

        File file = new File(fileName);
        if(!file.exists())
        {
            throw new FileNotFoundException();
        }

        return true;
    }

    /**
     * Checking valid of XML file, by using XSD Schema
     * @param xmlFile
     * @throws SAXException file does not match XSD Schema
     * @throws IOException some file error
     */
    private void validate(String xmlFile) throws SAXException, IOException
    {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(XSDSchema));
        Validator validator = schema.newValidator();
        StreamSource streamSource = new StreamSource(xmlFile);
        validator.validate(streamSource);
    }

    /**
     * generating file name, that correspond with such User
     * @param user a User object for generating
     * @return User XML File name
     */
    private String userFileName(User user)
    {
        return prefix + user.getName() + suffix;
    }

    /**
     * Creating the User XML File and writing the User Document into this file
     * @param document The User document, that will be written
     * @param filePath path to the User XML File
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerConfigurationException
     * @throws FileNotFoundException
     * @throws TransformerException
     * @throws IOException
     */
    private void writeDocument(Document document, final String filePath) throws TransformerFactoryConfigurationError,
                                                                                TransformerConfigurationException,
                                                                                FileNotFoundException,
                                                                                TransformerException,
                                                                                IOException
    {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(document);
        File outfile = new File(filePath);
        outfile.createNewFile();
        FileOutputStream out = new FileOutputStream(outfile);

        StreamResult result = new StreamResult(out);
        transformer.transform(source, result);
    }
}
