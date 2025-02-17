package model.ADTs;

import model.Values.IValue;

import java.util.List;

public class MyHeap implements MyIHeap{
    MyIDictionary<Integer, IValue> heap;
    int firstFree;

    public MyHeap(){
        heap = new MyDictionary<>();
        firstFree = 1;
    }

    @Override
    public int getFirstFree(){
        return firstFree;
    }

    @Override
    public int allocate(IValue value) {
        heap.put(firstFree, value);
        firstFree += 1;
        return firstFree - 1;
    }

    @Override
    public IValue read(int address) {
            return heap.get(address);
    }

    @Override
    public void write(int address, IValue value){
        heap.put(address, value);
    }

    @Override
    public void deallocate(int address){
        heap.remove(address);
    }

    @Override
    public MyIDictionary<Integer, IValue> getHeap(){
        return heap;
    }

    @Override
    public void setHeap(MyIDictionary<Integer, IValue> heap){
        this.heap = heap;
    }

    @Override
    public MyIDictionary<Integer, IValue> safeGarbageCollector(List<Integer> usedAddresses, MyIDictionary<Integer, IValue> heap) {
        MyIDictionary<Integer, IValue> newHeap = new MyDictionary<>();
        heap.getContent().entrySet().stream()
                .filter(entry -> usedAddresses.contains(entry.getKey()))
                .forEach(entry -> newHeap.put(entry.getKey(), entry.getValue()));
        return newHeap;
    }


    @Override
    public String toString(){
        return heap.toString();
    }
}
