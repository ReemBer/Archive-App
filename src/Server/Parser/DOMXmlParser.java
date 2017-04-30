package Server.Parser;

/**
 * Created by Tarasevich Vladislav (ReemBer) on 30.04.2017.
 * A class, that represents the Document Object Model
 */
public abstract class DOMXmlParser<PatternType> extends XmlParser<PatternType>
{
    public abstract void create(PatternType object) throws Exception;
    public abstract void edit(PatternType oldObject, PatternType newObject) throws Exception;
    public abstract void delete(PatternType object);
}
