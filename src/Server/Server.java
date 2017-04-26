package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by ReemBer on 24.04.2017.
 * @author ReemBer
 * @author gmail : tarasevich.vlad.97@gmail.com
 * @version 1.0
 * This class is a server.
 */
public class Server extends Thread // TODO: 27.04.2017 Добавить логирование
{
    private static final int port = 4242;

    private byte accessLevel;
    private Socket soket;

    public Server(){}

    public void getStarted(Socket socket)
    {
        this.soket = socket;
        setDaemon(true);
        start();
    }

    @Override
    public void run()
    {

    }

    public static void main(String args[])
    {
        ServerSocket serverSocket = null;
        try
        {
            try
            {
                InetAddress loopBack = InetAddress.getByName("localhost");

                serverSocket = new ServerSocket(port, 0, loopBack);

                while (true)
                {
                    //Waiting for connection
                    Socket socket = serverSocket.accept();

                    //Start processing client requests in a separate thread
                    new Server().getStarted(socket);
                }
            }
            catch (UnknownHostException e)
            {
                // TODO: 27.04.2017 Забацать обработку такого рода исключения
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
                // TODO: 27.04.2017 ...
            }
        }
        finally
        {
            try
            {
                if(serverSocket != null)
                {
                    serverSocket.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
                // TODO: 27.04.2017 ...
            }
        }

        System.exit(0);
    }
}
