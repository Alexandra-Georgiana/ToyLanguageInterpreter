package model.ADTs;

import model.Values.IntValue;
import javafx.util.Pair;

import java.util.Map;

public interface MyILatchTbl {
    public void add(IntValue value);
    public Integer getLastKey();
    public Integer getVal(Integer key);
    public boolean isDefined(Integer key);
    public void update(Integer key, IntValue value);
    public Map<Integer, IntValue> getContent(Integer key, Integer value);
    public String toString();
    public Map<Integer, Integer> getLatchTable();
}
