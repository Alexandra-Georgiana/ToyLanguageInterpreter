package model.ADTs;

import javafx.util.Pair;
import model.Exceptions.MyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface MyICountSemaohoreTbl {
    Pair<Integer, List<Integer>> get(int key) throws MyException;
    boolean isDefined(int key);
    int getFreeLocation();
    void update(int key,  Pair<Integer, List<Integer>> value) throws MyException;
    void setFreeLocation(int freeAddress);
    Map<Integer,  Pair<Integer, List<Integer>>> getContent();
    void setContent(Map<Integer,  Pair<Integer, List<Integer>>> newSemaphoreTable);
    void add(Pair<Integer, List<Integer>> semEntry);

}
