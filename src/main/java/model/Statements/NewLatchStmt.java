package model.Statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.ADTs.MyILatchTbl;
import model.Exceptions.MyException;
import model.Expressions.IExpression;
import model.State.PrgState;
import model.Types.IntType;
import model.Values.IValue;
import model.Values.IntValue;

public class NewLatchStmt implements IStmt{
    private String var;
    private IExpression exp;

    public NewLatchStmt(String var, IExpression exp){
        this.var = var;
        this.exp = exp;
    }

    @Override
    public String toString(){
        return "newLatch(" + var + ", " + exp.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException{
        MyIDictionary<String, IValue> symTable = state.getSymTable().peek();
        MyIHeap heap = state.getHeap();
        MyILatchTbl latchTable = state.getLatchTbl();

        if(!symTable.containsKey(var))
            throw new MyException("Variable " + var + " is not defined.");

        if(!symTable.get(var).getType().equals(new IntType()))
            throw new MyException("Variable " + var + " is not of type int.");

        IValue val = exp.eval(symTable, heap);
        if(!val.getType().equals(new IntType()))
            throw new MyException("Expression " + exp.toString() + " is not of type int.");

        latchTable.add(new IntValue(((IntValue) val).getVal()));
        symTable.put(var, new IntValue(latchTable.getLastKey()));

        return null;
    }

    @Override
    public MyIDictionary<String, model.Types.IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) throws MyException{
        if(!typeEnv.containsKey(var))
            throw new MyException("Variable " + var + " is not defined.");

        if(!typeEnv.get(var).equals(new IntType()))
            throw new MyException("Variable " + var + " is not of type int.");

        if(!exp.typecheck(typeEnv).equals(new IntType()))
            throw new MyException("Expression " + exp.toString() + " is not of type int.");

        return typeEnv;
    }
}
