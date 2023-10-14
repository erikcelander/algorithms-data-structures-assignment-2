package uppgift;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;

public class RandomizedQueue<Item> implements Iterable<Item> {
  private Item[] items;
  private int size;
  private final Random random = new Random();

  @SuppressWarnings("unchecked")
  public RandomizedQueue() {
    items = (Item[]) new Object[1];
    size = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public void enqueue(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("Item cannot be null");
    }

    // resize array if necessary
    if (size == items.length) {
      resize(2 * items.length);
    }

    // add item to end of array
    items[size++] = item;
  }

  public Item dequeue() {
    if (isEmpty()) {
      throw new NoSuchElementException("Queue is empty");
    }
    // get random index and use it to get random item
    int randomIndex = random.nextInt(size);
    Item item = items[randomIndex];

    size--;

    // replace random item with last item in array
    items[randomIndex] = items[size];
    items[size] = null;

    // resize array if necessary
    if (size > 0 && size == items.length / 4) {
      resize(items.length / 2);
    }
    return item;
  }

  public Iterator<Item> iterator() {
    return new RandomizedQueueIterator();
  }

  @SuppressWarnings("unchecked")
  private void resize(int capacity) {
    Item[] copy = (Item[]) new Object[capacity];
    for (int i = 0; i < size; i++) {
      copy[i] = items[i];
    }
    items = copy;
  }

  private class RandomizedQueueIterator implements Iterator<Item> {
    private int current;
    private int[] indices;

    public RandomizedQueueIterator() {
      current = 0;
      indices = new int[size];
      for (int i = 0; i < size; i++) {
        indices[i] = i;
      }
      Collections.shuffle(Arrays.asList(indices), random);
    }

    public boolean hasNext() {
      return current < size;
    }

    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException("No more items to return");
      }
      return items[indices[current++]];
    }

    public void remove() {
      throw new UnsupportedOperationException("not supported");
    }
  }

  public static void main(String[] args) {
    RandomizedQueue<String> queue = new RandomizedQueue<String>();

    // Test: isEmpty on an empty queue
    assert queue.isEmpty() : "Queue should be empty initially";

    // Test: size on an empty queue
    assert queue.size() == 0 : "Size should be 0 initially";

    // Test: enqueue
    queue.enqueue("A");
    assert queue.size() == 1 : "Size should be 1 after adding one item";
    assert !queue.isEmpty() : "Queue should not be empty after adding an item";

    queue.enqueue("B");
    assert queue.size() == 2 : "Size should be 2 after adding two items";

    // Test: dequeue
    String firstItem = queue.dequeue();
    assert (firstItem.equals("A") || firstItem.equals("B")) : "Dequeued item should be 'A' or 'B'";
    assert queue.size() == 1 : "Size should be 1 after dequeuing one item";

    String secondItem = queue.dequeue();
    assert (secondItem.equals("A") || secondItem.equals("B")) &&
        !secondItem.equals(firstItem)
        : "Dequeued item should be the one not dequeued before";
    assert queue.size() == 0 : "Size should be 0 after dequeuing two items";

    // Test: Exception handling
    try {
      queue.dequeue();
      assert false : "Expected exception when dequeuing from an empty queue";
    } catch (NoSuchElementException e) {
      System.out.println(e.getMessage());
    }

    // Test: Iterator
    queue.enqueue("C");
    queue.enqueue("D");
    Iterator<String> iterator = queue.iterator();
    assert iterator.hasNext() : "Iterator should have next item";
    String firstIterItem = iterator.next();
    assert (firstIterItem.equals("C") || firstIterItem.equals("D")) : "First item from iterator should be 'C' or 'D'";
    String secondIterItem = iterator.next();
    assert (secondIterItem.equals("C") || secondIterItem.equals("D")) &&
        !secondIterItem.equals(firstIterItem)
        : "Second item from iterator should be the one not returned before";
    assert !iterator.hasNext() : "Iterator should not have more items";

    System.out.println("All tests passed!");
  }
}
