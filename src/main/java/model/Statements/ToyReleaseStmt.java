package model.Statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyToySemaphoreTbl;
import model.ADTs.Tuple;
import model.Exceptions.MyException;
import model.State.PrgState;
import model.Types.IntType;
import model.Values.IValue;
import model.Values.IntValue;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class ToyReleaseStmt implements IStmt{
    String var;
    private static final Lock lock = new ReentrantLock();

    public ToyReleaseStmt(String var){
        this.var = var;
    }

    @Override
    public String toString(){
        return "ToyRelease(" + var + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        try {
            MyIDictionary<String, IValue> symTable = state.getSymTable().peek();
            MyToySemaphoreTbl semaphoreTable = state.getToySemaphoreTbl();

            if (!symTable.containsKey(var)) {
                throw new MyException("Variable " + var + " is not defined");
            }

            if (!symTable.get(var).getType().equals(new IntType())) {
                throw new MyException("Variable " + var + " is not of type int");
            }

            int foundIndex = ((IntValue) symTable.get(var)).getVal();

            if (!semaphoreTable.isDefined(foundIndex)) {
                throw new MyException("Semaphore is not defined for index " + foundIndex);
            }

            Tuple<Integer, List<Integer>, Integer> semaphoreEntry = semaphoreTable.get(foundIndex);
            List<Integer> waitingList = semaphoreEntry.getSecond();

            if (waitingList.contains(state.getId())) {
                waitingList.remove((Integer) state.getId());
                semaphoreTable.update(foundIndex, new Tuple<>(semaphoreEntry.getFirst(), waitingList, semaphoreEntry.getThird()));
            }
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public MyIDictionary<String, model.Types.IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) throws MyException {
        model.Types.IType typeVar = typeEnv.get(var);
        if(!typeVar.equals(new model.Types.IntType())){
            throw new MyException("Variable is not an integer");
        }
        return typeEnv;
    }
}
