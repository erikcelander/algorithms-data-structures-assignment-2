package uppgift;

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

  public HashTableQuadraticProbing(int capacity) {
    this.capacity = capacity;
    this.hashTable = new HashEntry[capacity];
    this.size = 0;
  }

  public void put(K key, V value) {
    int hash = getHash(key);
    int i = 0;
    int quadraticHash = hash;

    while (hashTable[quadraticHash] != null && !hashTable[quadraticHash].key.equals(key)) {
      i++;
      quadraticHash = (hash + i * i) % capacity;
    }

    hashTable[quadraticHash] = new HashEntry<>(key, value);
    size++;

  }

  private int getHash(K key) {
    return key.hashCode() % capacity;
  }

}
