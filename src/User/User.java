package User;

import java.io.Serializable;

/**
 * Created by Tarasevich vladislav on 26.04.2017.
 * @author Reember
 * This class is used to represent the user in the program
 */
public class User implements Serializable
{
    private static final String DEFAULT_NAME = "NO NAME";
    private static final byte DEFAULT_ACCESS = 0x00000000b;
    private static final String DEFAULT_PASS = "1488228666";

    private String name;
    private String password;
    private byte access; // Bitmask, that contain all access levels of User

    public User()
    {
        name     = DEFAULT_NAME;
        password = DEFAULT_PASS;
        access   = DEFAULT_ACCESS;
    }

    public User(String name, String password, byte accessMask)
    {
        this.name     = name;
        this.password = password;
        this.access   = accessMask;
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

    public void setAccess(byte access) {
        this.access = access;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
