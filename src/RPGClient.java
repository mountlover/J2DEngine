import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.JFrame;
/**
 * <b>RPGClient</b> - A client for accessing an RPGServer.
 * @author Yama H
 * @version 0.2a
 */
public class RPGClient extends JFrame implements KeyListener
{
    /**
     * Default domain name of online server.
     */
    public static final String DOMAIN_NAME = "www.serversite.com";
    /**
     * Default port to accept incoming requests on.
     */
    public static final int DEFAULT_PORT = 4242;
    /**
     * Number of times to recurse certain operations before giving up and failing.
     */
    public static final int RECURSIVE_LIMIT = 10;
    /**
     * This RPGClient's content pane.
     */
    protected static Container contentPane;
    /**
     * Controls execution of gameThread.
     */
    protected boolean gameEnd;
    /**
     * The Game that this class controls.
     */
    protected static RPGClient frame;
    /**
     * Main Character of this RPGClient.
     */
    protected static Character character;
    private static Socket server;
    /**
     * Constructor for objects of type Game.
     * @param title title of window
     */
    public RPGClient(String title)
    {
        super(title);
        gameEnd = false;
        setFocusable(true);
        addKeyListener(this);
        contentPane = getContentPane();
        contentPane.setFocusable(true);
        contentPane.addKeyListener(this);
    }
    /**
     * Main method
     * @param args custom server domain and port number to access server on <br>
     * i.e. C:\> java RPGClient www.serversite.com 4242
     */
    public static void main(String args[])
    {
        long time = System.currentTimeMillis();
        try
        {
            server = new Socket(InetAddress.getLocalHost(), Integer.parseInt(args[0]));
        }
        catch(Exception e)
        {
            try
            {
                System.out.println("Reverting to default port...");
                server = new Socket(InetAddress.getLocalHost(), DEFAULT_PORT);
            }
            catch(Exception e2)
            {
                e2.printStackTrace();
                return;
            }
        }
        Field field;
        try
        {
            ObjectInputStream stream = new ObjectInputStream(server.getInputStream());
            field = (Field)stream.readObject();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }
        frame = new RPGClient("RPGClient");
        character = field.addCharacter("fighter", new Coord(50,50));
        field.addCharacter("fighter", new Coord(50,200)).defend(true);
        field.addCharacter("fighter_ai", new Coord(50,300));
        contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(field, BorderLayout.CENTER);
        contentPane.validate();
        contentPane.repaint();
        frame.setBounds(10,10,field.X_SIZE,field.Y_SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.validate();
        frame.repaint();
        frame.setVisible(true);
        // to make sure this isn't getting too intensive...
        System.out.println(System.currentTimeMillis() - time + "ms elapsed");
        Thread gameThread = new Thread()
        {
            public void run()
            {
                while(!frame.gameEnd)
                {
                    pause(100);
                    //frame.validate();
                    //frame.repaint();
                    //contentPane.validate();
                    //contentPane.repaint();
                    frame.getContentPane().requestFocus();
                }
            }
            /**
             * Pauses the thread temporarily. Makes several more attempts if pausing fails.
             * @param time time to pause for
             */
            public void pause(long time)
            {
                recursivePause(time, RECURSIVE_LIMIT);
            }
            private void recursivePause(long time, int tries) 
            {
                if(tries != 0)
                {
                    try 
                    { 
                        sleep(time);
                    }
                    catch(Exception ignored) 
                    {
                        recursivePause(time, tries-1);
                    }
                }
            }
        };
        gameThread.start();
        
    }
    /**
     * Reacts to KeyEvents.
     * @param key the KeyEvent
     */
    public void keyPressed(KeyEvent key)
    {
        if(key.getKeyCode() == KeyEvent.VK_UP)
            character.upPressed(true);
        else if(key.getKeyCode() == KeyEvent.VK_RIGHT)
            character.rightPressed(true);
        else if(key.getKeyCode() == KeyEvent.VK_DOWN)
            character.downPressed(true);
        else if(key.getKeyCode() == KeyEvent.VK_LEFT)
            character.leftPressed(true);
        else if(key.getKeyCode() == KeyEvent.VK_S)
            character.strafe(true);
        else if(key.getKeyCode() == KeyEvent.VK_D)
            character.defend(true);
        else if(key.getKeyCode() == KeyEvent.VK_F)
            character.turn(true);
    }
    /**
     * Reacts to KeyEvents.
     * @param key the KeyEvent
     */
    public void keyReleased(KeyEvent key)
    {
        if(key.getKeyCode() == KeyEvent.VK_UP)
            character.upPressed(false);
        else if(key.getKeyCode() == KeyEvent.VK_RIGHT)
            character.rightPressed(false);
        else if(key.getKeyCode() == KeyEvent.VK_DOWN)
            character.downPressed(false);
        else if(key.getKeyCode() == KeyEvent.VK_LEFT)
            character.leftPressed(false);
        else if(key.getKeyCode() == KeyEvent.VK_A)
            character.attack();
        else if(key.getKeyCode() == KeyEvent.VK_S)
            character.strafe(false);
        else if(key.getKeyCode() == KeyEvent.VK_D)
            character.defend(false);
        else if(key.getKeyCode() == KeyEvent.VK_F)
            character.turn(false);
        else if(key.getKeyCode() == KeyEvent.VK_C)
            character.shoot();
    }
    /**
     * Reacts to KeyEvents.
     * @param key the KeyEvent
     */
    public void keyTyped(KeyEvent key)
    {}
}