package model.ADTs;

import model.Values.IValue;

import java.util.List;

public interface MyIHeap {
    int allocate(IValue value);
    IValue read(int address);
    void write(int address, IValue value);
    void deallocate(int address);
    int getFirstFree();
    MyIDictionary<Integer, IValue> safeGarbageCollector(List<Integer> usedAddresses, MyIDictionary<Integer, IValue> heap);
    MyIDictionary<Integer, IValue> getHeap();
    void setHeap(MyIDictionary<Integer, IValue> heap);
}
