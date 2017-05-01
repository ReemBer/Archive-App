package Server.Parser;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Tarasevich Vladislav on 28.04.2017.
 * @author ReemBer
 * @version 1.0 beta
 * This class is a parent-class for alss parsers, what will be created in Application
 */
public abstract class XmlParser<PatternType>
{
    private final String XSDSchema;

    public XmlParser(String XSDSchema)
    {
        this.XSDSchema = XSDSchema;
    }

    public abstract PatternType parse(String xmlFile) throws Exception;

    /**
     * Checking valid of XML file, by using XSD Schema
     * @param xmlFile
     * @throws SAXException file does not match XSD Schema
     * @throws IOException some file error
     */
    protected void validate(String xmlFile) throws SAXException, IOException
    {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(XSDSchema));
        Validator validator = schema.newValidator();
        StreamSource streamSource = new StreamSource(xmlFile);
        validator.validate(streamSource);
    }

    /**
     * Checking File existing
     * @param fileName checking file
     * @return true, if such file exist
     * @throws FileNotFoundException such file not found
     * @throws NullPointerException fileName parameter is null pointer
     */
    protected boolean exist(String fileName) throws FileNotFoundException, NullPointerException
    {
        if(fileName == null) throw new NullPointerException();

        File file = new File(fileName);
        if(!file.exists())
        {
            throw new FileNotFoundException();
        }

        return true;
    }
}
