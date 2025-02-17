package model.ADTs;

import model.Exceptions.MyException;

import java.util.Collection;
import java.util.Map;

public interface MyILockTbl {
    Integer get(Integer key) throws MyException;
    public Collection<Integer> values();
    boolean isDefined(Integer key);
    void update(Integer key, Integer value);
    void remove(Integer key) throws MyException;
    String toString();
    MyILockTbl deepCopy() throws MyException;
    Integer getFreeLocation();
    Map<Integer, Integer> getContent();
}
