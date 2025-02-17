package model.Statements;

import javafx.util.Pair;
import model.ADTs.MyCountSemaphoreTbl;
import model.ADTs.MyIDictionary;
import model.State.PrgState;
import model.Types.IntType;
import model.Values.IValue;
import model.Values.IntValue;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class CountAcquireStmt implements IStmt{
    private final String var;
    private static final Lock lock = new ReentrantLock();

    public CountAcquireStmt(String var){
        this.var = var;
    }

    @Override
    public String toString(){
        return "CountAcquire(" + var + ")";
    }

    @Override
    public PrgState execute(PrgState state){
        lock.lock();
        MyIDictionary<String, IValue> symTable = state.getSymTable().peek();
        MyCountSemaphoreTbl semaphoreTable = state.getCountSemaphoreTbl();

        if(!symTable.containsKey(var)){
            throw new RuntimeException("Variable " + var + " is not defined");
        }

        if(!symTable.get(var).getType().equals(new IntType())){
            throw new RuntimeException("Variable " + var + " is not of type int");
        }

        int foundIndex = ((IntValue) symTable.get(var)).getVal();

        if(!semaphoreTable.isDefined(foundIndex)){
            throw new RuntimeException("Semaphore is not defined for index " + foundIndex);
        }

        int N1 = semaphoreTable.get(foundIndex).getKey();
        List<Integer> list = semaphoreTable.get(foundIndex).getValue();
        int length = list.size();

        if(N1 > length){
            if(!list.contains(state.getId())){
                list.add(state.getId());
                //semaphoreTable.update(foundIndex, new Pair<>(N1, list));
            }
        }else{
            state.getExeStack().push(this);
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, model.Types.IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv){
        model.Types.IType typeVar = typeEnv.get(var);
        if(typeVar.equals(new IntType())){
            return typeEnv;
        }else{
            throw new RuntimeException("Variable " + var + " is not of type int");
        }
    }
}
