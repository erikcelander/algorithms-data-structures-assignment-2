package uppgift;

import java.util.Arrays;

public class HashTableQuadraticProbing<K, V> {
  private static class HashEntry<K, V> {
    K key;
    V value;

    HashEntry(K key, V value) {
      this.key = key;
      this.value = value;
    }
  }

  private HashEntry<K, V>[] hashTable;
  private int capacity;
  private int size;

  @SuppressWarnings("unchecked")
  public HashTableQuadraticProbing(int capacity) {
    this.capacity = capacity;
    this.hashTable = (HashEntry<K, V>[]) new HashEntry[capacity];
    this.size = 0;
  }

  public void insert(K key, V value) {
    int hash = getHash(key);
    int i = 0;
    int quadraticHash = hash;

    while (hashTable[quadraticHash] != null && !hashTable[quadraticHash].key.equals(key)) {
      i++;
      quadraticHash = (hash + i * i) % capacity;
    }

    hashTable[quadraticHash] = new HashEntry<>(key, value);
    size++;

    if (size >= capacity / 2) {
      resize();
    }
  }

  private void resize() {
    capacity = capacity * 2;
    size = 0;

    // save old hash table
    HashEntry<K, V>[] oldHashTable = hashTable;
    // create new hash table with double the capacity
    hashTable = Arrays.copyOf(oldHashTable, capacity);

    // put all entries from old hash table into new hash table
    for (HashEntry<K, V> entry : oldHashTable) {
      if (entry != null) {
        insert(entry.key, entry.value);
      }
    }
  }

  private int getHash(K key) {
    return key.hashCode() % capacity;
  }



  
}
