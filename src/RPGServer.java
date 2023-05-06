import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * <b>RPGServer</b> - Establishes a main server for the RPGEngine.
 * @author Yama H
 * @version 0.2a
 */
public class RPGServer
{
    /**
     * Default port to accept incoming requests on.
     */
    public static final int DEFAULT_PORT = 4242;
    /**
     * Default number of requests to queue.
     */
    public static final int DEFAULT_BACKLOG = 2;
    /**
     * Domain name of online server.
     */
    public static final String DOMAIN_NAME = "www.serversite.com";
    /**
     * True when establishing a server to serve requests on the same computer.
     */
    public static final boolean OFFLINE_MODE = false;
    protected static ServerSocket serverSocket;
    /**
     * Main method.
     * @param args custom port number to establish server on.
     */
    public static void main(String args[])
    {
        Field field = new Field("l1.png", "l2.png");
        if(OFFLINE_MODE)
        {
            try
            {
                serverSocket = new ServerSocket(DEFAULT_PORT);
            }
            catch(IOException ioe)
            {
                ioe.printStackTrace();
            }
        }
        else
        {
            try
            {
                serverSocket = new ServerSocket(Integer.parseInt(args[0]), 
                    DEFAULT_BACKLOG, InetAddress.getByName(DOMAIN_NAME));
            }
            catch(Exception e)
            {
                try
                {
                    System.out.println("Reverting to default port on local IP...");
                    serverSocket = new ServerSocket(DEFAULT_PORT, DEFAULT_BACKLOG,
                        InetAddress.getLocalHost());
                }
                catch(IOException ioe)
                {
                    ioe.printStackTrace();
                    System.out.println("Failed establishing server.");
                    return;
                }
            }
            Socket playerSocket;
            ObjectOutputStream stream;
            try
            {
                playerSocket = serverSocket.accept();
                stream = new ObjectOutputStream(playerSocket.getOutputStream());
                stream.writeObject(field);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}