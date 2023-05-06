/**
 * <b>Fighter</b> - A well balanced Character
 * @author Yama H
 * @version 0.9b
 */
public class Fighter extends Character
{
    private static final int THROW_RANGE = 200;
    private static final String[] spriteFilenames = 
    {
        "fighter/fighter_stand_right.png",  // spriteFilenames[0]
        "fighter/fighter_stand_left.png",   // spriteFilenames[1]
        "fighter/fighter_walk_right.png",   // spriteFilenames[2]
        "fighter/fighter_walk_left.png",    // spriteFilenames[3]
        "weapons/ten_shield_right.png",     // spriteFilenames[4]
        "weapons/ten_shield_left.png",      // spriteFilenames[5]
        "weapons/sword_right.png",          // spriteFilenames[6]
        "weapons/sword_left.png",           // spriteFilenames[7]
        "weapons/throwing_knife_right.png", // spriteFilenames[8]
        "weapons/throwing_knife_left.png",  // spriteFilenames[9]
    };
    /*
     * Initial locations of projectiles relative to Character sprite.
     */
    private static final Coord[] projLocs =
    {
        new Coord(23,6),    // projLocs[0] Shield Right
        new Coord(-4,6),    // projLocs[1] Shield Left
        new Coord(25,1),    // projLocs[2] Sword Stand Right
        new Coord(25,1),    // projLocs[3] Sword Walk Right
        new Coord(-19,1),   // projLocs[4] Sword Stand Left
        new Coord(-16,1),   // projLocs[5] Sword Walk Left
        new Coord(40,10),   // projLocs[6] Throwing Knife Right
        new Coord(-24,10)   // projLocs[7] Throwing Knife Left
    };
    /**
     * Constructor for objects of type Fighter.
     * @param f the Field upon which to make this Fighter
     * @param initPos initial coordinates of this Fighter
     */
    public Fighter(Field f, Coord initPos)
    {
        super(f, initPos, spriteFilenames);
    }
    /**
     * Constructor for objects of type Fighter.
     * @param f the Field upon which to make this Fighter
     * @param pos coordinates of this Fighter
     * @param fighterData previously saved data to load into the Fighter
     * in the form: <br>
     * fighterData[0] = totHP <br>
     * fighterData[1] = hp <br>
     * fighterData[2] = exp <br>
     * fighterData[3] = lv <br>
     * fighterData[4] = atk <br>
     * fighterData[5] = longatk
     */
    public Fighter(Field f, Coord pos, int[] fighterData)
    {
        super(f, pos, spriteFilenames);
        totHP = fighterData[0];
        hp = fighterData[1];
        exp = fighterData[2];
        lv = fighterData[3];
        atk = fighterData[4];
        longatk = fighterData[5];
    }
    /**
     * Tells the Character to attack.
     */
    protected void attack()
    {
        if(defending) return; // can't attack while defending
        if(melees < 1) return;
        switch(anim)
        {
            case 1: // standing right
                new Projectile().start(this, new int[]{6}, 0, 1, true, true, projLocs[2]);
                break;
            case 2: // standing left
                new Projectile().start(this, new int[]{7}, 0, 1, true, true, projLocs[4]);
                break;
            case 3: // walking right
                new Projectile().start(this, new int[]{6}, 0, 1, true, true, projLocs[3]);
                break;
            case 4: // walking left
                new Projectile().start(this, new int[]{7}, 0, 1, true, true, projLocs[5]);
                break;
            default:
                break;
        }
        melees--;
    }
    /**
     * Tells the Character to defend.
     * @param defense whether or not to defend
     */
    protected void defend(boolean defense)
    {
        if(defending == defense)
            return;
        defending = defense;
        if(defense)
        {
            if(anim == 1 || anim == 3) // facing right
            {
                shield = new Projectile();
                shield.start(this, new int[]{4}, 0, -1, true, false, projLocs[0]);
            }
            else if(anim == 2 || anim == 4) // facing left
            {
                shield = new Projectile();
                shield.start(this, new int[]{4}, 0, -1, true, false, projLocs[1]);
            }
        }
        else
            shield.cancel();
    }
    /**
     * Kills this Character.
     */
    protected void die()
    {
        System.out.println("Fighter " + ucid + " has died.\n");
        erase();
        if(!draw(initPos))
            System.out.println("Invalid Initial Position");
        hp = totHP;
    }
    /**
     * Calculates the amount of exp necessary to get to the next level.
     */
    protected int expToNextLv()
    {
        return (int)Math.pow(2, lv/4) + (int)Math.pow(lv, 3) + 90*lv;
    }
    /**
     * Tells this Character to shoot it's projectile.
     */
    protected void shoot()
    {
        if(defending) return; // can't attack while defending
        if(projectiles < maxProjectiles()) return;
        if(anim == 1 || anim == 3) // facing right
            new Projectile().start(this, new int[]{8}, THROW_RANGE, 
                spriteSheet[8].getWidth()/2, false, true, projLocs[6]);
        else if(anim == 2 || anim == 4) // facing left
            new Projectile().start(this, new int[]{9}, -THROW_RANGE, 
                spriteSheet[9].getWidth()/2, false, true, projLocs[7]);
        projectiles--;
    }
    /**
     * Calculates how the Fighter's atk grows as it levels up.
     * @return this Character's attack value
     */
    protected int atk()
    {
        return 10*lv;
    }
    /**
     * Calculates how the Fighter's longatk grows as it levels up.
     * @return this Character's long range attack value
     */
    protected int longatk()
    {
        return 5*lv;
    }
    /**
     * Calculates how the Fighter's HP grows as it levels up.
     * @return this Character's total HP
     */
    protected int totHP()
    {
        return 100*lv;
    }
    /**
     * Calculates the rate at which the Character's projectile gauge (projectiles) 
     * replenishes.
     * @return rate at which the projectile gauge replenishes, in projectiles per frame
     */
    protected double projectileRate()
    {
        return (0.1 + .9*lv/100)*50/150;
    }
    /**
     * Calculates the rate at which the Character's melee attack gauge (melees) replenishes.
     * @return rate at which the melee attack gauge replenishes, in melee attacks per frame
     */
    protected double meleeRate()
    {
        return 0.5;
    }
    /**
     * Calculates the step rate.
     * @return the number of frames to wait before changing character sprite
     */
    protected int stepRate()
    {
        return Math.round(3*50/50);
    }
    /**
     * Calculates the jump distance.
     * @return the amount of pixels to move at a time
     */
    protected int jumpDist()
    {
        return Math.round(50/15);
    }
    /**
     * Calculates max simultaneous projectiles.
     * @return the maximum amount of simultaneous projectiles
     */
    protected int maxProjectiles()
    {
        return 1;
    }
}