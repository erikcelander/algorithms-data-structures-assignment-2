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

    public DequeIterator(Node<Item> first) {
      current = first;
    }

    public boolean hasNext() {
      return current != null;
    }

    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      Item item = current.item;
      current = current.next;
      return item;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }
  }
}
