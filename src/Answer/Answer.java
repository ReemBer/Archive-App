package Answer;

import PersonalData.PersonalData;

import java.util.Vector;

/**
 * Created by Taraseich Vladislav on 26.04.2017.
 * @author ReemBer
 * This class is used to send answer for a client
 */
public class Answer
{
    private boolean legal;
    private boolean success;

    private byte accessMask;

    private Vector<PersonalData> personalData;

    private Comments comment;

    public Answer(boolean legal, boolean success, byte accessMask, Vector<PersonalData> personalData, Comments comment)
    {
        this.legal = legal;
        this.success = success;
        this.accessMask = accessMask;
        this.personalData = new Vector<PersonalData>(personalData);
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

    public Vector<PersonalData> getPersonalData() {
        return personalData;
    }

    public Comments getComment() {
        return comment;
    }
}
