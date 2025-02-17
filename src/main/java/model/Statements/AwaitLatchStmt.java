package model.Statements;

import model.ADTs.*;
import model.Exceptions.MyException;
import model.State.PrgState;
import model.Values.IValue;
import model.Values.IntValue;

public class AwaitLatchStmt implements IStmt{
    private String var;

    public AwaitLatchStmt(String var){
        this.var = var;
    }

    @Override
    public String toString(){
        return "await(" + var + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable().peek();
        MyIStack<IStmt> stack = state.getExeStack();
        MyILatchTbl latchTable = state.getLatchTbl();

        if(!symTable.containsKey(var))
            throw new MyException("Variable " + var + " is not defined.");

        if(!symTable.get(var).getType().equals(new model.Types.IntType()))
            throw new MyException("Variable " + var + " is not of type int.");

        IValue index = symTable.get(var);

        if(!index.getType().equals(new model.Types.IntType()))
            throw new MyException("Variable " + var + " is not of type int.");

        IntValue intIndex = (IntValue) index;
        if(!latchTable.isDefined(intIndex.getVal()))
            throw new MyException("Index " + intIndex.getVal() + " is not in the latch table.");

        if(latchTable.getVal(intIndex.getVal()) != 0)
            stack.push(new AwaitLatchStmt(var));

        return null;
    }

    @Override
    public MyIDictionary<String, model.Types.IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) throws MyException{
        if(!typeEnv.containsKey(var))
            throw new MyException("Variable " + var + " is not defined.");

        if(!typeEnv.get(var).equals(new model.Types.IntType()))
            throw new MyException("Variable " + var + " is not of type int.");

        return typeEnv;
    }
}
