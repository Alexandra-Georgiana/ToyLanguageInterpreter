package model.ADTs;

import model.Exceptions.MyException;

import java.util.Collection;
import java.util.Map;

public class MyLockTbl implements MyILockTbl{
    Map<Integer, Integer> lockTbl;

    public MyLockTbl(Map<Integer, Integer> lockTbl){
        this.lockTbl = lockTbl;
    }

    @Override
    public Integer get(Integer key) throws MyException {
        if(!lockTbl.containsKey(key)){
            throw new MyException("Key not found in lock table");
        }
        return lockTbl.get(key);
    }

    @Override
    public boolean isDefined(Integer key) {
        return lockTbl.containsKey(key);
    }

    @Override
    public Collection<Integer> values() {
        return lockTbl.values();
    }

    @Override
    public void update(Integer key, Integer value) {
        lockTbl.put(key, value);
    }

    @Override
    public void remove(Integer key) throws MyException {
        if(!lockTbl.containsKey(key)){
            throw new MyException("Key not found in lock table");
        }
        lockTbl.remove(key);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(Integer key : lockTbl.keySet()){
            result.append(key.toString()).append(" -> ").append(lockTbl.get(key).toString()).append("\n");
        }
        return result.toString();
    }

    @Override
    public MyILockTbl deepCopy() throws MyException {
        return new MyLockTbl(Map.copyOf(lockTbl));
    }

    @Override
    public Integer getFreeLocation() {
        int freeLocation = 0;
        for (Integer key : lockTbl.keySet()){
            if (key > freeLocation){
                freeLocation = key;
            }
        }
        return freeLocation;
    }

    @Override
    public Map<Integer, Integer> getContent() {
        return lockTbl;
    }
}
