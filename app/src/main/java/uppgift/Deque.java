package uppgift;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
  private Node<Item> first;
  private Node<Item> last;
  private int size;

  private static class Node<Item> {
    private Item item;
    private Node<Item> next;
    private Node<Item> prev;
  }

  public Deque() {
    first = null;
    last = null;
    size = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public void addFirst(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("Item cannot be null");
    }

    // save old first node
    Node<Item> oldFirst = first;

    // create new first node
    first = new Node<Item>();

    // set referencences for new first
    first.item = item;
    first.next = oldFirst;
    first.prev = null;
    if (isEmpty()) {
      // if deque is empty, set last to first
      last = first;
    } else {
      // if deque is not empty, set old first prev to new first
      oldFirst.prev = first;
    }
    size++;
  }

  public void addLast(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("Item cannot be null");
    }
    // save old last node
    Node<Item> oldLast = last;

    // create new last node
    last = new Node<Item>();

    // set references for new last
    last.item = item;
    last.next = null;
    last.prev = oldLast;

    if (isEmpty()) {
      // if deque is empty, set first to last
      first = last;
    } else {
      // if deque is not empty, set old last next to new last
      oldLast.next = last;
    }
    size++;
  }

  public Item removeFirst() {
    if (isEmpty()) {
      throw new NoSuchElementException("Deque is empty");
    }
    // save old first node
    Item item = first.item;
    // set first to old first next
    first = first.next;
    size--;

    if (isEmpty()) {
      // if deque is empty, set last to null
      last = null;
    } else {
      // if deque is not empty, set first prev to null
      first.prev = null;
    }
    return item;
  }

  public Item removeLast() {
    if (isEmpty()) {
      throw new NoSuchElementException("Deque is empty");
    }

    // save old last node
    Item item = last.item;
    // set last to old last prev
    last = last.prev;
    size--;
    if (isEmpty()) {
      // if deque is empty, set first to null
      first = null;
    } else {
      // if deque is not empty, set last next to null
      last.next = null;
    }
    return item;
  }

  public Iterator<Item> iterator() {
    return new DequeIterator<Item>(first);
  }

  private class DequeIterator<T> implements Iterator<Item> {
    private Node<Item> current;

    // constructor
    public DequeIterator(Node<Item> first) {
      current = first;
    }

    public boolean hasNext() {
      // if current is null, there is no next
      return current != null;
    }

    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      // save current item
      Item item = current.item;
      // set current to current next
      current = current.next;
      // return saved item
      return item;
    }

    
  }

  public static void main(String[] args) {
    Deque<String> deque = new Deque<String>();

    // Test: isEmpty on an empty deque
    assert deque.isEmpty() : "Deque should be empty initially";

    // Test: size on an empty deque
    assert deque.size() == 0 : "Size should be 0 initially";

    // Test: addFirst
    deque.addFirst("A");
    assert deque.size() == 1 : "Size should be 1 after adding one item";
    assert !deque.isEmpty() : "Deque should not be empty after adding an item";

    // Test: addLast
    deque.addLast("B");
    assert deque.size() == 2 : "Size should be 2 after adding two items";

    // Test: removeFirst
    String firstItem = deque.removeFirst();
    assert firstItem.equals("A") : "Removed item should be 'A'";
    assert deque.size() == 1 : "Size should be 1 after removing one item";

    // Test: removeLast
    String lastItem = deque.removeLast();
    assert lastItem.equals("B") : "Removed item should be 'B'";
    assert deque.size() == 0 : "Size should be 0 after removing two items";

    // Test: Exception handling
    try {
        deque.removeFirst();
        assert false : "Expected exception when removing from an empty deque";
    } catch (NoSuchElementException e) {
        System.out.println(e.getMessage());
    }

    try {
        deque.removeLast();
        assert false : "Expected exception when removing from an empty deque";
    } catch (NoSuchElementException e) {
        System.out.println(e.getMessage());

    }

    deque.addFirst("C");
    deque.addLast("D");
    Iterator<String> iterator = deque.iterator();
    assert iterator.hasNext() : "Iterator should have next item";
    assert iterator.next().equals("C") : "First item from iterator should be 'C'";
    assert iterator.next().equals("D") : "Second item from iterator should be 'D'";
    assert !iterator.hasNext() : "Iterator should not have more items";

    System.out.println("All tests passed!");
}

}
