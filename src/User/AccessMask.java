package User;

/**
 * Created by Tarasevich Vladislav on 26.04.2017.
 * @author ReemBer
 * This class is used to determine an approprimate user access level
 */
public class AccessMask
{
    public static final byte VIEW      = (byte)0x00000001b;
    public static final byte CREATE    = (byte)0x00000010b;
    public static final byte EDIT      = (byte)0x00000100b;
    public static final byte DELETE    = (byte)0x00001000b;
    public static final byte USER_EDIT = (byte)0x00010000b;
}
