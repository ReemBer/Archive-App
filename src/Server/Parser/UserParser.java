package Server.Parser;

import User.User;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by Tarasevich Vladislav (ReemBer) on 30.04.2017.
 * A class, that used to creating and parsing User XML files
 */
public class UserParser extends DOMXmlParser<User>
{
    private static final String XSDSchema = "UserValidSchema.xsd";

    /**
     * Method, that parse the User XML File and returns the User object
     * @param xmlFile path to User XML File
     * @return User object
     * @throws FileNotFoundException The User XML File not found
     * @throws NullPointerException The String User XML File name is null
     * @throws SAXException The User XML File is invalid (The file does not math the xsd schema)
     * @throws IOException validator cannot check the User XML File
     */
    @Override
    public User parse(String xmlFile) throws Exception
    {
        //existence check
        exist(xmlFile);

        //valid check
        validate(xmlFile);

        //parsing
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();

        //creating result
        User result = new User();

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

        return result;
    }

    @Override
    public void create(User object) throws Exception
    {

    }

    private void exist(String fileName) throws FileNotFoundException, NullPointerException
    {
        if(fileName == null) throw new NullPointerException();

        File file = new File(fileName);
        if(!file.exists())
        {
            throw new FileNotFoundException();
        }
    }

    private void validate(String xmlFile) throws SAXException, IOException
    {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(XSDSchema));
        Validator validator = schema.newValidator();
        StreamSource streamSource = new StreamSource(xmlFile);
        validator.validate(streamSource);
    }
}
