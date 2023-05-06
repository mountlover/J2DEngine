import java.util.HashSet;
import java.util.LinkedList;
/**
 * <b>UCIDKeeper</b> - Holds onto and assigns <b>U</b>nique <b>C</b>haracter <b>ID</b>'s.
 * @author Yama H
 * @version 1.0
 */
public class UCIDKeeper
{
    Character[] chars;
    LinkedList<Integer> freeUCIDs;
    HashSet<Integer> takenUCIDs;
    int index;
    /**
     * Constructor for objects of type UCIDKeeper.
     * @param maxSize max number of characters
     */
    public UCIDKeeper(int maxSize)
    {
        chars = new Character[maxSize+1];
        index = 1; // we'll start at one to avoid discrepancies.
        freeUCIDs = new LinkedList<Integer>();
        takenUCIDs = new HashSet<Integer>();
        for(int i=0; i<maxSize; i++)
        {
            freeUCIDs.add(new Integer(i+1));
        }
    }
    /**
     * Assigns a new UCID to the specified Character.
     * @param c the Character to be assigned the new UCID
     * @return the new UCID
     */
    public int newUCID(Character c)
    {
        chars[freeUCIDs.peek()] = c;
        takenUCIDs.add(freeUCIDs.peek());
        return freeUCIDs.pop();
    }
    /**
     * Retrieves a Character specified by a UCID.
     * @param ucid unique character ID of the Character
     * @return the Character
     */
    public Character getChar(int ucid)
    {
        return chars[ucid];
    }
    /**
     * Determines whether or not the UCIDKeeper is full.
     * @return true if the UCIDKeeper is full, false otherwise
     */
    public boolean full()
    {
        if(freeUCIDs.isEmpty())
            return true;
        return false;
    }
    /**
     * Returns the number of Characters currently in this UCIDKeeper.
     * @return the number of Characters currently in this UCIDKeeper
     */
    public int chars()
    {
        return chars.length-1 - freeUCIDs.size();
    }
    /**
     * Rmoves a Character from the UCIDKeeper.
     * @param ucid UCID of the Character
     */
    public void removeChar(int ucid)
    {
        chars[ucid] = null;
        freeUCIDs.offer(new Integer(ucid));
        takenUCIDs.remove(new Integer(ucid));
    }
    /**
     * Returns a HashSet of all currently occupied UCIDs.
     * @return a HashSet of all currently occupied UCIDs
     */
    public HashSet<Integer> getUCIDs()
    {
        return takenUCIDs;
    }
}