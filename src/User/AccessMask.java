package User;

/**
 * Created by Tarasevich Vladislav on 26.04.2017.
 * @author ReemBer
 * This class is used to determine an approprimate user access level
 */
public class AccessMask
{
    public static final byte VIEW          = (byte)0b00000001;
    public static final byte CREATE        = (byte)0b00000010;
    public static final byte EDIT          = (byte)0b00000100;
    public static final byte DELETE        = (byte)0b00001000;
    public static final byte USER_VIEW     = (byte)0b00010000;
    public static final byte USER_EDIT     = (byte)0b00100000;
    public static final byte USER_DELETE   = (byte)0b01000000;
    public static final byte CHANGE_PARSER = (byte)0b01110000;
}
