package Server.RequestHandler;

import Answer.*;
import PersonalData.Person;
import Request.Request;
import Server.Parser.*;
import Server.PersonManager.PersonManager;
import User.AccessMask;
import User.User;
import org.apache.log4j.Logger;

import java.util.Vector;

import static User.AccessMask.USER_EDIT;
import static User.AccessMask.USER_VIEW;


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

    private Answer answer = null;

    public static final Logger logger = Logger.getLogger(RequestHandler.class);

    public RequestHandler()
    {
        userHandler   = new UserParser();
        personHandler = new PersonManager();
    }

    public Answer process(Request request)
    {
        Answer answer;
        answer = new Answer();
        answer.setLegal(true);
        answer.setSuccess(false);
        answer.setAccessMask((byte)0);
        answer.setPersonalData(null);
        answer.setUsers(null);
        answer.setComment(Comments.SERVER_ERROR);

        try {
            byte access = request.getAccessMask();
            switch (request.getType())
            {
                case VIEW:
                {
                    if (!((access & AccessMask.VIEW) == AccessMask.VIEW)) throw new IllegalAccessException();
                    Vector<Person> personVector = personHandler.getAllPersons();

                    answer = new Answer();
                    answer.setLegal(true);
                    answer.setSuccess(true);
                    answer.setAccessMask((byte)0);
                    answer.setPersonalData(personVector);
                    answer.setUsers(null);
                    answer.setComment(Comments.OK);
                    return answer;
                }
                case EDIT:
                {
                    if (!((access & AccessMask.EDIT) == AccessMask.EDIT)) throw new IllegalAccessException();
                    personHandler.editPerson(request.getFirst(), request.getSecond());
                    Vector<Person> personVector = personHandler.getAllPersons();

                    answer = new Answer();
                    answer.setLegal(true);
                    answer.setSuccess(true);
                    answer.setAccessMask((byte)0);
                    answer.setPersonalData(personVector);
                    answer.setUsers(null);
                    answer.setComment(Comments.OK);
                    return answer;
                }
                case CREATE:
                {
                    if (!((access & AccessMask.CREATE) == AccessMask.CREATE)) throw new IllegalAccessException();
                    personHandler.createNewPerson(request.getFirst());
                    Vector<Person> personVector = personHandler.getAllPersons();

                    answer = new Answer();
                    answer.setLegal(true);
                    answer.setSuccess(true);
                    answer.setAccessMask((byte)0);
                    answer.setPersonalData(personVector);
                    answer.setUsers(null);
                    answer.setComment(Comments.OK);
                    return answer;
                }
                case DELETE:
                {
                    if (!((access & AccessMask.DELETE) == AccessMask.DELETE)) throw new IllegalAccessException();
                    personHandler.deletePerson(request.getFirst());
                    Vector<Person> personVector = personHandler.getAllPersons();

                    answer = new Answer();
                    answer.setLegal(true);
                    answer.setSuccess(true);
                    answer.setAccessMask((byte)0);
                    answer.setPersonalData(personVector);
                    answer.setUsers(null);
                    answer.setComment(Comments.OK);
                    return answer;
                }
                case USER_EDIT:
                {
                    if (!((access & USER_EDIT) == USER_EDIT)) throw new IllegalAccessException();
                    userHandler.edit(request.getFirstUser(), request.getSecondUser());
                    Vector<User> userVector = userHandler.getUsers();

                    answer = new Answer();
                    answer.setLegal(true);
                    answer.setSuccess(true);
                    answer.setAccessMask((byte)0);
                    answer.setPersonalData(null);
                    answer.setUsers(userVector);
                    answer.setComment(Comments.OK);
                    return answer;
                }
                case USER_VIEW:
                {
                    if(!((access & USER_VIEW) == USER_VIEW)) throw new IllegalAccessException();

                    Vector<User> userVector = userHandler.getUsers();

                    answer = new Answer();
                    answer.setLegal(true);
                    answer.setSuccess(true);
                    answer.setAccessMask((byte)0);
                    answer.setPersonalData(null);
                    answer.setUsers(userVector);
                    answer.setComment(Comments.OK);
                    return answer;
                }
                case SIGNIN:
                {
                    User user = userHandler.parse(prefix + request.getUserName() + suffix);

                    if(!(user.getPassword().equals(request.getPassword()))) throw new UserExistException();

                    answer = new Answer();
                    answer.setLegal(true);
                    answer.setSuccess(true);
                    answer.setAccessMask(user.getAccess());
                    answer.setPersonalData(null);
                    answer.setUsers(null);
                    answer.setComment(Comments.OK);
                    return answer;
                }
                case SIGNUP:
                {
                    userHandler.create(request.getFirstUser());

                    answer = new Answer();
                    answer.setLegal(true);
                    answer.setSuccess(true);
                    answer.setAccessMask((byte)0);
                    answer.setPersonalData(null);
                    answer.setUsers(null);
                    answer.setComment(Comments.OK);
                    return answer;
                }
                case USER_DELETE:
                {
                    if(!((access & USER_EDIT) == USER_EDIT)) throw new IllegalAccessException();
                    userHandler.delete(request.getFirstUser());
                    Vector<User> userVector = userHandler.getUsers();

                    answer = new Answer();
                    answer.setLegal(true);
                    answer.setSuccess(true);
                    answer.setAccessMask((byte)0);
                    answer.setPersonalData(null);
                    answer.setUsers(userVector);
                    answer.setComment(Comments.OK);
                    return answer;
                }
                case CHANGE_PARSER:
                {
                    if(!((access & USER_EDIT) == USER_EDIT)) throw new IllegalAccessException();
                    personHandler.setParserType(request.getParserType());

                    answer = new Answer();
                    answer.setLegal(true);
                    answer.setSuccess(true);
                    answer.setAccessMask((byte)0);
                    answer.setPersonalData(null);
                    answer.setUsers(null);
                    answer.setComment(Comments.OK);
                    return answer;
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

            answer = new Answer();
            answer.setLegal(true);
            answer.setSuccess(false);
            answer.setAccessMask((byte)0);
            answer.setPersonalData(null);
            answer.setUsers(null);
            answer.setComment(Comments.ALREADY_EXIST);
            return answer;
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

                answer = new Answer();
                answer.setLegal(true);
                answer.setSuccess(false);
                answer.setAccessMask((byte)0);
                answer.setPersonalData(null);
                answer.setUsers(null);
                answer.setComment(Comments.SERVER_ERROR);
                return answer;
            }

            answer = new Answer();
            answer.setLegal(true);
            answer.setSuccess(false);
            answer.setAccessMask((byte)0);
            answer.setPersonalData(null);
            answer.setUsers(null);
            answer.setComment(Comments.ALREADY_EXIST);
            return answer;
        }
        catch (UserCreatingException e)
        {
            logger.fatal(e);

            answer = new Answer();
            answer.setLegal(true);
            answer.setSuccess(false);
            answer.setAccessMask((byte)0);
            answer.setPersonalData(null);
            answer.setUsers(null);
            answer.setComment(Comments.SERVER_ERROR);
            return answer;
        }
        catch (UserParsingException e)
        {
            logger.fatal(e);

            answer = new Answer();
            answer.setLegal(true);
            answer.setSuccess(false);
            answer.setAccessMask((byte)0);
            answer.setPersonalData(null);
            answer.setUsers(null);
            answer.setComment(Comments.NO_EXIST);
            return answer;
        }
        catch (IllegalUserXMLFormatException e)
        {
            logger.fatal(e);
            answer = new Answer();
            answer.setLegal(true);
            answer.setSuccess(false);
            answer.setAccessMask((byte)0);
            answer.setPersonalData(null);
            answer.setUsers(null);
            answer.setComment(Comments.SERVER_ERROR);
            return answer;
        }
        return answer;
    }

    private Answer generateIllegalAccessAnswer()
    {
        answer = new Answer();
        answer.setLegal(false);
        answer.setSuccess(false);
        answer.setAccessMask((byte)0);
        answer.setPersonalData(null);
        answer.setUsers(null);
        answer.setComment(Comments.ACCESS_ERROR);
        return answer;
    }

    private Answer generateServerErrorAnswer()
    {
        answer = new Answer();
        answer.setLegal(false);
        answer.setSuccess(false);
        answer.setAccessMask((byte)0);
        answer.setPersonalData(null);
        answer.setUsers(null);
        answer.setComment(Comments.SERVER_ERROR);
        return answer;
    }
}
