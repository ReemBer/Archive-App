package Request;

import PersonalData.Person;
import Server.PersonManager.ParserType;
import User.User;

import java.io.Serializable;

/**
 * Created by Tarasevich Vladislav on 26.04.2017.
 * @author ReemBer
 * This class is used to send a request to the server.
 * Each fields of this class can only be initialized (no edit)
 */
public class Request implements Serializable
{
    private final RequestType type;

    private final String  userName;
    private final String  password;

    private final Person first;
    private final Person second;

    private final User firstUser;
    private final User secondUser;

    private final ParserType parserType;

    private final byte accessMask;

    public Request(RequestType type, String userName, String password,
                   Person first, Person second, User firstUser, User secondUser,
                   ParserType parserType, byte accessMask)
    {
        this.type = type;
        this.userName = userName;
        this.password = password;
        this.first = first;
        this.second = second;
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.parserType = parserType;
        this.accessMask = accessMask;
    }

    public RequestType getType() {
        return type;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Person getFirst() {
        return first;
    }

    public Person getSecond() {
        return second;
    }

    public User getFirstUser()
    {
        return firstUser;
    }

    public User getSecondUser() {
        return secondUser;
    }

    public ParserType getParserType()
    {
        return parserType;
    }

    public byte getAccessMask() {
        return accessMask;
    }
}
