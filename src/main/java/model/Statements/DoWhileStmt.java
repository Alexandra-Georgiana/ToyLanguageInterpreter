package model.Statements;

import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.Expressions.NegateExp;
import model.State.PrgState;
import model.Types.*;
import model.Values.IValue;
import model.Expressions.IExpression;
import model.Statements.*;


public class DoWhileStmt implements IStmt{
    private IStmt statement;
    private final IExpression exp;

    public DoWhileStmt(IStmt statement, IExpression exp){
        this.statement = statement;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IExpression exp2 = new NegateExp(exp);
        IStmt newStmt = new CompStmt(statement, new WhileStmt(exp2, statement));
        state.getExeStack().push(newStmt);
        return null;
    }

    @Override
    public String toString(){
        return "repeat{" + statement.toString() + "}until(" + exp.toString() + ")";
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = exp.typecheck(typeEnv);
        if (!typeExp.equals(new BoolType()))
            throw new MyException("Do While Statement: Expression " + exp.toString() + " is not of type bool");
        statement.typecheck(typeEnv.clone());
        return typeEnv;
    }
}
