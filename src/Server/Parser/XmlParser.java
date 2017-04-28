package Server.Parser;

/**
 * Created by Tarasevich Vladislav on 28.04.2017.
 * @author ReemBer
 * @version 1.0 beta
 * This class is a parent-class for alss parsers, what will be created in Application
 */
public abstract class XmlParser<PatternType>
{
    public abstract PatternType parse(String xmlFile);
}
