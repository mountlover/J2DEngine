import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.util.Set;
/**
 * <b>Field</b> - A Canvas that also manages a game.
 * @author Yama H
 * @version 0.6a
 */
public class Field extends Component
{
    /**
     * Color to perceive as transparent when reading images.
     */
    public static final int TRANSPARENT = new Color(254,66,254).getRGB();
    /**
     * Color to recognize as a marker in a file.
     */
    public static final int MARKERCOL = new Color(66,254,66).getRGB();
    /**
     * Value to recognize as a marker in an image.
     */
    public static final int MARKER = -1;
    /**
     * Constant for declaring that an image has loaded successfully.
     */
    public static final int IMAGE_LOAD = 39;
    /**
     * Width of window (in pixels).
     */
    public static final int X_SIZE = 800;
    /**
     * Height of window (in pixels).
     */
    public static final int Y_SIZE = 600;
    /**
     * Maximum amount of simultaneous characters.
     */
    public static final int MAX_CHAR_AMT = 16;
    /**
     * Original draw permissions of Field.
     */
    protected boolean origDrawable[][];
    /**
     * Current draw permissions of Field.
     */
    protected boolean drawable[][];
    /**
     * Map of all Unique Character ID's on the Field.
     */
    protected int ucidMap[][];
    /**
     * Original Field image.
     */
    protected int origBitmap[][];
    /**
     * Current Field image.
     */
    protected BufferedImage bitmap;
    /**
     * Keeps track of all Characters on the Field.
     */
    protected UCIDKeeper ucidKeeper;
    /**
     * Controls running of Field Thread.
     */
    protected boolean gameEnd;
    protected String l1;
    protected String l2;
    protected Script startupScript = null;
    /**
     * Constructor for objects of type Field.
     * @param layer1 name of first layer image
     * @param layer2 name of second layer image
     */
    public Field(String layer1, String layer2)
    {
        super();
        l1 = layer1;
        l2 = layer2;
    	gameEnd = false;
    	free();
    }
    
    protected void setScript(Script script)
    {
    	startupScript = script;
    }
    
    protected void allocate()
    {
        ucidKeeper = new UCIDKeeper(MAX_CHAR_AMT);
        drawable = new boolean[X_SIZE][Y_SIZE];
        origDrawable = new boolean[X_SIZE][Y_SIZE];
        ucidMap = new int[X_SIZE][Y_SIZE];
        origBitmap = new int[X_SIZE][Y_SIZE];
        bitmap = new BufferedImage(X_SIZE, Y_SIZE, BufferedImage.TYPE_INT_RGB);
        loadLayer1(l1);
        loadLayer2(l2);
    }
    
    protected void free()
    {
        ucidKeeper = null;
        drawable = null;
        origDrawable = null;
        ucidMap = null;
        origBitmap = null;
        bitmap = null;
    }

    public void frame()
    {
    	try
        {
            for(Integer i : ucidKeeper.getUCIDs())
                ucidKeeper.getChar(i).frame();
        }
        catch(java.util.ConcurrentModificationException cme){}
        repaint();
        //display();
    }
    
    public void paint(Graphics g)
    {
        g.drawImage(bitmap, 0, 0, null);
    }
    /**
     * Report an attack to the field.
     * @param src Character attacking
     * @param pixels pixels of attack sprite
     * @param initPos coordinates where attack is taking place (top right corner)
     * @param longatk true if the attack is a long range attack
     * @return true if the attack hit someone
     */
    public boolean reportAttack(Character src, Set<Pixel> pixels, Coord initPos, 
        boolean longatk)
    {
        Character dest;
        for(Pixel p : pixels)
        {
            if(ucidMap[p.getX()+initPos.getX()][p.getY()+initPos.getY()] != 0)
            {
                if(!ucidKeeper.getUCIDs().contains(ucidMap[p.getX()+initPos.getX()]
                                                           [p.getY()+initPos.getY()]))
                    return false;
                dest = ucidKeeper.getChar(ucidMap[p.getX()+initPos.getX()]
                                                  [p.getY()+initPos.getY()]);
                if(dest.ucid != src.ucid)
                {
                    if(longatk)
                    {
                        if(dest.modHP(src.longatk))
                            src.modEXP(100+dest.lv-src.lv);
                        System.out.println("Character " + src.ucid + " attacked Character " 
                            + dest.ucid + " for " + src.longatk + " HP!\n");
                    }
                    else
                    {
                        if(dest.modHP(src.atk))
                            src.modEXP(100+dest.lv-src.lv);
                        System.out.println("Character " + src.ucid + " attacked Character " 
                            + dest.ucid + " for " + src.atk + " HP!\n");
                    }
                    return true;
                }
            }
        }
        return false;
    }
    public boolean reportAttack(Character src, Coord topLeft, Coord bottomRight, 
        boolean longatk)
    {
        for(int i=topLeft.getX(); i<bottomRight.getX(); i++)
        {
            for(int j=topLeft.getY(); j<bottomRight.getY(); j++)
            {
                if(ucidMap[i][j] != 0)
                {
                    // TODO do i want to be doing this?
                }
            }
        }
        return false;
    }
    /**
     * Adds a Character to the Field. Null if charType is invalid or if the 
     * field is currently full.
     * @param charType a string representation of the type of Character to be made
     * @param init initial coordinate of the new Character
     * @return a handle to the Character.
     */
    public Character addCharacter(String charType, Coord init)
    {
        if(ucidKeeper.full())
            return null;
        if(charType.toLowerCase().equals("fighter"))
            return new Fighter(this, init);
        if(charType.toLowerCase().equals("fighter_ai"))
            return new FighterAI(this, init);
        if(charType.toLowerCase().equals("sparky"))
            return new Sparky(this, init);
        return null;
    }
    /**
     * Removes a Character from the Field.
     * @param ucid UCID of the Character being removed
     */
    public void removeCharacter(int ucid)
    {
        try
        {
            ucidKeeper.removeChar(ucid);
        }
        catch(java.util.ConcurrentModificationException cme)
        {
            removeCharacter(ucid);
        }
    }
    /**
     * Loads layer 1 (scenery layer) onto bitmap.
     * @param filename filename of layer 1
     */
    protected void loadLayer1(String filename)
    {
        Toolkit toolkit = getToolkit();
        Graphics g;
        Image l1 = toolkit.getImage(getClass().getResource(filename));
        toolkit.prepareImage(l1, -1, -1, null);
        while(toolkit.checkImage(l1, -1, -1, this) != IMAGE_LOAD){}
        BufferedImage imgbuf = new BufferedImage(l1.getWidth(null), 
            l1.getHeight(null), BufferedImage.TYPE_INT_RGB);
        g = imgbuf.getGraphics();
        g.drawImage(l1, 0, 0, null);
        for(int i=0; i<Y_SIZE; i++)
        {
            for(int j=0; j<X_SIZE; j++)
            {
                origBitmap[j][i] = imgbuf.getRGB(j,i);
                bitmap.setRGB(j, i, imgbuf.getRGB(j,i));
                origDrawable[j][i] = true;
                drawable[j][i] = true;
            }
        }
    }
    /**
     * Load layer 2 (obstructions layer) onto bitmap.
     * @param filename filename of layer 2
     */
    protected void loadLayer2(String filename)
    {
        Toolkit toolkit = getToolkit();
        Graphics g;
        Image l2 = toolkit.getImage(getClass().getResource(filename));
        toolkit.prepareImage(l2, -1, -1, null);
        while(toolkit.checkImage(l2, -1, -1, this) != IMAGE_LOAD){}
        BufferedImage imgbuf = new BufferedImage(l2.getWidth(null), 
            l2.getHeight(null), BufferedImage.TYPE_INT_ARGB_PRE);
        g = imgbuf.getGraphics();
        g.drawImage(l2, 0, 0, null);
        for(int i=0; i<Y_SIZE; i++)
        {
            for(int j=0; j<X_SIZE; j++)
            {
                if(imgbuf.getRGB(j,i) != TRANSPARENT)
                {
                    origBitmap[j][i] = imgbuf.getRGB(j,i);
                    bitmap.setRGB(j, i, imgbuf.getRGB(j,i));
                    origDrawable[j][i] = false;
                    drawable[j][i] = false;
                }
            }
        }
    }
}