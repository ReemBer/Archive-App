package Answer;

import PersonalData.Person;
import User.User;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by Taraseich Vladislav on 26.04.2017.
 * @author ReemBer
 * This class is used to send answer for a client
 */
public class Answer implements Serializable
{
    private boolean legal;
    private boolean success;

    private byte accessMask;

    private Vector<Person> personalData;

    private Vector<User>   users;

    private Comments comment;

    public Answer()
    {
        legal = false;
        success = false;
        accessMask = 0;
        users = null;
        comment = Comments.OK;
    }

    public void setLegal(boolean legal) {
        this.legal = legal;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setAccessMask(byte accessMask) {
        this.accessMask = accessMask;
    }

    public void setPersonalData(Vector<Person> personalData) {
        this.personalData = personalData;
    }

    public void setUsers(Vector<User> users) {
        this.users = users;
    }

    public void setComment(Comments comment) {
        this.comment = comment;
    }

    public boolean Legal() {
        return legal;
    }

    public boolean Success() {
        return success;
    }

    public byte getAccessMask() {
        return accessMask;
    }

    public Vector<Person> getPersonalData() {
        return personalData;
    }

    public Vector<User> getUsers() {
        return users;
    }

    public Comments getComment() {
        return comment;
    }
}
