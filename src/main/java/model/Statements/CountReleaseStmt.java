package model.Statements;

import model.ADTs.MyCountSemaphoreTbl;
import model.ADTs.MyIDictionary;
import model.State.PrgState;
import model.Types.IntType;
import model.Values.IValue;
import model.Values.IntValue;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class CountReleaseStmt implements IStmt{
    private final String var;
    private static final Lock lock = new ReentrantLock();

    public CountReleaseStmt(String var){
        this.var = var;
    }

    @Override
    public String toString(){
        return "CountRelease(" + var + ")";
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

        List<Integer> list= semaphoreTable.get(foundIndex).getValue();
        if(list.contains(state.getId())){
            list.remove((Integer) state.getId());
        }


        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, model.Types.IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) {
        model.Types.IType typeVar = typeEnv.get(var);
        if(!typeVar.equals(new IntType())){
            throw new RuntimeException("Variable " + var + " is not of type int");
        }
        return typeEnv;
    }
}
