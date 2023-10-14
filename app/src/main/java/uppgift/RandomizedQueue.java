package uppgift;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Collections;

public class RandomizedQueue<Item> implements Iterable<Item> {
  private ArrayList<Item> items;

  public RandomizedQueue() {
    items = new ArrayList<Item>();
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }

  public int size() {
    return items.size();
  }

  // add item to end of arraylist
  public void enqueue(Item item) {
    if (item == null) {
      throw new IllegalArgumentException("Item cannot be null");
    }
    items.add(item);
  }

  // remove random item from arraylist
  public Item dequeue() {
    if (isEmpty()) {
      throw new NoSuchElementException("Queue is empty");
    }
    // get random index and remove item at that index
    int randomIndex = new Random().nextInt(items.size());
    Item item = items.get(randomIndex);
    items.remove(randomIndex);
    return item;
  }

  public Iterator<Item> iterator() {
    return new RandomizedQueueIterator(items);
  }

  private class RandomizedQueueIterator implements Iterator<Item> {
    private ArrayList<Item> shuffledItems;
    private int index;

    // copy items to new arraylist and shuffle it
    public RandomizedQueueIterator(ArrayList<Item> items) {
      shuffledItems = new ArrayList<Item>(items);
      Collections.shuffle(shuffledItems);
      index = 0;
    }

    // check if there are more items to iterate over
    public boolean hasNext() {
      return index < shuffledItems.size();
    }

    // return next item in shuffled arraylist
    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return shuffledItems.get(index++);
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
    assert (secondItem.equals("A") || secondItem.equals("B")) && !secondItem.equals(firstItem)
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
    assert (secondIterItem.equals("C") || secondIterItem.equals("D")) && !secondIterItem.equals(firstIterItem)
        : "Second item from iterator should be the one not returned before";
    assert !iterator.hasNext() : "Iterator should not have more items";

    System.out.println("All tests passed!");
  }
}
