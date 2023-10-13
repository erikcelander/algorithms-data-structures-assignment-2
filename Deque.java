import java.util.Iterator;

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

  public Iterator<Item> iterator() {
    return new DequeIterator<Item>(first);
  }
}
