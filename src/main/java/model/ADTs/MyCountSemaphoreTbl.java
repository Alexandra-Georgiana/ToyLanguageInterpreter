package model.ADTs;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyCountSemaphoreTbl implements MyICountSemaohoreTbl{
    Map<Integer, Pair<Integer, List<Integer>>> countSemaphoreTbl;
    int freeLocation;

    public MyCountSemaphoreTbl() {
        countSemaphoreTbl = new ConcurrentHashMap<>();
        freeLocation = 0;
    }

    @Override
    public synchronized Pair<Integer, List<Integer>> get(int key) {
        if(!isDefined(key))
            return null;
        return countSemaphoreTbl.get(key);
    }

    @Override
    public boolean isDefined(int key) {
        return countSemaphoreTbl.containsKey(key);
    }

    @Override
    public int getFreeLocation() {
        synchronized (this) {
            return freeLocation++;
        }
    }

    @Override
    public void update(int key, Pair<Integer, List<Integer>> value) {
        synchronized (this){
            countSemaphoreTbl.put(key, value);
        }
    }

    @Override
    public void setFreeLocation(int freeLocation) {
        synchronized (this) {
            this.freeLocation = freeLocation;
        }
    }

    @Override
    public Map<Integer, Pair<Integer, List<Integer>>> getContent() {
        synchronized (this) {
            return countSemaphoreTbl;
        }
    }

    @Override
    public void setContent(Map<Integer, Pair<Integer, List<Integer>>> newContent) {
        synchronized (this) {
            countSemaphoreTbl = newContent;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Integer key : countSemaphoreTbl.keySet()) {
            result.append(key).append(" -> ").append(countSemaphoreTbl.get(key)).append(",   ");
        }
        return result.toString();
    }

    @Override
    public void add(Pair<Integer, List<Integer>> semEntry) {
        synchronized (this) {
            countSemaphoreTbl.put(freeLocation++, semEntry);
        }
    }
}
