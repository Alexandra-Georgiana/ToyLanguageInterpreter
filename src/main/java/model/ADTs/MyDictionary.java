package model.ADTs;

import java.util.HashMap;
import java.util.Set;

public class MyDictionary<K, V> implements MyIDictionary<K, V>{
    private final HashMap<K, V> dictionary;

    public MyDictionary() {
        dictionary = new HashMap<>();
    }

    @Override
    public HashMap<K, V> getContent() {
        return dictionary;
    }

    @Override
    public void put(K key, V value) {
        dictionary.put(key, value);
    }

    @Override
    public V get(K key) {
        return dictionary.get(key);
    }

    @Override
    public boolean containsKey(K key) {
        return dictionary.containsKey(key);
    }

    @Override
    public void remove(K key) {
        dictionary.remove(key);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (K key : dictionary.keySet()) {
            result.append(key).append(" -> ").append(dictionary.get(key)).append(",   ");
        }
        return result.toString();
    }

    @Override
    public MyIDictionary<K, V> clone() {
        MyIDictionary<K, V> clone = new MyDictionary<>();
        for (K key : dictionary.keySet()) {
            clone.put(key, dictionary.get(key));
        }
        return clone;
    }

    @Override
    public void update(K key, V value) {
        dictionary.put(key, value);
    }

    @Override
    public void clear() {
        dictionary.clear();
    }

    @Override
    public Set<K> keys() {
        return dictionary.keySet();
    }

}
