package model.Statements;

import javafx.util.Pair;
import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.Expressions.IExpression;
import model.State.PrgState;
import model.Types.IType;
import model.Values.IValue;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class AwaitBarrierStmt implements IStmt{
    private IExpression exp;

    public AwaitBarrierStmt(IExpression exp){
        this.exp = exp;
    }

    @Override
    public String toString(){
        return "awaitBarrier(" + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();

        IValue val = state.getSymTableTop().get(exp.toString());
        if(!val.getType().equals(new model.Types.IntType())){
            throw new MyException("Expression " + exp.toString() + " is not of type int");
        }

        int index = ((model.Values.IntValue)val).getVal();
        if(!state.getBarrierTable().getContent().containsKey(index)){
            throw new MyException("Index " + index + " is not in the barrier table");
        }

        Pair<Integer, List<Integer>> pair = state.getBarrierTable().getContent().get(index);

        int n1 = pair.getValue().size();
        int n2 = pair.getKey();

        List<Integer> threads = pair.getValue();

        if(n1>n2){
            if(pair.getValue().contains(state.getId())){
                state.getExeStack().push(new AwaitBarrierStmt(exp));
        }else{
            threads.add(state.getId());
            state.getBarrierTable().put(index, new Pair<>(n2, threads));
            }
    }
    lock.unlock();
    return null;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) throws MyException {
        model.Types.IType typexp = exp.typecheck(typeEnv);
        if (typexp.equals(new model.Types.IntType())) {
            return typeEnv;
        } else {
            throw new MyException("Expression " + exp.toString() + " is not of type int");
        }
    }
}
