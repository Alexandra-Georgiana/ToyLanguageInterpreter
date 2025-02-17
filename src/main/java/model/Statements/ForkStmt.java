package model.Statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIStack;
import model.ADTs.MyStack;
import model.Exceptions.MyException;
import model.State.PrgState;
import model.Values.IValue;

import java.util.Iterator;

public class ForkStmt implements IStmt{
    private final IStmt stmt;

    public ForkStmt(IStmt stmt){
        this.stmt = stmt;
    }

    @Override
    public String toString(){
        return "fork(" + stmt.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException{
        MyIStack<IStmt> newStack = new MyStack<>();
        newStack.push(stmt);

        MyIStack<MyIDictionary<String, IValue>> originalSymTableStack = state.getSymTable();
        MyIStack<MyIDictionary<String, IValue>> newSymTable = new MyStack<>();

        Iterator<MyIDictionary<String, IValue>> iterator = originalSymTableStack.iterator();
        while (iterator.hasNext()) {
            newSymTable.push(iterator.next().clone());
        }

        return new PrgState(newStack, newSymTable, state.getOut(), state.getFiles(), state.getHeap(), state.getProcTbl(), state.getLockTbl(), state.getToySemaphoreTbl(), state.getCountSemaphoreTbl(), state.getLatchTbl(), true, state.getBarrierTable());
    }

    @Override
    public MyIDictionary<String, model.Types.IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) throws MyException {
        stmt.typecheck(typeEnv.clone());
        return typeEnv;
    }
}
