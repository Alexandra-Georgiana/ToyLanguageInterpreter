package model.ADTs;

import java.util.LinkedList;
import java.util.List;

public class MyList<T> implements MyIList<T> {
    private final List<T> list;

    public MyList() {
        this.list = new LinkedList<>();
    }

    @Override
    public void add(T value) {
        list.add(value);
    }

    @Override
    public T remove(int index) {
        return list.remove(index);
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public MyIList<T> clone() {
        MyList<T> clonedList = new MyList<>();
        clonedList.list.addAll(this.list);
        return clonedList;
    }

    @Override
    public List<T> getList() {
        return list;
    }

    @Override
    public void addAll(MyIList<T> out) {
        list.addAll(out.getList());
    }

    @Override
    public String toString() {
        if (list.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        for (T value : list) {
            sb.append(value != null ? value.toString() : "null").append(", ");
        }
        return sb.toString();
    }
}