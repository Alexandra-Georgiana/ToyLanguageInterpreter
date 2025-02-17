package model.ADTs;

import javafx.util.Pair;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MyCyclicBarrier implements MyICyclicBarrier{
    private ConcurrentHashMap<Integer, Pair<Integer, List<Integer>>> barrier;
    private int address = 1;

    public MyCyclicBarrier() {
        barrier = new ConcurrentHashMap<>();
    }

    @Override
    public ConcurrentHashMap<Integer, Pair<Integer, List<Integer>>> getContent() {
        return barrier;
    }

    @Override
    public void setContent(ConcurrentHashMap<Integer, Pair<Integer, List<Integer>>> newContent) {
        barrier = newContent;
    }

    @Override
    public void put(int key, Pair<Integer, List<Integer>> value) {
        barrier.put(key, value);
    }

    @Override
    public int getFreeLocation() {
        address++;
        return  address-1;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Integer key : barrier.keySet()) {
            result.append(key).append(" -> ").append(barrier.get(key)).append(",   ");
        }
        return result.toString();
    }
}
