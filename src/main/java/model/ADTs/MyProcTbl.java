package model.ADTs;

import javafx.util.Pair;
import model.Exceptions.MyException;
import model.Statements.IStmt;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class MyProcTbl implements MyIProcTbl{
    Map<String, Pair<List<String>, IStmt>> procTbl;

    public MyProcTbl(Map<String, Pair<List<String>, IStmt>> procTbl){
        this.procTbl = procTbl;
    }

    @Override
    public boolean isDefined(String key){
        return procTbl.containsKey(key);
    }

    @Override
    public void update(String key, Pair<List<String>, IStmt> value){
        procTbl.put(key, value);
    }

    @Override
    public Pair<List<String>, IStmt> get(String key) throws MyException {
        if (!procTbl.containsKey(key)){
            throw new MyException("Procedure " + key + " is not defined");
        }
        return procTbl.get(key);
    }

    @Override
    public Collection<Pair<List<String>, IStmt>> values() {
        return procTbl.values();
    }

    @Override
    public void remove(String key) throws MyException {
        if (!procTbl.containsKey(key)){
            throw new MyException("Procedure " + key + " is not defined");
        }
        procTbl.remove(key);
    }

    @Override
    public Map<String, Pair<List<String>, IStmt>> getContent(){
        return procTbl;
    }

    @Override
    public MyIProcTbl deepCopy() throws MyException {
        return new MyProcTbl(Map.copyOf(procTbl));
    }

    public String toString(){
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Pair<List<String>, IStmt>> entry : procTbl.entrySet()){
            result.append(entry.getKey()).append(" -> ").append(entry.getValue().getKey()).append(" ").append(entry.getValue().getValue()).append("\n");
        }
        return result.toString();
    }

    @Override
    public Integer getFreeLocation() {
        int freeLocation = 0;
        for (String key : procTbl.keySet()){
            if (Integer.parseInt(key) > freeLocation){
                freeLocation = Integer.parseInt(key);
            }
        }
        return freeLocation + 1;
    }
}
