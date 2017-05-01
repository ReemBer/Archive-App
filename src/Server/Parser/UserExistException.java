package Server.Parser;

/**
 * Created by Tarasevich Vladislav on 30.04.2017.
 */
public class UserExistException extends Exception
{
    public UserExistException(){}

    public UserExistException(Throwable cause)
    {
        super(cause);
    }
}
