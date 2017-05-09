package Client;

import Answer.Answer;
import PersonalData.Person;
import Request.Request;
import Server.PersonManager.ParserType;
import User.User;

import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

import static Request.RequestType.*;
import static Server.PersonManager.ParserType.DOM;

/**
 * Created by Tarasevich Vladislav on 04.05.2017.
 *
 */
public class ClientHandler
{
    private static final int serverPort = 4242;
    private static final String localHost = "127.0.0.1";

    private static final String hashAlgo = "SHA-256";

    private User currentUser = null;

    private Vector<User>   users;
    private Vector<Person> persons;

    private Socket socket = null;

    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ClientHandler() throws ConnectException
    {
        try
        {
            InetAddress inetAddress = InetAddress.getByName(localHost);
            socket = new Socket(inetAddress, serverPort);

            InputStream sin = socket.getInputStream();
            OutputStream sout = socket.getOutputStream();

            out = new ObjectOutputStream(sout);
            in = new ObjectInputStream(sin);
        }
        catch(IOException e)
        {
            e.printStackTrace();
            throw new ConnectException(e.toString());
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Answer signIn(String name, String password)
    {
        Answer answer = sendRequest(new Request(SIGNIN, name, password));
        if(answer.Success() && answer.Legal())
        {
            currentUser = new User(name, password, answer.getAccessMask());
        }
        return answer;
    }

    public Answer signUp(String name, String password)
    {
        Answer answer = sendRequest(new Request(SIGNUP, name, password));
        if(answer.Legal() && answer.Success())
        {
            currentUser = new User(name, password, answer.getAccessMask());
        }
        return  answer;
    }

    public Answer getPersons()
    {
        Answer answer = sendRequest(new Request(VIEW, currentUser.getAccess()));
        persons = answer.getPersonalData();
        return answer;
    }

    public Answer getUsers()
    {

        Answer answer = sendRequest(new Request(USER_VIEW, currentUser.getAccess()));
        users = answer.getUsers();
        return answer;
    }

    public Answer createPerson(Person person)
    {
        Answer answer = sendRequest(new Request(CREATE, person, currentUser.getAccess()));
        persons = answer.getPersonalData();
        return answer;
    }

    public Answer editPerson(Person oldPerson, Person newPerson)
    {
        Answer answer = sendRequest(new Request(oldPerson, newPerson, currentUser.getAccess()));
        persons = answer.getPersonalData();
        return answer;
    }

    public Answer deletePerson(Person person)
    {
        return sendRequest(new Request(DELETE, person, currentUser.getAccess()));
    }

    public Answer editUser(User oldUser, User newUser)
    {
        return sendRequest(new Request(oldUser, newUser, currentUser.getAccess()));
    }

    public Answer deleteUser(User user)
    {
        return sendRequest(new Request(user, currentUser.getAccess()));
    }

    public Answer changeParser(ParserType parserType)
    {
        return sendRequest(new Request(parserType, currentUser.getAccess()));
    }

    public Answer quit()
    {
        return sendRequest(new Request());
    }

    public User getUserAt(int index)
    {
        return users.get(index);
    }

    public Person getPersonAt(int index)
    {
        return persons.get(index);
    }

    private Answer sendRequest(Request request)
    {
        try
        {
            out.writeObject(request);
            out.flush();
            return (Answer)in.readObject();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
