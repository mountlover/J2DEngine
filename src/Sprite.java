import java.awt.Color;
import java.util.HashSet;
/**
 * <b>Sprite</b> - Stores a number of pixels.
 * @author Yama H
 * @version 1.0
 */
public class Sprite
{
    private HashSet<Pixel> pixels;
    private HashSet<Coord> coords;
    private int xlo, xhi, ylo, yhi;
    /**
     * Constructor for objects of type Sprite
     */
    public Sprite()
    {
        pixels = new HashSet<Pixel>();
        coords = new HashSet<Coord>();
        xlo = -1;
        ylo = -1;
        xhi = -1;
        yhi = -1;
    }
    /**
     * Adds a pixel to the Sprite.
     * @param x X coordinate
     * @param y Y coordinate
     * @param rgb RGB value of pixel represented as an int
     */
    public void addPixel(int x, int y, int rgb)
    {
        pixels.add(new Pixel(x,y,rgb));
        coords.add(new Coord(x,y));
        if(xhi == -1 && yhi == -1 && xlo == -1 && ylo == -1)
        {
            xlo = x;
            xhi = x;
            ylo = y;
            yhi = y;
        }
        else
        {
            if(x < xlo) xlo = x;
            if(y < ylo) ylo = y;
            if(x > xhi) xhi = x;
            if(y > yhi) yhi = y;
        }
    }
    /**
     * Adds a pixel to the Sprite.
     * @param x X coordinate
     * @param y Y coordinate
     * @param c Color of pixel
     */
    public void addPixel(int x, int y, Color c)
    {
        pixels.add(new Pixel(x,y,c));
        coords.add(new Coord(x,y));
        if(xhi == -1 && yhi == -1 && xlo == -1 && ylo == -1)
        {
            xlo = x;
            xhi = x;
            ylo = y;
            yhi = y;
        }
        else
        {
            if(x < xlo) xlo = x;
            if(y < ylo) ylo = y;
            if(x > xhi) xhi = x;
            if(y > yhi) yhi = y;
        }
    }
    /**
     * Gets a set of all of the pixels in this Sprite.
     * @return all pixels in the Sprite, in a HashSet
     */
    public HashSet<Pixel> getPixels()
    {
        return pixels;
    }
    /**
     * Checks to see if this Sprite contains (is not transparent at) the specified point.
     * @param x X coordinate
     * @param y Y coordinate
     * @return whether or not this Sprite contains the specified point
     */
    public boolean containsPt(int x, int y)
    {
        if(coords.contains(new Coord(x,y)))
            return true;
        return false;
    }
    /**
     * Returns the height of the sprite, in pixels.
     * @return the height of the sprite, in pixels
     */
    public int getHeight()
    {
        return yhi-ylo+1;
    }
    /**
     * Returns the width of the sprite, in pixels.
     * @return the width of the sprite, in pixels
     */
    public int getWidth()
    {
        return xhi-xlo+1;
    }
}