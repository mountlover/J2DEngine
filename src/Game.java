import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.Timer;
/**
 * <b>Game</b> - Test driver for FrameScrollingField, and a self-controlling JFrame.
 * @author Yama H
 * @version 0.5a
 */
public class Game extends JFrame implements KeyListener
{
    public static final long REFRESH_RATE = 60;
	/**
     * This game's content pane.
     */
    protected Container contentPane;
    /**
     * Controls execution of Game Thread.
     */
    protected boolean gameEnd;
    /**
     * Main Character of this Game.
     */
    protected Character character;
    /**
     * Field upon which this game will take place.
     */
    private FrameScrollingField field;
    protected ActionListener refresh;
    protected Timer gameTimer = null;
    /**
     * Constructor for objects of type Game.
     * @param title title of window
     */
    public Game(String title)
    {
        super(title);
        gameEnd = false;
        setFocusable(true);
        addKeyListener(this);
        contentPane = getContentPane();
        contentPane.setFocusable(true);
        contentPane.addKeyListener(this);
        refresh = new ActionListener()
        {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				character.area.frame();
				if(!field.equals(character.area))
	            {
	                FrameScrollingField prev = field;
					setField((FrameScrollingField)character.area);
					prev.close(field);
	                contentPane.remove(0);
	                contentPane.add(character.area);
	                validate();
	                repaint();
	            }
	            getContentPane().requestFocus();
			}
        };
    }
    public void setField(FrameScrollingField field)
    {
    	this.field = field;
    	field.load();
    }
    public void start()
    {
    	if(gameTimer == null)
        {
        	gameTimer = new Timer((int)REFRESH_RATE, refresh);
        	gameTimer.start();
        }
    }
    public void stop()
    {
    	gameTimer.stop();
    	gameTimer = null;
    }
    /**
     * Main method.
     * @param args
     */
    public static void main(String[] args) 
    {
        Game frame = new Game("2D JOGL Engine Demo by Yama H");
        long time = System.currentTimeMillis();
        //this should be instantiated on a server
        FrameScrollingField[][] map = new FrameScrollingField[10][10];
        map[5][5] = new FrameScrollingField("scenarios/l1.png", "scenarios/l2.png");
        Script defaultScript = new Script()
		{
			int i = 1;
			public void run(Field f)
			{
		        Character c;
				f.addCharacter("fighter", new Coord(50,200)).defend(true);
		        c = f.addCharacter("fighter_ai", new Coord(50,300));
		        c.modEXP(10*i++);
		        c = f.addCharacter("sparky", new Coord(400,300));
		        c.modEXP(10*i++);
			}
		};
		for(int i=0; i<10; i++)
        {
        	for(int j=0; j<10; j++)
        	{
        		if(i != 5 || j != 5)
        		{
        			map[i][j] = new FrameScrollingField("scenarios/grassyfield.png", "scenarios/blank.png");
        		}
        		map[i][j].setScript(defaultScript);
        	}
        }
        for(int i=0; i<10; i++)
        {
        	for(int j=0; j<10; j++)
        	{
        		if(i != 0)
        		{
        			map[i][j].setWestField(map[i-1][j]);
        		}
        		if(i != 9)
        		{
        			map[i][j].setEastField(map[i+1][j]);
        		}
        		if(j != 0)
        		{
        			map[i][j].setNorthField(map[i][j-1]);
        		}
        		if(j != 9)
        		{
        			map[i][j].setSouthField(map[i][j+1]);
        		}
        	}
        }
        frame.setField(map[5][5]);
        frame.character = frame.field.addCharacter("fighter", new Coord(50,50));
        frame.contentPane = frame.getContentPane();
        frame.contentPane.setLayout(new BorderLayout());
        frame.contentPane.add(frame.field, BorderLayout.CENTER);
        frame.contentPane.validate();
        frame.contentPane.repaint();
        frame.setBounds(10,10,frame.field.X_SIZE,frame.field.Y_SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.validate();
        frame.repaint();
        frame.setVisible(true);
        frame.start();
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