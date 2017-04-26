package User;

/**
 * Created by Tarasevich vladislav on 26.04.2017.
 * @author Reember
 * This class is used to represent the user in the program
 */
public class User
{
    private static final String DEFAULT_NAME = "NO NAME";
    private static final byte DEFAULT_ACCESS = 0x00000000b;

    private String name;

    private final byte access; // Bitmask, that contain all access levels of User

    public User()
    {
        name   = DEFAULT_NAME;
        access = DEFAULT_ACCESS;
    }

    public User(String name, byte accessMask)
    {
        this.name   = name;
        this.access = accessMask;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getAccess() {
        return access;
    }
}
