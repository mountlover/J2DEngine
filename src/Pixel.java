import java.awt.Color;
/**
 * <b>Pixel</b> - stores a coordinate and a color value.
 * @author Yama H
 * @version 1.0
 */
public class Pixel extends Coord
{
    private int RGB;
    /**
     * Constructor for objects of type Pixel.
     * @param x X coordinate
     * @param y Y coordinate
     * @param rgb RGB value
     */
    public Pixel(int x, int y, int rgb)
    {
        super(x,y);
        RGB = rgb;
    }
    /**
     * Constructor for objects of type Pixel.
     * @param x X coordinate
     * @param y Y coordinate
     * @param color color of pixel
     */
    public Pixel(int x, int y, Color color)
    {
        super(x,y);
        RGB = color.getRGB();
    }
    /**
     * Returns color of this pixel.
     * @return color of pixel, in RGB format
     */
    public int getRGB()
    {
        return RGB;
    }
    /**
     * Sets this pixel to a specified RGB value
     * @param rgb RGB value expressed as an int
     */
    public void setRGB(int rgb)
    {
        RGB = rgb;
    }
    /**
     * Sets this pixel to a Color.
     * @param color color to set the pixel to
     */
    public void setRGB(Color color)
    {
        RGB = color.getRGB();
    }
    public String toString()
    {
        return super.toString() + ": " + RGB + " ";
    }
}