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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ToyAcquireStmt implements IStmt{
    private final String var;
    private static final Lock lock = new ReentrantLock();

    public ToyAcquireStmt(String var){
        this.var = var;
    }

    @Override
    public String toString(){
        return "ToyAcquire(" + var + ")";
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
            int N1 = semaphoreEntry.getFirst();
            List<Integer> List1 = semaphoreEntry.getSecond();
            int N2 = semaphoreEntry.getThird();
            int NL = List1.size();

            if ((N1 - N2) > NL) {
                if (!List1.contains(state.getId())) {
                    List1.add(state.getId());
                    semaphoreTable.update(foundIndex, new Tuple<>(N1, List1, N2));
                }
            } else {
                state.getExeStack().push(this);
            }
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public  MyIDictionary<String, model.Types.IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) throws MyException {
        model.Types.IType typeVar = typeEnv.get(var);
        if(!typeVar.equals(new model.Types.IntType()))
            throw new MyException("ToyAcquire: variable is not an integer");
        return typeEnv;
    }
}
