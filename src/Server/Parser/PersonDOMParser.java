package Server.Parser;

import PersonalData.Person;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Tarasevich Vladislav on 01.05.2017.
 * A class, that used to creating, editing, deleting, and parsing Personal Data XML files
 */
public class PersonDOMParser extends DOMXmlParser<Person>
{
    private static final String XSDSchema = "PersonValidSchema.xsd";
    private static final String prefix = "";
    private static final String suffix = ".xml";

    @Override
    public Person parse(String xmlFile) throws Exception {
        return null;
    }

    @Override
    public void create(Person object) throws Exception {

    }

    @Override
    public void edit(Person oldObject, Person newObject) throws Exception {

    }

    @Override
    public void delete(Person object) {

    }

    private boolean exsist(String fileName) throws FileNotFoundException, NullPointerException
    {

    }

    private void validate(String xmlFile) throws SAXException, IOException
    {

    }

    private String personFileName(Person person)
    {

    }

    private void writeDocument(Document document, final String filePath)
    {

    }
}
