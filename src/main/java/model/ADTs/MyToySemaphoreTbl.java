package model.ADTs;

import model.Exceptions.MyException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.List;

public class MyToySemaphoreTbl implements MyIToySemaphoreTbl{
    Map<Integer, Tuple<Integer, List<Integer>, Integer>> toySemaphoreTbl;
    int freeLocation;

    public MyToySemaphoreTbl() {
        toySemaphoreTbl = new ConcurrentHashMap<>();
        freeLocation = 0;
    }

    @Override
    public synchronized Tuple<Integer, List<Integer>, Integer> get(int key) throws MyException {
        if(!isDefined(key))
            throw new MyException("Semaphore not defined");
        return toySemaphoreTbl.get(key);
    }

    @Override
    public boolean isDefined(int key) {
        return toySemaphoreTbl.containsKey(key);
    }

    @Override
    public int getFreeLocation() {
        synchronized (this) {
            return freeLocation++;
        }
    }

    @Override
    public void update(int key, Tuple<Integer, List<Integer>, Integer> value) throws MyException {
        synchronized (this){
            toySemaphoreTbl.put(key, value);
        }
    }

    @Override
    public void setFreeLocation(int freeLocation) {
        synchronized (this) {
            this.freeLocation = freeLocation;
        }
    }

    @Override
    public Map<Integer, Tuple<Integer, List<Integer>, Integer>> getContent() {
        synchronized (this) {
            return toySemaphoreTbl;
        }
    }

    @Override
    public void setContent(Map<Integer, Tuple<Integer, List<Integer>, Integer>> newContent) {
        synchronized (this) {
            toySemaphoreTbl = newContent;
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Integer key : toySemaphoreTbl.keySet()) {
            result.append(key).append(" -> ").append(toySemaphoreTbl.get(key)).append(",   ");
        }
        return result.toString();
    }
}
