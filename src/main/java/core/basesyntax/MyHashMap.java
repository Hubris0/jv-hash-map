package core.basesyntax;

public class MyHashMap<K, V> implements MyMap<K, V> {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private int size = 0;
    static final int capacity = DEFAULT_INITIAL_CAPACITY;
    private Node<K, V>[] table;

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        table = new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size >= table.length * DEFAULT_LOAD_FACTOR) {
            resize();
        }
        int index = getIndex(key, table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if (keysEqual(node.key, key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }
        table[index] = new Node<>(hash(key), key, value, table[index]);
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = getIndex(key, table.length);
        Node<K, V> node = table[index];
        while (node != null) {
            if (keysEqual(node.key, key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    static class Node<K,V> {
        private final int hash;
        private final K key;
        private V value;
        private Node<K,V> next;

        public Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private boolean keysEqual(K firstKey, K secondKey) {
        return firstKey == null ? secondKey == null : firstKey.equals(secondKey);
    }

    private int hash(K key) {
        return key == null ? 0 : key.hashCode();
    }

    private int getIndex(K key, int length) {
        int hash = hash(key);
        return (hash ^ (hash >>> 16)) & (length - 1);
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Node<K, V>[] oldTable = table;
        table = new Node[oldTable.length * 2];
        size = 0;
        for (Node<K, V> head : oldTable) {
            Node<K, V> node = head;
            while (node != null) {
                Node<K, V> next = node.next;
                int index = getIndex(node.key, table.length);
                node.next = table[index];
                table[index] = node;
                size++;
                node = next;
            }
        }
    }
}
