package Server.RequestHandler;

import Server.Parser.UserParser;
import Server.PersonManager.PersonManager;

/**
 * Created by Tarasevich Vladislav on 01.05.2017.
 * A class, that process client requests and get answer
 */
public class RequestHandler
{
    private UserParser    userHandler;
    private PersonManager personHandler;

    public RequestHandler()
    {
        userHandler   = new UserParser();
        personHandler = new PersonManager();
    }
}
