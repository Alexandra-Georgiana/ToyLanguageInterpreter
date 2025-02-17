package model.Statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyILatchTbl;
import model.ADTs.MyIStack;
import model.Exceptions.MyException;
import model.Expressions.ValueExpression;
import model.State.PrgState;
import model.Values.IValue;
import model.Values.IntValue;

public class CountDownLatchStmt implements IStmt{
    private String var;

    public CountDownLatchStmt(String var){
        this.var = var;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable().peek();
        MyIStack<IStmt> stack = state.getExeStack();
        MyILatchTbl latchTable = state.getLatchTbl();
        Integer index = state.getId();

        if(!symTable.containsKey(var))
            throw new MyException("Variable " + var + " is not defined.");

        if(!symTable.get(var).getType().equals(new model.Types.IntType()))
            throw new MyException("Variable " + var + " is not of type int.");

        IValue latchIndex = symTable.get(var);

        if(!latchIndex.getType().equals(new model.Types.IntType()))
            throw new MyException("Variable " + var + " is not of type int.");

        IntValue intIndex = (IntValue) latchIndex;
        if(!latchTable.isDefined(intIndex.getVal()))
            throw new MyException("Index " + intIndex.getVal() + " is not in the latch table.");

        if(latchTable.getVal(intIndex.getVal()) > 0){
            latchTable.update(intIndex.getVal(), new IntValue(latchTable.getVal(intIndex.getVal()) - 1));
        }
        if(state.isChild())
            stack.push(new PrintStmt(new ValueExpression(new IntValue(index))));

        return null;
    }

    @Override
    public String toString(){
        return "countDown(" + var + ")";
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
