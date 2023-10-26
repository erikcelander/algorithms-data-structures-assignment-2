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


  
}
