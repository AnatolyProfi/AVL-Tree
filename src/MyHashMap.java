import java.util.*;

public class MyHashMap<K, V> implements Map<K, V> {

    private Node<K, V>[] table = new Node[16];
    private int size;

    public int getIndex(int hashCode) {
        return hashCode & (table.length - 1);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size > 0;
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        for (Node<K, V> current : table) {
            if (current != null) {
                if (current.getValue().equals(value)) {
                    return true;
                } else {
                    while (current.getNext() != null) {
                        current = current.getNext();
                        if (current.getValue().equals(value)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        return getNode((K)key).getValue();
    }

    @Override
    public V put(K key, V value) {
        int index = getIndex(key.hashCode());
        Node<K, V> current = table[index];
        Node<K, V> newNode = new Node<>(getIndex(key.hashCode()), key, value, null);
        if (current == null) {
            table[index] = newNode;
            size++;
            return null;
        } else {
            if (key.equals(current.getKey())) {
                table[index] = newNode;
                return value;
            }
        }
        Node<K, V> previous = table[index];
        current = previous.getNext();
        while (current != null) {
            if (current.getKey().equals(key)) {
                previous.setNext(newNode);
                newNode.setNext(current.getNext());
            }
            previous = previous.getNext();
            current = current.getNext();
        }
        previous.setNext(newNode);
        size++;
        return value;
    }

    @Override
    public V remove(Object key) {
        int index = getIndex(key.hashCode());
        Node<K, V> current = table[index];
        if (current == null) {
            return null;
        } else {
            while (current.getNext() != null) {
                if (current.getKey().equals(key)) {
                    table[index] = current.getNext();
                    current.setNext(current.getNext());
                    size--;
                    return null;
                }
                current = current.getNext();
            }
        }
        table[index] = null;
        size--;
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        table = new Node[16];
        size = 0;
    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        List<V> list = new LinkedList<>();
        for(Node<K,V> current : table){
            if(current != null){
                while (current.getNext() != null) {
                    list.add(current.getValue());
                    current = current.getNext();
                }
                list.add(current.getValue());
            }
        }
        return list;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }

    public Node<K,V> getNode(Object key){
        int index = getIndex(key.hashCode());
        Node<K, V> current = table[index];
        if (current == null) {
            return null;
        } else {
            if (!current.getKey().equals(key)) {
                return null;
            }
            while (current.getNext() != null) {
                if (current.getKey().equals(key)) {
                    return current;
                }
                current = current.getNext();
            }
        }
        return current;
    }

    public Node<K,V>[] getChain(int index){
        Node<K,V>[] chain = new Node[16];
        Node<K, V> current = table[index];
        if (current == null) {
            return null;
        } else {
            int i = 0;
            while (current.getNext() != null) {
                chain[i] = current;
                current = current.getNext();
                i++;
            }
            chain[i] = current;
        }
        return chain;
    }
}
