package Server.RequestHandler;

import Answer.*;
import PersonalData.Person;
import Request.Request;
import Server.Parser.*;
import Server.PersonManager.PersonManager;
import User.AccessMask;
import User.User;
import org.apache.log4j.Logger;
import org.w3c.dom.Comment;

import java.util.Vector;

import static User.AccessMask.USER_EDIT;


/**
 * Created by Tarasevich Vladislav on 01.05.2017.
 * A class, that process client requests and get answer
 */
public class RequestHandler
{
    private static final String prefix = "D:\\WORKSPACE\\Archive\\src\\Server\\Users\\";
    private static final String suffix = ".xml";

    private UserParser    userHandler;
    private PersonManager personHandler;

    public static final Logger logger = Logger.getLogger(RequestHandler.class);

    public RequestHandler()
    {
        userHandler   = new UserParser();
        personHandler = new PersonManager();
    }

    public Answer process(Request request)
    {
        try {
            byte access = request.getAccessMask();
            switch (request.getType())
            {
                case VIEW:
                {
                    if (!((access & AccessMask.VIEW) == AccessMask.VIEW)) throw new IllegalAccessException();
                    Vector<Person> personVector = personHandler.getAllPersons();

                    return new Answer(true,true, access,  personVector, Comments.OK);
                }
                case EDIT:
                {
                    if (!((access & AccessMask.EDIT) == AccessMask.EDIT)) throw new IllegalAccessException();
                    personHandler.editPerson(request.getFirst(), request.getSecond());
                    Vector<Person> personVector = personHandler.getAllPersons();
                    return new Answer(true,true, (byte)0, personVector, Comments.OK);
                }
                case CREATE:
                {
                    if (!((access & AccessMask.CREATE) == AccessMask.CREATE)) throw new IllegalAccessException();
                    personHandler.createNewPerson(request.getFirst());
                    Vector<Person> personVector = personHandler.getAllPersons();
                    return new Answer(true,true, (byte)0, personVector, Comments.OK);
                }
                case DELETE:
                {
                    if (!((access & AccessMask.DELETE) == AccessMask.DELETE)) throw new IllegalAccessException();
                    personHandler.deletePerson(request.getFirst());
                    Vector<Person> personVector = personHandler.getAllPersons();
                    return new Answer(true,true, (byte)0, personVector, Comments.OK);
                }
                case USER_EDIT:
                {
                    if (!((access & USER_EDIT) == USER_EDIT)) throw new IllegalAccessException();
                    userHandler.edit(request.getFirstUser(), request.getSecondUser());
                    return new Answer(true,true, request.getSecondUser().getAccess(), null, Comments.OK);
                }
                case SIGNIN:
                {
                    User user = userHandler.parse(prefix + request.getUserName() + suffix);
                    return new Answer(true,true, user.getAccess(), null, Comments.OK);
                }
                case SIGNUP:
                {
                    userHandler.create(request.getFirstUser());
                    return new Answer(true,true, (byte)0, null, Comments.OK);
                }
                case USER_DELETE:
                {
                    if(!((access & USER_EDIT) == USER_EDIT)) throw new IllegalAccessException();
                    userHandler.delete(request.getFirstUser());
                    return new Answer(true,true,(byte)0, null, Comments.OK);
                }
                case CHANGE_PARSER:
                {
                    if(!((access & USER_EDIT) == USER_EDIT)) throw new IllegalAccessException();
                    personHandler.setParserType(request.getParserType());
                    return new Answer(true,true, (byte)0, null, Comments.OK);
                }
                default:
                {
                    logger.info("Access checking success");
                }
            }
        }
        catch (IllegalAccessException e)
        {
            logger.error(e);
            return generateIllegalAccessAnswer();
        }
        catch (PersonCreatingException e)
        {
            logger.fatal(e);
            return generateServerErrorAnswer();
        }
        catch (PersonExistException e)
        {
            logger.error(e);
            return new Answer(true, false, (byte)0, null, Comments.ALREADY_EXIST);
        }
        catch (UserExistException e)
        {
            logger.error(e);
            try
            {
                userHandler.create(request.getFirstUser());
            }
            catch (UserExistException ex) {/*loopback*/}
            catch (UserCreatingException ex)
            {
                logger.fatal(ex);
                return new Answer(true,false, (byte)0, null, Comments.SERVER_ERROR);
            }
            return new Answer(true,false, (byte)0, null, Comments.ALREADY_EXIST);
        }
        catch (UserCreatingException e)
        {
            logger.fatal(e);
            return new Answer(true,false, (byte)0, null, Comments.SERVER_ERROR);
        }
        catch (UserParsingException e)
        {
            logger.fatal(e);
            return new Answer(true,false,(byte)0, null, Comments.NO_EXIST);
        }
        catch (IllegalUserXMLFormatException e)
        {
            logger.fatal(e);
            return new Answer(true,false, (byte)0, null, Comments.SERVER_ERROR);
        }
        return null;
    }

    private Answer generateIllegalAccessAnswer()
    {
        return new Answer(false, false, (byte)0, null, Comments.ACCESS_ERROR);
    }

    private Answer generateServerErrorAnswer()
    {
        return new Answer(true, false, (byte)0, null, Comments.SERVER_ERROR);
    }
}
