package model.ADTs;

import javafx.util.Pair;
import model.Exceptions.MyException;
import model.Statements.IStmt;

import java.util.Collection;
import java.util.List;
import java.util.Map;


public interface MyIProcTbl {

    boolean isDefined(String key);
    void update(String key, Pair<List<String>, IStmt> value);
    Pair<List<String>, IStmt> get(String key) throws MyException;;
    Collection<Pair<List<String>, IStmt>> values();
    void remove(String key) throws MyException;
    Map<String, Pair<List<String>, IStmt>> getContent();
    MyIProcTbl deepCopy() throws MyException;

    Integer getFreeLocation();
}
