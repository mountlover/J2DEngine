/**
 * <b>SpriteSheet</b> - A Circular, Doubly Linked List of Sprites.
 * @author Yama H
 * @version 1.0
 */
public class SpriteSheet
{
    private CircularDoublyLinkedList<Sprite> spriteSheet;
    private Node<Sprite> ptr;
    /**
     * Makes an empty SpriteSheet.
     */
    public SpriteSheet()
    {
        spriteSheet = new CircularDoublyLinkedList<Sprite>();
        ptr = spriteSheet.head;
    }
    /**
     * Makes a SpriteSheet from an existing CircularDoublyLinkedList of Sprites.
     * @param sheet an existing CircularDoublyLinkedList of Sprites
     */
    public SpriteSheet(CircularDoublyLinkedList<Sprite> sheet)
    {
        spriteSheet = sheet;
        ptr = spriteSheet.head;
    }
    protected Sprite getCurrentSprite()
    {
        return ptr.value();
    }
    protected Sprite next()
    {
        ptr = ptr.next();
        return ptr.value();
    }
    protected Sprite prev()
    {
        ptr = ptr.previous();
        return ptr.value();
    }
    protected Sprite reset()
    {
        ptr = spriteSheet.head;
        return ptr.value();
    }
    protected boolean atStart()
    {
        return ptr.equals(spriteSheet.head);
    }
    protected int getSize()
    {
        return spriteSheet.size();
    }
    protected void addLast(Sprite s)
    {
        spriteSheet.addLast(s);
        if(spriteSheet.size() == 1)
            ptr = spriteSheet.head;
    }
    protected void addFirst(Sprite s)
    {
        spriteSheet.addFirst(s);
        if(spriteSheet.size() == 1)
            ptr = spriteSheet.head;
    }
}