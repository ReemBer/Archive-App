package Answer;

import PersonalData.Person;

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

    private Comments comment;

    public Answer(boolean legal, boolean success, byte accessMask, Vector<Person> personalData, Comments comment)
    {
        this.legal = legal;
        this.success = success;
        this.accessMask = accessMask;
        this.personalData = new Vector<Person>(personalData);
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

    public Comments getComment() {
        return comment;
    }
}
