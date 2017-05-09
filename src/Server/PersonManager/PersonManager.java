package Server.PersonManager;

import PersonalData.Person;
import Server.Parser.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Vector;

/**
 * Created by Tarasevich Vladislav on 01.05.2017.
 * A class, that designed for the Personal data management, using by the Person Parsers
 * @version 1.0 beta
 */
public class PersonManager
{
    private ParserType        parserType;
    private XmlParser<Person> reader;

    private static final String prefix = "D:\\WORKSPACE\\Archive\\src\\Server\\Persons\\";
    private static final String suffix = ".xml";

    private final PersonDOMParser editor;

    public static final Logger logger = Logger.getLogger(PersonManager.class);

    {
        editor = new PersonDOMParser();
    }

    public PersonManager()
    {
        reader = new PersonDOMParser();
        parserType = ParserType.DOM;
    }

    public void setParserType(ParserType parserType)
    {
        if(this.parserType == parserType) return;

        this.parserType = parserType;
        switch (parserType)
        {
            case DOM:
            {
                reader = new PersonDOMParser();
                break;
            }
            case SAX:
            {
                reader = new PersonSAXParser();
                break;
            }
            case StAX:
            {
                reader = new PersonStAXParser();
                break;
            }
        }
    }

    public Vector<Person> getAllPersons()
    {

        Vector<Person> persons = new Vector<>();

        File folder = new File(prefix);


        String[] fileNames = folder.list((dir, name) -> name.endsWith(suffix));

        for(String fileName : fileNames)
        {
            Person person;
            try
            {
                person = reader.parse(prefix + fileName);
                persons.add(person);
            }
            catch (Exception e)
            {
                logger.error(e);
            }
        }

        return persons;
    }

    public void createNewPerson(Person person) throws PersonCreatingException, PersonExistException
    {
        editor.create(person);
    }

    public void editPerson(Person oldPerson, Person newPerson) throws PersonCreatingException
    {
        try
        {
            editor.edit(oldPerson, newPerson);
        }
        catch (PersonExistException e)
        {
            e.printStackTrace();
            logger.fatal(e);
        }
    }

    public void deletePerson(Person person)
    {
        editor.delete(person);
    }
}
