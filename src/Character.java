import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.HashSet;
/**
 * <b>Character</b> - Common functions of all Character types.
 * @author Yama H
 * @version 0.9b
 */
public abstract class Character
{
    /**
     * Max width of a Character sprite.
     */
    public static final int MAX_SIZE_X = 100;
    /**
     * Max height of a Character sprite.
     */
    public static final int MAX_SIZE_Y = 100;
    /**
     * Serves as a pointer to the Character's state sprites 
     * (spriteSheet[0] - spriteSheet[3] by default)
     */
    protected Sprite charbmp; // just used as a pointer to the 4 states
    /**
     * To be specified in subclass--default is: <br>
     * spriteSheet[0] = stand_right <br>
     * spriteSheet[1] = stand_left <br>
     * spriteSheet[2] = walk_right <br>
     * spriteSheet[3] = walk_left <br>
     * spriteSheet[4] = shield_right <br>
     * spriteSheet[5] = shield_left <br>
     * spriteSheet[6] = sword_right <br>
     * spriteSheet[7] = sword_left <br>
     * spriteSheet[8] = projectile_right <br>
     * spriteSheet[9] = projectile_left
     */
    protected Sprite[] spriteSheet;
    /**
     * Current total HP of this Character.
     */
    protected int totHP;
    /**
     * Current HP of this Character.
     */
    protected int hp;
    /**
     * Current exp of this Character.
     */
    protected int exp;
    /**
     * Current level of this Character.
     */
    protected int lv;
    /**
     * Current attack strength of this Character.
     */
    protected int atk;
    /**
     * Current long range attack strength of this Character.
     */
    protected int longatk;
    /**
     * Unique Character ID of this Character.
     */
    protected int ucid;
    /**
     * Represents current state sprite of Character as follows:<br>
     * 1 - stand_right <br>
     * 2 - stand_left <br>
     * 3 - walk_right <br>
     * 4 - walk_left
     */
    protected int anim;
    /**
     * Represents the current method of movement of the Character as follows: <br>
     * 1 - walk <br>
     * 2 - strafe <br>
     * 3 - turn
     */
    protected int action;
    /**
     * Projectile gauge.
     */
    protected double projectiles;
    /**
     * Melee attack gauge.
     */
    protected double melees;
    /**
     * True if Character is defending.
     */
    protected boolean defending;
    /**
     * Field in which this Character resides.
     */
    protected Field area;
    /**
     * Current position of this Character.
     */
    protected Coord pos;
    /**
     * Spawn position of this Character.
     */
    protected Coord initPos;
    /**
     * Projectile representing this Character's shield.
     */
    protected Projectile shield;
    /*
     * To be specified in subclass.
     * Contains filenames of sprites in spriteSheet, index values should
     * correspond to those of spriteSheet.
     */
    private String[] spriteFiles;
    /*
     * Booleans representing depression of arrow keys.
     */
    private boolean up, right, down, left;
    /*
     * Number of sprites that this Character needs.
     */
    private int numSprites;
    /*
     * Loop index for movement.
     */
    private int frame;
    
    /**
     * Constructor for objects of type Character. Throws an
     * IllegalStateException if the character cannot be drawn at the specified
     * coordinates.
     * @param f Field this Character is to be drawn upon
     * @param initPos initial position of this Character
     * @param spriteFiles array of all Filenames of all Sprites to be loaded 
     * into this Character
     */
    protected Character(Field f, Coord initPos, String[] spriteFiles)
    {
        this.initPos = initPos;
        projectiles = maxProjectiles();
        melees = maxProjectiles();
        numSprites = spriteFiles.length;
        spriteSheet = new Sprite[numSprites];
        this.spriteFiles = spriteFiles;
        area = f;
        action = 1; // walk by default
        newUCID();
        loadImages();
        imgStandRight();
        if(!draw(initPos))
            new IllegalStateException("Invalid Initial Position.");
        exp = 0;
        lv = 1;
        totHP = totHP();
        hp = totHP;
        atk = atk();
        longatk = longatk();
    }
    
    /*
     * All of the following classes should be specified in the subclass
     * as per the necessities of the type of Character being implemented.
     */
    protected abstract void attack();
    protected abstract void defend(boolean defense);
    protected abstract void shoot();
    protected abstract void die();
    protected abstract int expToNextLv();
    protected abstract int totHP();
    protected abstract int atk();
    protected abstract int longatk();
    protected abstract int stepRate();
    protected abstract int jumpDist();
    protected abstract int maxProjectiles();
    protected abstract double projectileRate();
    protected abstract double meleeRate();
    
    @SuppressWarnings("unused")
    private void dumpDebug()
    {
        System.out.println("anim: " + anim);
        System.out.println("action: " + action);
        if(up) System.out.println("up");
        if(right) System.out.println("right");
        if(left) System.out.println("left");
        if(down) System.out.println("down");
        System.out.println("pos: " + pos);
        System.out.println();
    }
    private void newUCID()
    {
        boolean concurrentModification;
        do
        {
            try
            {
                ucid = area.ucidKeeper.newUCID(this);
                concurrentModification = false;
            }
            catch(java.util.ConcurrentModificationException cme)
            {
                concurrentModification = true;
            }
        }
        while(concurrentModification);
    }
    
    /**
     * Called for every frame of gameplay -- moves Character.
     */
    protected void frame()
    {
        // dumpDebug();
        while(charbmp == null || pos == null)
        {
            // stabilize before doing anything
        }
        if(!(up || right || down || left))
            frame = stepRate(); // always start with a step
        if(projectiles < maxProjectiles())
            projectiles += projectileRate();
        if(melees < maxProjectiles())
            melees += meleeRate();
        if(defending) 
        {
            erase();
            if(anim == 3)
                imgStandRight();
            else if(anim == 4)
                imgStandLeft();
            draw(pos);
            shield.refresh();
            return; // can't move while defending
        }
        erase();
        if(!(up || down || left || right))
        {
            if(anim == 3)
            {
                imgStandRight();
                if(!draw(pos))
                {
                    imgWalkRight();
                    draw(pos);
                }
            }
            else if(anim == 4)
            {
                imgStandLeft();
                if(!draw(pos))
                {
                    imgWalkLeft();
                    draw(pos);
                }
            }
            else
                draw(pos);
            return;
        }
        switch(action)
        {
            case 1: // walk
                walkAction();
                break;
            case 2: // strafe
                strafeAction();
                break;
            case 3: // turn
                turnAction();
                break;
            default: // shouldn't happen
                break;
        }
    }
    protected void upPressed(boolean pressed)
    {
        up = pressed;
    }
    protected void rightPressed(boolean pressed)
    {
        right = pressed;
    }
    protected void downPressed(boolean pressed)
    {
        down = pressed;
    }
    protected void leftPressed(boolean pressed)
    {
        left = pressed;
    }
    private void walkAction()
    {
        int tmp = anim;
        if(right && !left && (anim == 2 || anim == 4))
            imgStandRight();
        else if(left && !right && (anim == 1 || anim == 3))
            imgStandLeft();
        if(checkDraw(pos))
            strafeAction();
        else
        {
            if(tmp == 1)
                imgStandRight();
            else if(tmp == 2)
                imgStandLeft();
            else if(tmp == 3)
                imgWalkRight();
            else if(tmp == 4)
                imgWalkLeft();
            if(!checkDraw(pos))
            {
                System.out.println("Error: walkAction case 0-1");
                return;
            }
            strafeAction();
        }
    }
    private void strafeAction()
    {
        int xjump = 0;
        int yjump = 0;
        if(up)
            yjump -= jumpDist();
        if(right)
            xjump += jumpDist();
        if(down)
            yjump += jumpDist();
        if(left)
            xjump -= jumpDist();
        if(xjump == 0 && yjump == 0)
        {
            if(draw(pos))
                return;
        }
        if(frame < stepRate())
        {
            frame++;
            if(!draw(new Coord(pos.getX()+xjump,pos.getY()+yjump)))
            {
                if(draw(pos))
                    return;
            }
            else
                return;
        }
        else
            frame = 0;
        
        if(anim == 1) // stand_right
        {
            imgWalkRight();
            if(!draw(new Coord(pos.getX()+xjump,pos.getY()+yjump)))
            {
                imgStandRight();
                if(!draw(pos))
                    System.out.println("Error: walkAction case 1-1");
            }
        }
        else if(anim == 2) // stand_left
        {
            imgWalkLeft();
            if(!draw(new Coord(pos.getX()+xjump,pos.getY()+yjump)))
            {
                imgStandLeft();
                if(!draw(pos))
                    System.out.println("Error: walkAction case 1-2");
            }
        }
        else if(anim == 3) // walk_right
        {
            imgStandRight();
            if(!draw(new Coord(pos.getX()+xjump,pos.getY()+yjump)))
            {
                imgWalkRight();
                if(!draw(pos))
                    System.out.println("Error: walkAction case 1-3");
            }
        }
        else if(anim == 4) // walk_left
        {
            imgStandLeft();
            if(!draw(new Coord(pos.getX()+xjump,pos.getY()+yjump)))
            {
                imgWalkLeft();
                if(!draw(pos))
                    System.out.println("Error: walkAction case 1-4");
            }
        }
    }
    private void turnAction()
    {
        int tmp = anim;
        if(anim == 3)
            imgStandRight();
        else if(anim == 4)
            imgStandLeft();
        if(right)
            imgStandRight();
        else if(left)
            imgStandLeft();
        if(!draw(pos))
        {
            if(tmp == 1)
                imgStandRight();
            else if(tmp == 2)
                imgStandLeft();
            else if(tmp == 3)
                imgWalkRight();
            else if(tmp == 4)
                imgWalkLeft();
            if(!draw(pos))
                System.out.println("Error: turnAction");
        }
    }
    /**
     * Tells the Character to start or stop strafing.
     * @param strafe whether or not to strafe
     */
    protected void strafe(boolean strafe)
    {
        if(strafe)
            action = 2;
        else
            action = 1;
    }
    /**
     * Tells the Character to start or stop turning.
     * @param turn whether or not to turn
     */
    protected void turn(boolean turn)
    {
        if(turn)
            action = 3;
        else
            action = 1;
    }
    /**
     * Changes current image to standing right.
     */
    protected void imgStandRight()
    {
        charbmp = spriteSheet[0];
        anim = 1;
    }
    /**
     * Changes current image to standing left.
     */
    protected void imgStandLeft()
    {
        charbmp = spriteSheet[1];
        anim = 2;
    }
    /**
     * Changes current image to walking right.
     */
    protected void imgWalkRight()
    {
        charbmp = spriteSheet[2];
        anim = 3;
    }
    /**
     * Changes current image to walking left.
     */
    protected void imgWalkLeft()
    {
        charbmp = spriteSheet[3];
        anim = 4;
    }
    /**
     * Loads images into memory...very carefully.
     */
    protected void loadImages()
    {
        Toolkit toolkit = area.getToolkit();
        Graphics g;
        Image[] tmpImgs = new Image[numSprites];
        for(int i=0; i<numSprites; i++)
        {
            tmpImgs[i] = toolkit.getImage(getClass().getResource(spriteFiles[i]));
            toolkit.prepareImage(tmpImgs[i], -1, -1, null);
        }
        boolean loaded = false;
        while(!loaded)
        {
            loaded = true;
            for(Image img : tmpImgs)
            {
                if(toolkit.checkImage(img, -1, -1, area) != area.IMAGE_LOAD)
                    loaded = false;
            }
        }
        BufferedImage imgbuf;
        for(int i=0; i<numSprites; i++)
        {
            imgbuf = new BufferedImage(tmpImgs[i].getWidth(null), 
                tmpImgs[i].getHeight(null), BufferedImage.TYPE_INT_ARGB_PRE);
            spriteSheet[i] = new Sprite();
            g = imgbuf.getGraphics();
            g.drawImage(tmpImgs[i], 0, 0, null);
            for(int j=0; j<MAX_SIZE_Y; j++)
            {
                for(int k=0; k<MAX_SIZE_X; k++)
                {
                    if(imgbuf.getRGB(k,j) != area.TRANSPARENT)
                    {
                        if(imgbuf.getRGB(k,j) == area.MARKERCOL)
                            spriteSheet[i].addPixel(k, j, area.MARKER);
                        else
                            spriteSheet[i].addPixel(k, j, imgbuf.getRGB(k,j));
                    }
                }
            }
        }
    }
    /**
     * Determines whether the Character contains any of a set of coordinates.
     * @param coords set of coordinates to check
     * @return true if the Character contains any of the coordinates
     */
    protected boolean contains(Coord[] coords)
    {
        for(Coord c : coords)
        {
            if(charbmp.containsPt(c.getX(),c.getY()))
                return true;
        }
        return false;
    }
    /**
     * Modifies this character's HP.
     * @param dmg amount by which to decrease the HP
     * @return whether or not the modification killed the Character
     */
    protected boolean modHP(int dmg)
    {
        if(defending)
        {
            System.out.println("Character " + ucid + " defended!");
            return false;
        }
        hp -= dmg;
        System.out.println("Character " + ucid + "'s HP: " + hp + "/" + totHP);
        if(hp <= 0)
        {
            hp = 0;
            die();
            return true;
        }
        if(hp > totHP)
            hp = totHP;
        return false;
    }
    /**
     * Gives this Character experience points.
     * @param exp amount of experience points to give
     */
    protected void modEXP(int exp)
    {
        this.exp += exp;
        while(this.exp >= expToNextLv())
        {
            lv++;
            totHP = totHP();
            atk = atk();
            longatk = longatk();
            System.out.println("Character " + ucid + " has grown a level!");
            System.out.println("Level: " + lv + "\tTotal HP: " + totHP);
            System.out.println("Attack: " + atk + "\tLong Attack: " + longatk + "\n");
        }
    }
    /**
     * Restores pixels occupied by character's current state with original
     * pixels of Field.
     */
    protected void erase()
    {
        HashSet<Pixel> pixels = charbmp.getPixels();
        int x,y;
        for(Pixel p : pixels)
        {
            x = p.getX()+pos.getX();
            y = p.getY()+pos.getY();
            area.bitmap.setRGB(x,y,area.origBitmap[x][y]);
            area.drawable[x][y] = area.origDrawable[x][y];
            area.ucidMap[x][y] = 0;
        }
    }
    private boolean checkDraw(Coord pos)
    {
        HashSet<Pixel> pixels = charbmp.getPixels();
        for(Pixel p : pixels)
        {
            if(!area.drawable[p.getX()+pos.getX()][p.getY()+pos.getY()])
                return false;
        }
        return true;
    }
    /**
     * Draws the Character at a given position on the screen.
     * @param pos coordinates to draw the Character at
     * @return true if the character was successfully drawn, false if it could
     * not be
     */
    protected boolean draw(Coord pos)
    {
        // if out of boundaries, try moving to next Field
        FrameScrollingField fsarea = (FrameScrollingField)area;
        if(pos.getX() < 0)  // west
        {
            if(!(area instanceof FrameScrollingField) || fsarea.getWestField() == null)
                return false;
            area = fsarea.getWestField();
            if(!draw(new Coord(fsarea.X_SIZE-charbmp.getWidth()-1, pos.getY())))
            {
                area = fsarea;
                return false;
            }
            fsarea.removeCharacter(ucid);
            newUCID();
            return true;
        }
        else if(pos.getX() + charbmp.getWidth() >= fsarea.X_SIZE) // east
        {
            if(!(area instanceof FrameScrollingField) || fsarea.getEastField() == null)
                return false;
            area = fsarea.getEastField();
            if(!draw(new Coord(0, pos.getY())))
            {
                area = fsarea;
                return false;
            }
            fsarea.removeCharacter(ucid);
            newUCID();
            return true;
        }
        else if(pos.getY() < 0) // north
        {
            if(!(area instanceof FrameScrollingField) || fsarea.getNorthField() == null)
                return false;
            area = fsarea.getNorthField();
            if(!draw(new Coord(pos.getX(), fsarea.Y_SIZE-charbmp.getHeight()-1)))
            {
                area = fsarea;
                return false;
            }
            fsarea.removeCharacter(ucid);
            newUCID();
            return true;
        }
        else if(pos.getY() + charbmp.getHeight() >= fsarea.Y_SIZE) // south
        {
            if(!(area instanceof FrameScrollingField) || fsarea.getSouthField() == null)
                return false;
            area = fsarea.getSouthField();
            if(!draw(new Coord(pos.getX(), 0)))
            {
                area = fsarea;
                return false;
            }
            fsarea.removeCharacter(ucid);
            newUCID();
            return true;
        }
        // First loop determines if we're allowed to draw
        HashSet<Pixel> pixels = charbmp.getPixels();
        for(Pixel p : pixels)
        {
            if(!area.drawable[p.getX()+pos.getX()][p.getY()+pos.getY()])
                return false;
        }
        // Second loop draws
        this.pos = pos;
        int x,y;
        for(Pixel p : pixels)
        {
            if(p.getRGB() != area.MARKER)
            {
                x = p.getX()+pos.getX();
                y = p.getY()+pos.getY();
                area.bitmap.setRGB(x, y, p.getRGB());
                area.drawable[x][y] = false;
                area.ucidMap[x][y] = ucid;
            }
        }
        return true;
    }
}