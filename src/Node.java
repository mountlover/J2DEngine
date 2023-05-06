/**
 * <b>Node</b> - Node for a DoublyLinkedList
 * @author Yama H
 * @version 1.0
 * @param <E> generics
 */
public class Node<E> 
{
    protected E data; // value stored in this element 
    protected Node<E> nextElement, previousElement; // ref to next 
    /**
     * Constructor for Objects of type Node.
     * @param v data
     * @param next next node in list
     * @param previous previous node in list
     */
    public Node(E v, Node<E> next, Node<E> previous) 
    { 
        data = v; 
        nextElement = next; 
        previousElement = previous;
    }
    /**
     * Returns reference to next value in list.
     * @return reference to next value in list
     */
    public Node<E> next() 
    {
        return nextElement; 
    } 
    /**
     * Sets reference to new next value.
     * @param next new next value
     */
    public void setNext(Node<E> next) 
    {
        nextElement = next; 
    } 
    /**
     * Returns reference to previous value in list.
     * @return reference to previous value in list
     */
    public Node<E> previous() 
    {
        return previousElement; 
    } 
    /**
     * Sets reference to new previous value.
     * @param previous new previous value
     */
    public void setPrevious(Node<E> previous) 
    {
        previousElement = previous; 
    } 
    /**
     * Returns value associated with this element.
     * @return value associated with this element
     */
    public E value() 
    {
        return data; 
    } 
    /**
     * Sets value associated with this element.
     * @param value new value
     */
    public void setValue(E value) 
    {
        data = value; 
    } 
} 