/**
 * <b>FighterAI</b> - Computer controlled fighter.
 * @author Yama H
 * @version 0.2a
 */
public class FighterAI extends Fighter
{
    /**
     * Constructor for objects of type FighterAI
     * @param f Field upon which to draw the Fighter
     * @param initPos initial position of this Fighter
     */
    public FighterAI(Field f, Coord initPos)
    {
        super(f, initPos);
    }
    /**
     * Called when this fighter is killed.
     */
    protected void die()
    {
        System.out.println("Fighter " + ucid + " has died.\n");
        erase();
        boolean respawned = false;
        while(!respawned)
        {
            if(draw(new Coord((int)(Math.random()*(area.X_SIZE-1)),
                    (int)(Math.random()*(area.Y_SIZE-1)))))
                respawned = true;
        }
        hp = totHP;
    }
}