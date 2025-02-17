package model.ADTs;

import java.util.HashMap;
import java.util.Set;

public interface MyIDictionary<K, V> {
    void put(K key, V value);
    
    V get(K key);
    
    boolean containsKey(K key);
    
    void remove(K key);
    
    @Override
    String toString();
    
    void update(K key, V value);

    MyIDictionary<K, V> clone();

    void clear();

    Set<K> keys();

    public HashMap<K, V> getContent();

}
