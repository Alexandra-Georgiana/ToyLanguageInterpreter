package model.Statements;

import javafx.util.Pair;
import model.ADTs.MyCountSemaphoreTbl;
import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.Exceptions.MyException;
import model.Expressions.IExpression;
import model.State.PrgState;
import model.Types.IntType;
import model.Values.IValue;
import model.Values.IntValue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CreateCountSemaphoreStmt implements IStmt {
    String var;
    IExpression exp;
    static final Lock lock = new ReentrantLock();

    public CreateCountSemaphoreStmt(String var, IExpression exp) {
        this.var = var;
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "CreateCountSemaphore(" + var + ", " + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        MyIDictionary<String, IValue> symTable = state.getSymTable().peek();
        MyIHeap heap = state.getHeap();
        MyCountSemaphoreTbl semaphoreTable = state.getCountSemaphoreTbl();
        IValue val1 = exp.eval(symTable, heap);
        IntValue intVal = (IntValue) val1;

        if(!symTable.containsKey(var)) {
            throw new MyException("Variable " + var + " is not defined");
        }

        if(!symTable.get(var).getType().equals(new IntType())) {
            throw new MyException("Variable " + var + " is not of type int");
        }

        IValue val = exp.eval(symTable, state.getHeap());
        if(!val.getType().equals(new IntType())) {
            throw new MyException("Expression " + exp.toString() + " is not of type int");
        }

        Pair<Integer, List<Integer>> pairToAdd = new Pair<>(intVal.getVal(), new ArrayList<>());
        semaphoreTable.add(pairToAdd);
        symTable.put(var, new IntValue(semaphoreTable.getContent().size() - 1));

        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, model.Types.IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) throws MyException {
        model.Types.IType typeVar = typeEnv.get(var);
        model.Types.IType typeExp = exp.typecheck(typeEnv);
        if(typeVar.equals(new IntType())) {
            if(typeExp.equals(new IntType())) {
                return typeEnv;
            } else {
                throw new MyException("Expression " + exp.toString() + " is not of type int");
            }
        } else {
            throw new MyException("Variable " + var + " is not of type int");
        }
    }
}
