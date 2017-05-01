package Server.Parser;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Tarasevich Vladislav on 28.04.2017.
 * @author ReemBer
 * @version 1.0 beta
 * This class is a parent-class for alss parsers, what will be created in Application
 */
public abstract class XmlParser<PatternType>
{
    public abstract PatternType parse(String xmlFile) throws Exception;

    protected void validate(String xmlFile) throws Exception{}

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
