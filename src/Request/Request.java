package Request;

import PersonalData.Person;
import Server.PersonManager.ParserType;
import User.User;

import java.io.Serializable;

import static Server.PersonManager.ParserType.DOM;
import static User.AccessMask.USER_VIEW;

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

    //Quit
    public Request()
    {
        type = RequestType.QUIT;

        userName = null;
        password = null;

        first  = null;
        second = null;

        firstUser  = null;
        secondUser = null;

        parserType = DOM;
        accessMask = (byte)0;
    }

    //sign in or sign up
    public Request(RequestType type, String userName, String password)
    {
        if(type != RequestType.SIGNIN && type != RequestType.SIGNUP) throw new IllegalArgumentException();

        this.type = type;

        this.userName = userName;
        this.password = password;

        first  = null;
        second = null;

        firstUser  = new User(userName, password, (byte)0);
        secondUser = null;

        parserType = DOM;
        accessMask = (byte)0;
    }

    //create or delete
    public Request(RequestType type, Person first, byte  accessMask)
    {
        if(type != RequestType.CREATE && type != RequestType.DELETE) throw new IllegalArgumentException();

        this.type = type;
        userName = null;
        password = null;
        this.first  = first;
        this.second = null;
        firstUser = null;
        secondUser = null;
        parserType = DOM;
        this.accessMask = accessMask;
    }

    //edit
    public Request(Person first, Person second, byte accessMask)
    {
        type = RequestType.EDIT;

        userName = null;
        password = null;

        this.first = first;
        this.second = second;

        firstUser = null;
        secondUser = null;

        parserType = DOM;

        this.accessMask = accessMask;
    }

    //view Request
    public Request(RequestType type, byte accessMask)
    {
        if(type != RequestType.VIEW && type != RequestType.USER_VIEW) throw new IllegalArgumentException();

        this.type = type;
        this.accessMask = accessMask;

        userName = null;
        password = null;

        firstUser = null;
        secondUser = null;

        first = null;
        second = null;

        parserType = DOM;
    }

    //edit user
    public Request(User firstUser, User secondUser, byte accessMask)
    {
        type = RequestType.USER_EDIT;

        userName = null;
        password = null;

        first  = null;
        second = null;

        this.firstUser  =  firstUser;
        this.secondUser = secondUser;

        this.accessMask = accessMask;

        this.parserType = DOM;
    }

    //delete user
    public Request(User firstUser, byte accessMask)
    {
        type = RequestType.USER_DELETE;

        userName = null;
        password = null;

        first  = null;
        second = null;

        this.firstUser  = firstUser;
        secondUser = null;

        this.accessMask = accessMask;

        parserType = DOM;

    }

    //change parser
    public Request(ParserType parserType, byte accessMask)
    {
        type = RequestType.CHANGE_PARSER;

        userName = null;
        password = null;

        first  = null;
        second = null;

        firstUser  = null;
        secondUser = null;

        this.parserType = parserType;
        this.accessMask = accessMask;
    }

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
