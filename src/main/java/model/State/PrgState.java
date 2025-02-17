package model.State;

import model.ADTs.*;
import model.Exceptions.MyException;
import model.Statements.IStmt;
import model.Values.IValue;
import model.Values.RefValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.Map;

public class PrgState {
    private final MyIStack<IStmt> exeStack;
    private final MyIStack<MyIDictionary<String, IValue>> symTable;
    private MyIList<IValue> out;
    private final MyIDictionary<String, BufferedReader> files;
    private MyIHeap heap;
    private final int id;
    private static final AtomicInteger idGenerator = new AtomicInteger(0);
    MyProcTbl procTbl;
    MyLockTbl lockTbl;
    MyToySemaphoreTbl toySemaphoreTbl;
    MyCountSemaphoreTbl countSemaphoreTbl;
    MyLatchTbl latchTbl;
    private boolean isChild = false;
    MyCyclicBarrier barrierTable;

    public PrgState(IStmt originalProgram, MyProcTbl procTbl) {
        this.id = getNextId();
        exeStack = new MyStack<>();
        symTable = new MyStack<>();
        symTable.push(new MyDictionary<String, IValue>());
        out = new MyList<IValue>();
        files = new MyDictionary<String, BufferedReader>();
        heap = new MyHeap();
        exeStack.push(originalProgram);
        this.procTbl = procTbl;
        this.lockTbl = new MyLockTbl(new HashMap<>());
        this.toySemaphoreTbl = new MyToySemaphoreTbl();
        this.countSemaphoreTbl = new MyCountSemaphoreTbl();
        this.latchTbl = new MyLatchTbl();
        this.isChild = false;
        this.barrierTable = new MyCyclicBarrier();
    }

    public PrgState(MyIStack<IStmt> newStack, MyIStack<MyIDictionary<String, IValue>> newSymTable, MyIList<IValue> out, MyIDictionary<String, BufferedReader> files, MyIHeap heap, MyProcTbl procTbl, MyLockTbl lockTbl, MyToySemaphoreTbl toySemaphoreTbl, MyCountSemaphoreTbl countSemaphoreTbl, MyLatchTbl latchTbl, boolean child, MyCyclicBarrier barrierTable) {
        this.id = getNextId();
        exeStack = newStack;
        this.isChild  = child;
        symTable = newSymTable;
        this.out = out;
        this.files = files;
        this.heap = heap;
        this.procTbl = procTbl;
        this.lockTbl = lockTbl;
        this.toySemaphoreTbl = toySemaphoreTbl;
        this.countSemaphoreTbl = countSemaphoreTbl;
        this.latchTbl = latchTbl;
        this.barrierTable = barrierTable;
    }

    private static synchronized int getNextId() {
        return idGenerator.incrementAndGet();
    }

    public MyIStack<IStmt> getExeStack() {
        return this.exeStack;
    }

    public void setExeStack(MyIStack<IStmt> exeStack) {
        this.exeStack.clear();
        this.exeStack.push(exeStack.pop());
    }

    public MyIStack<MyIDictionary<String, IValue>> getSymTable() {
        return this.symTable;
    }

    public void setSymTable(MyIStack<MyIDictionary<String, IValue>> newSymTable) {
//        this.symTable.clear();
//        for (String key : symTable.keys()) {
//            this.symTable.put(key, symTable.get(key));
//        }
        this.symTable.clear();
        this.symTable.push(newSymTable.pop());
    }

    public MyIDictionary<String, IValue> getSymTableTop(){
        if (symTable.isEmpty()) {
            return new MyDictionary<>();
        }
        return symTable.peek();
    }

    public List<Integer> getAddrFromSymTable(Collection<IValue> symTableValues){
        return symTableValues.stream()
                .filter(v -> v instanceof RefValue)
                .map(v -> {RefValue v1 = (RefValue)v; return v1.getAddress();})
                .collect(Collectors.toList());
    }

    public MyIList<IValue> getOut() {
        return this.out;
    }

    public void setOut(MyIList<IValue> out) {
        this.out.clear();
    }

    public MyIDictionary<String, BufferedReader> getFiles() {
        return this.files;
    }

    public void setFiles(MyIDictionary<String, BufferedReader> files) {
        this.files.clear();
        for (String key : files.keys()) {
            this.files.put(key, files.get(key));
        }
    }

    public MyIHeap getHeap() {
        return this.heap;
    }

    public void setHeap(MyIHeap heap) {
        this.heap = heap;
    }

    public Boolean isNotCompleted(){
        return !exeStack.isEmpty();
    }

    public PrgState oneStep() throws MyException, IOException {
        if(exeStack.isEmpty()){
            throw new MyException("Program state stack is empty");
        }

        IStmt crtStmt = exeStack.pop();
        return crtStmt.execute(this);
    }

    public int getId() {
        synchronized (this) {
            return id;
        }
    }

    public MyIDictionary<String, BufferedReader> getFileTable() {
        return files;
    }

    public MyProcTbl getProcTbl() {
        return procTbl;
    }

    public MyLockTbl getLockTbl() {
        return lockTbl;
    }

    public void setLockTbl(MyLockTbl lockTbl) {
        this.lockTbl = lockTbl;
    }

    public MyToySemaphoreTbl getToySemaphoreTbl() {
        return toySemaphoreTbl;
    }

    public MyCountSemaphoreTbl getCountSemaphoreTbl() {
        return countSemaphoreTbl;
    }

    public MyLatchTbl getLatchTbl() {
        return latchTbl;
    }

    public Map<Integer, Integer> getLatchPopulate(){
        return latchTbl.getLatchTable();
    }

    public boolean isChild() {
        return isChild;
    }

    public void setChild(boolean child) {
        isChild = child;
    }

    public MyCyclicBarrier getBarrierTable() {
        return barrierTable;
    }

    @Override
    public String toString() {
        return """
            PrgState: {
                id: %s
                exeStack: %s
                symTable: %s 
                out: %s
                files: %s 
                heap: %s
                procTbl: %s 
                lockTbl: %s
                toySemaphoreTbl: %s
                countSemaphoreTbl: %s
                latchTbl: %s
            }
            """.formatted(id, exeStack.toString(), symTable.toString(), out.toString(), files.toString(), heap.toString(), procTbl.toString(), lockTbl.toString(), toySemaphoreTbl.toString(), countSemaphoreTbl.toString(), latchTbl.toString());
    }

    public MyIStack<IStmt> getExcutionStack() {
        return exeStack;
    }
}
