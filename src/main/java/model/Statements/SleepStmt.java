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

public class SleepStmt implements  IStmt{
    private Integer time;

    public SleepStmt(Integer time){
        this.time = time;
    }

    @Override
    public String toString(){
        return "sleep(" + time + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        if (time > 0) {
            MyIStack<IStmt> stk = state.getExeStack();
            stk.push(new SleepStmt(time - 1));
        }
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }

}
