package io.spbx.util.collect.map;

import io.spbx.util.collect.container.IntSize;

import java.util.Objects;

/**
 * Hash table with open addressing.
 *
 * <p>This contains some common methods of Map interface, including size, isEmpty, put, get, remove,
 * containsKey
 *
 * <p>For open addressing hash table, we use 100% of the inner array space, and it is a close
 * hashing. When collision occurs, it has to find an empty position to store the element. Pedro
 * provided few methods to probe a new index. I am not quite familiar with quadratic probing, so the
 * default probing method is linear probing, which means if the index is occupied, it automatically
 * move to the next index.
 *
 * <p>I have improved it by adding resize method. When the space is full, it will automatically
 * expand the capacity for 2 times. Also, when then size is one fourth of the capacity, it will
 * shrink its capacity to half. So we can add elements constantly and use the space efficiently.
 *
 * <p>I also added test main method, you can invoke this class to see what is happening during the
 * put and remove methods.
 *
 * @author Pedro Furtado
 * @author River Lin
 */
public class OpenHashTable<K, V> implements IntSize {
    // Impl Ref:
    //   https://gist.github.com/RuChuanLin/640626d48dc0df4802c70081400c4677

    private HashTableNode<K, V>[] table;
    private int size;

    /** Constructor method. */
    @SuppressWarnings("unchecked")
    OpenHashTable(int capacity) {
        this.size = 0;
        this.table = new HashTableNode[capacity];
    }

    public OpenHashTable() {
        this(16);
    }

    @Override
    public int size() {
        return size;
    }

    public int capacity() {
        return table.length;
    }

    HashTableNode<K, V>[] table() {
        return table;
    }

    /**
     * @param key key
     * @param value value
     */
    public void put(K key, V value) {
        int hash = hash(key);
        int index = getIndex(hash);
        int startIndex = index;
        HashTableNode<K, V> e;
        while ((e = table[index]) != null) {
            if (hash == e.hash && Objects.equals(e.key, key)) {
                table[index].value = value;
                return;
            }
            index = linearProbing(index);
            if (index == startIndex) {
                resize(capacity() * 2);
                index = getIndex(hash);
                startIndex = index;
            }
        }
        table[index] = new HashTableNode<>(hash, key, value);
        size++;
    }

    @SuppressWarnings("unchecked")
    private void resize(int newCapacity) {
        HashTableNode<K, V>[] oldTab = table;
        table = new HashTableNode[newCapacity];
        size = 0;
        for (HashTableNode<K, V> hashNode : oldTab) {
            if (hashNode != null) {
                put(hashNode.key, hashNode.value);
            }
        }
    }

    /**
     * Search method.
     *
     * <p>Search an element in hash table.
     */
    public V get(K key) {
        int hash = hash(key);
        HashTableNode<K, V> e;
        int index = getIndex(hash);
        int startIndex = index;
        while ((e = table[index]) != null) {
            if (hash == e.hash && Objects.equals(e.key, key)) {
                return table[index].value;
            }
            index = linearProbing(index);
            if (index == startIndex) return null;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) == null;
    }

    /**
     * Remove method.
     *
     * <p>Remove an element from the hash table.
     */
    public V remove(K key) {
        int hash = hash(key);
        int index = getIndex(hash);
        int startIndex = index;
        HashTableNode<K, V> e;
        V oldVal = null;
        while ((e = table[index]) != null) {
            if (hash == e.hash && Objects.equals(e.key, key)) {
                oldVal = table[index].value;
                table[index] = null;
                size--;
            }
            index = linearProbing(index);
            if (index == startIndex) break;
        }
        if (size == capacity() / 4 && capacity() / 2 != 0) {
            resize(capacity() / 2);
        }
        return oldVal;
    }

    /** Linear probing. */
    private int linearProbing(int index) {
        return (index + 1) % this.capacity();
    }

    /** Quadratic probing. */
    private int quadraticProbing(int index) {
        return ((index * index) & Integer.MAX_VALUE) % this.capacity();
    }

    /** Double hashing. */
    private int h_double(int key, int i) {
        return (this.h_double2(key) + i) % this.capacity();
    }

    /** Auxiliar hash function to Double hashing made by h_double() method. */
    private int h_double2(int key) {
        return (1 + key) % (this.capacity() - 1);
    }

    /**
     * We get hashcode of key object, and use & MAX integer to convert negative hash to positive,
     * which is more cost-efficient than Math.abs(n)
     *
     * @param key key
     * @return the index in the hashNodes array
     */
    int hash(K key) {
        return key == null ? 0 : Integer.MAX_VALUE & key.hashCode();
    }

    int getIndex(int hash) {
        return hash % capacity();
    }

    /** Element of hash table. */
    static class HashTableNode<K, V> {
        final int hash;
        final K key;
        V value;

        HashTableNode(int hash, K key, V value) {
            this.hash = hash;
            this.key = key;
            this.value = value;
        }
    }
}
