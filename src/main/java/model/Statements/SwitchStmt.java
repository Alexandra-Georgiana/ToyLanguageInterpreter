package model.Statements;

import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.Expressions.RationalExpression;
import model.State.PrgState;
import model.Types.*;
import model.Values.IValue;
import model.Expressions.IExpression;
import model.Statements.*;

public class SwitchStmt implements IStmt{
    private IExpression expt;
    private IExpression exp1;
    private IExpression exp2;
    private IStmt stmt1;
    private IStmt stmt2;
    private IStmt stmt3;

    public SwitchStmt(IExpression expt, IExpression exp1, IStmt stmt1, IExpression exp2, IStmt stmt2, IStmt stmt3){
        this.expt = expt;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.stmt1 = stmt1;
        this.stmt2 = stmt2;
        this.stmt3 = stmt3;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IStmt newStmt = new IfStmt(new RationalExpression(expt, exp1, 5), stmt1, new IfStmt(new RationalExpression(expt, exp2, 5), stmt2, stmt3));
        state.getExeStack().push(newStmt);
        return null;
    }

    @Override
    public String toString(){
        return "switch(" + expt.toString() + "){case (" + exp1.toString() + ") : " + stmt1.toString() + "\ncase (" + exp2.toString() + ") : " + stmt2.toString() + "\ndefault: " + stmt3.toString() + "}";
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = expt.typecheck(typeEnv);
        IType typeExp1 = exp1.typecheck(typeEnv);
        IType typeExp2 = exp2.typecheck(typeEnv);
        if (!typeExp.equals(typeExp1))
            throw new MyException("Switch Statement: Expression " + expt.toString() + " is not of the same type as " + exp1.toString());
        if (!typeExp.equals(typeExp2))
            throw new MyException("Switch Statement: Expression " + expt.toString() + " is not of the same type as " + exp2.toString());
        stmt1.typecheck(typeEnv.clone());
        stmt2.typecheck(typeEnv.clone());
        stmt3.typecheck(typeEnv.clone());
        return typeEnv;
    }
}
