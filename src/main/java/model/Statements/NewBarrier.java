package model.Statements;

import javafx.util.Pair;
import model.ADTs.MyICyclicBarrier;
import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.Exceptions.MyException;
import model.Expressions.IExpression;
import model.State.PrgState;
import model.Types.IntType;
import model.Values.IValue;
import model.Values.IntValue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class NewBarrier implements IStmt{
    private String var;
    private IExpression exp;

    public NewBarrier(String var, IExpression exp){
        this.var = var;
        this.exp = exp;
    }

    @Override
    public String toString(){
        return "newBarrier(" + var + ", " + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        IValue nr = exp.eval(state.getSymTable().peek(), state.getHeap());
        if (!nr.getType().equals(new IntType())) {
            throw new MyException("Expression " + exp.toString() + " is not of type int");
        }
        int number = ((IntValue) nr).getVal();

        Pair<Integer, List<Integer>> pair = new Pair<>(number, new ArrayList<>());
        int address = state.getBarrierTable().getFreeLocation();
        state.getBarrierTable().put(address, pair);

        if (state.getSymTableTop().containsKey(var)) {
            state.getSymTableTop().update(var, new IntValue(address));
        } else {
            throw new MyException("Variable " + var + " is not defined");
        }
        lock.unlock();
        return null;
    }

    @Override
    public MyIDictionary<String, model.Types.IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) throws MyException {
        model.Types.IType typevar = typeEnv.get(var);
        model.Types.IType typexp = exp.typecheck(typeEnv);
        if (typevar.equals(new IntType())) {
            if (typexp.equals(new IntType())) {
                return typeEnv;
            } else {
                throw new MyException("Expression " + exp.toString() + " is not of type int");
            }
        } else {
            throw new MyException("Variable " + var + " is not of type int");
        }
    }

}
