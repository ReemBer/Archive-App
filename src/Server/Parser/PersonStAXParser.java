package Server.Parser;

import PersonalData.Person;

/**
 * Created by Tarsevich Vladislav on 01.05.2017.
 */
public class PersonStAXParser extends XmlParser<Person>
{
    private static final String XSDSchema = "";

    public PersonStAXParser()
    {
        super(XSDSchema);
    }

    @Override
    public Person parse(String xmlFile) throws Exception {
        return null;
    }
}
