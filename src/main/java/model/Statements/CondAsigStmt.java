package model.Statements;

import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.State.PrgState;
import model.Types.IType;
import model.Values.IValue;
import model.Statements.*;
import model.Expressions.*;

public class CondAsigStmt implements IStmt{
    private IExpression var;
    private final IExpression exp1;
    private final IExpression exp2;
    private final IExpression exp3;

    public CondAsigStmt(IExpression var, IExpression exp1, IExpression exp2, IExpression exp3){
        this.var = var;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IStmt newStmt = new IfStmt(exp1, new AssignStmt(var.toString(), exp2), new AssignStmt(var.toString(), exp3));
        state.getExeStack().push(newStmt);
        return null;
    }

    @Override
    public String toString(){
        return var.toString() + "=" +exp1.toString() + "?" + exp3.toString() + ":" + exp2.toString();
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = var.typecheck(typeEnv);
        IType typeExp1 = exp1.typecheck(typeEnv);
        IType typeExp2 = exp2.typecheck(typeEnv);
        IType typeExp3 = exp3.typecheck(typeEnv);
        if (!typeVar.equals(typeExp2))
            throw new MyException("Conditional Assignment Statement: Variable " + var + " is not of type " + typeExp2);
        if (!typeExp1.equals(new model.Types.BoolType()))
            throw new MyException("Conditional Assignment Statement: Expression 1 is not of type bool");
        if (!typeExp2.equals(typeExp3))
            throw new MyException("Conditional Assignment Statement: Expression 2 is not of type " + typeExp3);
        return typeEnv;
    }
}
