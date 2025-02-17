package model.Statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIStack;
import model.ADTs.MyStack;
import model.Exceptions.MyException;
import model.Expressions.IExpression;
import model.Expressions.ValueExpression;
import model.Expressions.VariableExpression;
import model.State.PrgState;
import model.Values.*;
import model.Types.*;

public class WaitStmt implements IStmt{
    private final Integer time;

    public WaitStmt(Integer time){
        this.time = time;
    }

    @Override
    public String toString(){
        return "wait(" + time + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (Integer.parseInt(time.toString()) > 0) {
            IStmt newStmt = new CompStmt(new PrintStmt(new ValueExpression(new IntValue(Integer.parseInt(this.time.toString())))), new WaitStmt(time - 1));
            state.getExeStack().push(newStmt);
        }
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (!time.equals(new IntType()))
            throw new MyException("Wait Statement: Time is not of type int");
        return typeEnv;
    }
}
