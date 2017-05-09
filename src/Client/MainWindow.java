package Client;


import java.io.IOException;

/**
 * Created by Tarasevich Vladislav on 02.05.2017.
 * The Main Window of Application.
 */
public class MainWindow
{
    public static void main(String[] args)
    {
        try
        {
            GUIHandler guiHandler = new GUIHandler();
            guiHandler.showSignIn();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            GUIHandler.showConnectionError();
            System.exit(1);
        }
    }
}
