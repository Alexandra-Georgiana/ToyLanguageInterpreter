package model.ADTs;

import javafx.util.Pair;
import model.Values.IntValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MyLatchTbl implements MyILatchTbl{
    private Map<Integer, IntValue> latchTable;
    private int locationCounter;

    public MyLatchTbl(){
        this.latchTable = new ConcurrentHashMap<>();
        this.locationCounter = 1;
    }

    @Override
    public Integer getLastKey() {
        Set<Integer> keys = latchTable.keySet();
        List<Integer> keysList = new ArrayList<>(keys);
        return keysList.getLast();
    }

    @Override
    public Integer getVal(Integer key) {
        return latchTable.get(key).getVal();
    }

    @Override
    public boolean isDefined(Integer key) {
        return latchTable.containsKey(key);
    }

    @Override
    public void update(Integer key, IntValue value) {
        latchTable.put(key, value);
    }

    @Override
    public Map<Integer, IntValue> getContent(Integer key, Integer value) {
        return latchTable;
    }

    @Override
    public void add(IntValue value) {
        latchTable.put(locationCounter, value);
        locationCounter++;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Integer key : latchTable.keySet()) {
            result.append(key).append(" -> ").append(latchTable.get(key)).append(",   ");
        }
        return result.toString();
    }

    @Override
    public Map<Integer, Integer> getLatchTable(){
        Map<Integer, Integer> result = new ConcurrentHashMap<>();
        for(Integer key : latchTable.keySet()){
            result.put(key, latchTable.get(key).getVal());
        }
        return result;
    }
}
