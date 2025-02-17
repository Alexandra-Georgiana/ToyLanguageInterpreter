package model.ADTs;

import model.Statements.IStmt;

import java.util.Iterator;
import java.util.Stack;

public interface MyIStack<T> {
    void push(T value);
    
    T pop();
    
    boolean isEmpty();
    
    @Override
    String toString();
    
    MyIStack<T> clone();

    void clear();

    T peek();

    Stack<T> getStack();

    Iterator<T> iterator();
}
