package model.ADTs;

import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface MyICyclicBarrier {
    ConcurrentHashMap<Integer, Pair<Integer, List<Integer>>> getContent();
    void setContent(ConcurrentHashMap<Integer, Pair<Integer, List<Integer>>> newContent);
    void put(int key, Pair<Integer, List<Integer>> value);
    int getFreeLocation();
}
