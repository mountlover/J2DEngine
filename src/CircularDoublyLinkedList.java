/**
 * <b>CircularDoublyLinkedList</b> - O(n)
 * @author Bailey & Documented by Yama H
 * @param <E> generics
 */
public class CircularDoublyLinkedList<E> 
{
    protected int count; // list size 
    protected Node<E> head; // ref. to first element
    /**
     * Constructor for objects of class SinglyLinkedList -- O(1)
     */
    public CircularDoublyLinkedList() 
    {
    	// post: generates an empty list 
        head = null; 
        count = 0; 
    }
    /**
     * O(1)
     * @param value value to be added as first entry
     */
    public void addFirst(E value) 
    {
    	// post: value is added to beginning of list
    	if(head == null)
    	{
    		head = new Node<E>(value, null, null);
    		head.setNext(head);
    		head.setPrevious(head);
    		count++;
    	}
    	else if(count == 1)
    	{
    		Node<E> tail = head;
    		head = new Node<E>(value, head, head);
    		tail.setNext(head);
    		tail.setPrevious(head);
    		count++;
    	}
    	else
    	{
    		head = new Node<E>(value, head, head.previous());
    		head.next().setPrevious(head); // make sure preceeding element is pointing to head
    		head.previous().setNext(head); // and make sure proceeding element is pointing to head
    		count++;
    	}
    }
    /**
     * O(1)
     * @return value of the first entry
     */
    public E getFirst() 
    {
    	// pre: list is not empty 
    	// post: returns first value in list 
    	if(head == null)
    	{
    		return null;
    	}
    	return head.value(); 
    }
    /**
     * O(1)
     * @return value of the last entry
     */
    public E getLast() 
    {
    	// pre: list is not empty
    	// post: returns the last value
    	if(head == null)
    	{
    		return null;
    	}
    	return head.previous().value();
    }
    /**
     * O(1)
     * @return value of the entry removed (the former first entry)
     */
    public E removeFirst() 
    {
    	// pre: list is not empty 
    	// post: removes and returns value from beginning of list
    	if(count == 0)
    	{
    		return null;
    	}
    	if(count == 1)
    	{
    		E temp = head.value();
    		head = null;
    		count--;
    		return temp;
    	}
    	E temp = head.value();
    	head = new Node<E>(head.next().value(), head.next().next(), head.previous());
    	count--;
    	return temp;
    } 
    /**
     * O(1)
     * @param value value to be added as last entry
     */
    public void addLast(E value) 
    {
    	if(head == null)
    	{
    		head = new Node<E>(value, null, null);
    		head.setNext(head);
    		head.setPrevious(head);
    		count++;
    	}
    	else if(count == 1)
    	{
    		Node<E> tail = new Node<E>(value, head, head);
    		head.setNext(tail);
    		head.setPrevious(tail);
    		count++;
    	}
    	else
    	{
    		head.setPrevious(new Node<E>(value, head, head.previous())); // make sure proceeding element is pointing to it
    																						   // and instantiate it
    		head.previous().previous().setNext(head.previous()); // and make sure preceeding element is pointing to it
    		count++;
    	}
    } 
    /**
     * O(1)
     * @return value of the entry removed (the former last entry)
     */
    public E removeLast() 
    { 
    	// pre: list is not empty 
    	// post: removes last value from list 
    	if(count == 0)
    	{
    		return null;
    	}
    	if(count == 1)
    	{
    		E temp = head.value();
    		head = null;
    		count--;
    		return temp;
    	}
    	E temp = head.previous().value();
    	head.setPrevious(new Node<E>(head.previous().previous().value(), head, 
    		head.previous().previous().previous())); // that was unpleasant
    	count--;
    	return temp;
    } 
    /**
     * O(m); m = n/2
     * @param value value to scan for
     * @return whether or not the value was found
     */
    public boolean contains(E value) 
    { 
    	// pre: list is not empty
    	// post: returns true iff value is found in list 
    	if(count == 0)
    	{
    		return false;
    	}
    	Node<E> finger1 = head;
    	Node<E> finger2 = head.previous();
    	for(int i = 0; i <= count/2; i++)
    	{
    		if(finger1.value().equals(value) || finger2.value() == value)
    		{
    			return true;
    		}
    		finger1 = finger1.next();
    		finger2 = finger2.previous();
    	}
    	return false;
    } 
    /**
     * O(m); m = n/2
     * @param value value to search for and remove
     * @return the value removed or null if the value did not exist
     */
    public E remove(E value) 
    { 
    	// pre: value is not null 
    	// post: removes first element with matching value, if any 
    	if(count == 0)
    	{
    		return null; // or throw an exception
    	}
    	Node<E> finger1 = head;
    	Node<E> finger2 = head.previous();
    	for(int i = 0; i <= count/2; i++)
    	{
    		if(finger1.value().equals(value))
    		{
    			return delete(finger1); // this
    		}
    		else if(finger2.value().equals(value))
    		{
    			return delete(finger2); // and this are simply to avoid repetitive code
    		}
    		finger1 = finger1.next();
    		finger2 = finger2.previous();
    	}
    	return null; // or throw an exception
    } 
    /**
     * O(1)
     * @param finger specific node to delete
     */
    private E delete(Node<E> finger)
    {
    	E temp = finger.value();
    	finger.previous().setNext(finger.next());
    	finger.next().setPrevious(finger.previous());
    	finger = null;
    	count--;
    	return temp;
    }
    /**
     * O(n)
     */
    public String toString() 
    {
    	Node<E> finger = head;
    	String ret = "";
    	for(int i = 0; i < count; i++)
    	{
    		ret += finger.value()+" ";
    		finger = finger.next();
    	}
    	return ret;
    }
    /**
     * O(1)
     * @return the size of the list expressed as an integer
     */
    public int size()
    {
    	return count;
    }
}
