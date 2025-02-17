package model.ADTs;

import model.Statements.IStmt;

import java.util.Stack;

public class MyStack<T> implements MyIStack<T> {
    private final Stack<T> stack;

    public MyStack() {
        stack = new Stack<>();
    }

    @Override
    public void push(T value) {
        stack.add(value);
    }

    @Override
    public T pop() {
        return stack.remove(stack.size() - 1);
    }

    @Override
    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (T elem : stack) {
            result.append(elem.toString()).append(",   ");
        }
        return result.toString();
    }

    @Override
    public MyIStack<T> clone() {
        MyIStack<T> clone = new MyStack<>();
        for (int i = stack.size() - 1; i >= 0; i--) {
            clone.push(stack.get(i));
        }
        return clone;
    }

    @Override
    public void clear() {
        stack.clear();
    }

    @Override
    public T peek() {
        return stack.peek();
    }

    @Override
    public Stack<T> getStack() {
        return stack;
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return stack.iterator();
    }
}
