package uppgift;

public class HashTableQuadraticProbing<T> {
  private static class HashEntry<T> {
    T element;
    boolean isActive;

    HashEntry(T element, boolean isActive) {
      this.element = element;
      this.isActive = isActive;
    }
  }

  private HashEntry<T>[] hashTable;
  private int capacity;
  private int size;

  @SuppressWarnings("unchecked")
  public HashTableQuadraticProbing(int capacity) {
    this.capacity = capacity;
    this.hashTable = (HashEntry<T>[]) new HashEntry[capacity];
    this.size = 0;
  }

  public void insert(T element) {
    int currentPos = findPos(element);
    if (isActive(currentPos)) {
      hashTable[currentPos].element = element;
      return;
    }

    hashTable[currentPos] = new HashEntry<>(element, true);
    size++;

    if (size > capacity / 2) {
      resize();
    }
  }

  public boolean search(T element) {
    int currentPos = findPos(element);
    return isActive(currentPos);
  }

  public void remove(T element) {
    int currentPos = findPos(element);
    if (isActive(currentPos)) {
      hashTable[currentPos].isActive = false;
      size--;
    }
  }

  @SuppressWarnings("unchecked")
  private void resize() {
    capacity = capacity * 2;

    HashEntry<T>[] oldHashTable = hashTable;
    hashTable = (HashEntry<T>[]) new HashEntry[capacity];
    size = 0;

    for (HashEntry<T> entry : oldHashTable) {
      if (entry != null && entry.isActive) {
        insert(entry.element);
      }
    }
  }

  private int findPos(T element) {
    int offset = 1;
    int currentPos = element.hashCode() % capacity;

    while (hashTable[currentPos] != null && !hashTable[currentPos].element.equals(element)) {
      currentPos += offset;
      offset += 2;
      if (currentPos >= capacity) {
        currentPos -= capacity;
      }
    }

    return currentPos;
  }

  private boolean isActive(int currentPos) {
    return hashTable[currentPos] != null && hashTable[currentPos].isActive;
  }

  public int size() {
    return size;
  }

  public int capacity() {
    return capacity;
  }

 
  public static void main(String[] args) {
    HashTableQuadraticProbing<Integer> hashTable = new HashTableQuadraticProbing<>(10);

    // Test inserting values
    hashTable.insert(1);
    hashTable.insert(2);
    hashTable.insert(3);
    System.out.println("After inserts: Expected 3 items, got " + hashTable.size());

    // Test if values are present
    System.out.println("search 2: Expected true, got " + hashTable.search(2));
    System.out.println("search 4: Expected false, got " + hashTable.search(4));

    // Test removing values
    hashTable.remove(2);
    System.out.println("After removing 2: Expected false, got " + hashTable.search(2));
    System.out.println("Size after removing 2: Expected 2, got " + hashTable.size());

    // Test inserting more values to check for collisions and resizing
    for (int i = 4; i <= 20; i++) {
        hashTable.insert(i);
    }
    System.out.println("After inserting values 4 to 20: Size = " + hashTable.size() + ", Capacity = " + hashTable.capacity());

    // Test if specific values are present after resizing
    System.out.println("search 10: Expected true, got " + hashTable.search(10));
    System.out.println("search 15: Expected true, got " + hashTable.search(15));
    System.out.println("search 21: Expected false, got " + hashTable.search(21));

    // Test removing a value and checking size
    hashTable.remove(10);
    System.out.println("After removing 10: Expected false, got " + hashTable.search(10));
    System.out.println("Size after removing 10: Expected " + (hashTable.size() - 1) + ", got " + hashTable.size());

    // Test inserting a value that was previously removed
    hashTable.insert(10);
    System.out.println("After re-inserting 10: Expected true, got " + hashTable.search(10));
    System.out.println("Size after re-inserting 10: Expected " + (hashTable.size() + 1) + ", got " + hashTable.size());
}

}
