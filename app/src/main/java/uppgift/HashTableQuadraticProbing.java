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
  private int initialConflicts;
  private int probingConflicts;

  @SuppressWarnings("unchecked")
  public HashTableQuadraticProbing(int capacity) {
    this.capacity = capacity;
    this.hashTable = (HashEntry<T>[]) new HashEntry[capacity];
    this.initialConflicts = 0;
    this.probingConflicts = 0;
    this.size = 0;
  }

  public void insert(T element) {
    int currentPos = Math.abs(findPos(element));

    if (hashTable[currentPos] == null || !hashTable[currentPos].isActive) {
      hashTable[currentPos] = new HashEntry<>(element, true);
      size++;
      if (size > capacity / 2) {
        resize();
      }
    } else {
      hashTable[currentPos].element = element;
    }

  }

  public T search(T element) {
    int currentPos = findPos(element);
    return getObjectAt(currentPos);
  }

  public boolean contains(T element) {
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

  private int findPos(T element) {
    int initialHash = adaptedHash(element);
    int currentPos = initialHash;
    int probeNumber = 0;

    if (hashTable[currentPos] != null
        && (!hashTable[currentPos].isActive || !hashTable[currentPos].element.equals(element))) {
      initialConflicts++;
    }

    while (hashTable[currentPos] != null&& (!hashTable[currentPos].isActive || !hashTable[currentPos].element.equals(element))) {
      probeNumber++;
      if (probeNumber > 1) {
        probingConflicts++;
      }

      currentPos = (initialHash + (probeNumber * probeNumber)) % capacity;

      if (currentPos < 0) {
        currentPos = Math.abs(currentPos);
      }
    }

    return currentPos;
  }

  private int adaptedHash(T element) {
    int hashVal = (31 * element.hashCode());
    hashVal %= capacity;

    while (hashVal < 0) {
      hashVal += capacity;
    }

    return hashVal;
  }

  private T getObjectAt(int currentPos) {
    if (hashTable[currentPos] != null && hashTable[currentPos].isActive) {
      return hashTable[currentPos].element;
    }
    return null;
  }

  private boolean isActive(int currentPos) {
    return hashTable[currentPos] != null && hashTable[currentPos].isActive;
  }

  @SuppressWarnings("unchecked")
  private void resize() {
    HashEntry<T>[] oldHashTable = hashTable;
    capacity = capacity * 2;
    hashTable = (HashEntry<T>[]) new HashEntry[capacity];
    size = 0;

    for (HashEntry<T> entry : oldHashTable) {
      if (entry != null && entry.isActive) {
        insert(entry.element);
      }
    }
  }

  public int size() {
    return size;
  }

  public int capacity() {
    return capacity;
  }

  public int getTotalConflicts() {
    return initialConflicts + probingConflicts;
  }

  public int getInitialConflicts() {
    return initialConflicts;
  }

  public int getProbingConflicts() {
    return probingConflicts;
  }
}
