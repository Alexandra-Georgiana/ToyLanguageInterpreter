package model.ADTs;

import model.Exceptions.MyException;

import java.util.List;
import java.util.Map;

public interface MyIToySemaphoreTbl {
    Tuple<Integer, List<Integer>, Integer> get(int key) throws MyException;
    boolean isDefined(int key);
    int getFreeLocation();
    void update(int key, Tuple<Integer, List<Integer>, Integer> value) throws MyException;
    void setFreeLocation(int freeLocation);
    Map<Integer, Tuple<Integer, List<Integer>, Integer>> getContent();
    void setContent(Map<Integer, Tuple<Integer, List<Integer>, Integer>> newContent);
    String toString();
}
