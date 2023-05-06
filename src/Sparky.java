public class Sparky extends Character
{
    private static final String[] spriteFilenames = 
    {
        "sparky/sparky_stand_right.png",  // spriteFilenames[0]
        "sparky/sparky_stand_left.png",   // spriteFilenames[1]
        "sparky/sparky_walk_right.png",   // spriteFilenames[2]
        "sparky/sparky_walk_left.png",    // spriteFilenames[3]
    };
    public Sparky(Field f, Coord initPos)
    {
        super(f, initPos, spriteFilenames);
    }
    public void frame()
    {
    	int rand = (int)(8*Math.random());
        switch(rand)
        {
            case 0:
                upPressed(true);
                break;
            case 1:
                upPressed(false);
                break;
            case 2:
                rightPressed(true);
                break;
            case 3:
                rightPressed(false);
                break;
            case 4:
                downPressed(true);
                break;
            case 5:
                downPressed(false);
                break;
            case 6:
                leftPressed(true);
                break;
            case 7:
                leftPressed(false);
                break;
            default:
                break;
        }
        super.frame();
    }
    protected int atk()
    {
        return 50;
    }
    protected void attack()
    {
        // nothing
    }
    protected void defend( boolean defense )
    {
        // nothing
    }
    protected void die()
    {
        System.out.println("Sparky " + ucid + " has died.\n");
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
    protected int expToNextLv()
    {
        return (int)Math.pow(2, lv/4) + (int)Math.pow(lv, 3) + 90*lv;
    }
    protected int jumpDist()
    {
        return Math.round(50/15);
    }
    protected int longatk()
    {
        return 0;
    }
    protected int maxProjectiles()
    {
        return 1;
    }
    protected double meleeRate()
    {
        return 1;
    }
    protected double projectileRate()
    {
        return 1;
    }
    protected void shoot()
    {
        // nothing
    }
    protected int stepRate()
    {
       return Math.round(3*50/50);
    }
    protected int totHP()
    {
        return 20*lv;
    }
}