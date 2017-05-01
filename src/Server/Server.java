package Server;

import Answer.Answer;
import Request.Request;
import Server.RequestHandler.RequestHandler;
import org.apache.log4j.Logger;

import java.io.*;
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

    public static final Logger logger = Logger.getLogger(Server.class);

    private final RequestHandler requestHandler;

    private Request request;
    private Answer  answer;

    private Socket socket;

    public Server()
    {
        requestHandler = new RequestHandler();
    }

    public void getStarted(Socket socket)
    {
        this.socket = socket;
        setDaemon(true);
        start();
    }

    @Override
    public void run()
    {
        try
        {
            InputStream  inputStream  = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();

            ObjectInputStream  objectInputStream  = new ObjectInputStream(inputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            String line = null;

            while (true)
            {
                request = (Request)objectInputStream.readObject();
                answer  = requestHandler.process(request);
                objectOutputStream.writeObject(answer);
                objectOutputStream.flush();
            }
        }
        catch(Exception e)
        {
            logger.error("FUCK", e);
        }
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
                e.printStackTrace();
                logger.fatal("Server is stopped",e);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                logger.fatal("Server is stopped", e);
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
                logger.fatal("Server working was ended", e);
            }
        }
        logger.info("Server working was ended");
        System.exit(0);
    }
}
