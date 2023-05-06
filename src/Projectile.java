import java.util.HashSet;
/**
 * <b>Projectile</b> - Commonly used as an attack for a Character.
 * @author Yama H
 * @version 1.0
 */
public class Projectile extends Thread
{
    private int x;
    private int y;
    private Character source;
    private int[] spriteIndex;
    private int current;
    private int range;
    private int speed;
    private boolean objPiercing;
    private boolean attack;
    private boolean stop;
    /**
     * Start method for Projectiles.
     * @param source Character producing the projectile
     * @param spriteIndexes index values of spriteSheet that point to a sequence
     * of Sprites that should be used for this Projectile
     * @param range distance for projectile to go (in pixels). Should be >0 to 
     * go to the right, <0 to go to the left, and =0 to be stationary
     * @param speed speed of projectile, in pixels per frame. In the event that
     * range == 0, this represents the number of frames for the stationary 
     * projectile to remain on the Field (negative to remain indefinitely)
     * @param objPiercing true iff the projectile is to travel through objects
     * such as scenery
     * @param attack whether or not this projectile is an attack
     * @param initPos initial position of this projectile
     */
    public void start(Character source, int[] spriteIndexes, int range, 
        int speed, boolean objPiercing, boolean attack, Coord initPos)
    {
        this.speed = speed;
        x = initPos.getX()+source.pos.getX();
        y = initPos.getY()+source.pos.getY();
        this.range = range;
        current = 0;
        spriteIndex = spriteIndexes;
        this.source = source;
        this.objPiercing = objPiercing;
        this.attack = attack;
        stop = false;
        start();
    }
    public void run()
    {
        if(range > 0) // it's moving to the right
        {
            for(int i=0; i<range; i+=speed)
            {
                if(!drawProjectile()) return;
                if(attack)
                {
                    if(source.area.reportAttack(source, 
                            source.spriteSheet[spriteIndex[current]].getPixels(), new Coord(x,y), true))
                    {
                        eraseProjectile();
                        break;
                    }
                }
                trysleep();
                eraseProjectile();
                x += speed;
            }
        }
        else if(range < 0) // it's moving to the left
        {
            for(int i=0; i>range; i-=speed)
            {
                if(!drawProjectile()) return;
                if(attack)
                {
                    if(source.area.reportAttack(source, 
                            source.spriteSheet[spriteIndex[current]].getPixels(), 
                            new Coord(x,y), true))
                    {
                        eraseProjectile();
                        break;
                    }
                }
                trysleep();
                eraseProjectile();
                x -= speed;

            }
        }
        else // range == 0...it's not moving
        {
            if(!drawProjectile()) return;
            if(speed < 0)
            {
                while(true)
                {
                    if(attack)
                        source.area.reportAttack(source, 
                            source.spriteSheet[spriteIndex[current]].getPixels(), 
                            new Coord(x,y), false);
                    trysleep();
                    eraseProjectile();
                    if(stop) return;
                    drawProjectile();
                }
            }
            for(int i=0; i<speed; i++)
            {
                if(attack)
                    source.area.reportAttack(source, 
                        source.spriteSheet[spriteIndex[current]].getPixels(), 
                        new Coord(x,y), false);
                trysleep();
                eraseProjectile();
                drawProjectile();
            }
            eraseProjectile();
        }
    }
    private boolean drawProjectile()
    {
        // Can't draw out of boundaries
        if(x < 0 || x + source.spriteSheet[spriteIndex[current]].getWidth() >= 
            source.area.X_SIZE || y < 0 || y + 
            source.spriteSheet[spriteIndex[current]].getHeight() >= source.area.Y_SIZE)
            return false;
        HashSet<Pixel> pixels = source.spriteSheet[spriteIndex[current]].getPixels();
        // First loop determines if we're allowed to draw
        if(!objPiercing)
        {    
            for(Pixel p : pixels)
            {
                if(!source.area.drawable[p.getX()+x][p.getY()+y])
                {
                    // it doesn't matter if we hit a Character--hell, we *want* that
                    if(source.area.ucidMap[p.getX()+x][p.getY()+y] == 0)
                        return false;
                }
            }
        }
        // Second loop draws
        for(Pixel p : pixels)
            source.area.bitmap.setRGB(p.getX()+x, p.getY()+y, p.getRGB());
        return true;
    }
    private void eraseProjectile()
    {
        HashSet<Pixel> pixels = source.spriteSheet[spriteIndex[current]].getPixels();
        int x,y;
        for(Pixel p : pixels)
        {
            x = p.getX()+this.x;
            y = p.getY()+this.y;
            source.area.bitmap.setRGB(x,y,source.area.origBitmap[x][y]);
            source.area.drawable[x][y] = source.area.origDrawable[x][y];
        }
        changeSprite();
    }
    private void changeSprite()
    {
        if(current < spriteIndex.length-1)
            current++;
        else
            current = 0;
    }
    /**
     * Stops this Projectile if it is permanent.
     */
    protected void cancel()
    {
        stop = true;
    }
    private void trysleep()
    {
    	try
    	{
    		sleep(50);
    	}
    	catch(Exception e){}
    }
    /**
     * Redraws this Projectile.
     */
    protected void refresh()
    {
        eraseProjectile();
        drawProjectile();
    }
}