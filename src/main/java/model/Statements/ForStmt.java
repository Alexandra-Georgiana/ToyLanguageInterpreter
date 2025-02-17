package model.Statements;

import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.State.PrgState;
import model.Types.*;
import model.Values.IValue;
import model.Expressions.IExpression;
import model.Statements.*;

public class ForStmt implements IStmt{
    private final String var;
    private final IExpression exp1;
    private final IExpression exp2;
    private final IExpression exp3;
    private final IStmt stmt;

    public ForStmt(String var, IExpression exp1, IExpression exp2, IExpression exp3, IStmt stmt){
        this.var = var;
        this.exp1 = exp1;
        this.exp2 = exp2;
        this.exp3 = exp3;
        this.stmt = stmt;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IStmt newStmt = new CompStmt(new VariableDeclarationStatement(var, new IntType()), new CompStmt(new AssignStmt(var, exp1), new WhileStmt(exp2, new CompStmt(stmt, new AssignStmt(var, exp3)))));
        state.getExeStack().push(newStmt);
        return null;
    }

    @Override
    public String toString(){
        return "for(" + var + "=" + exp1.toString() + ";" + exp2.toString() + ";" + exp3.toString() + "){" + stmt.toString() + "}";
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.get(var);
        IType typeExp1 = exp1.typecheck(typeEnv);
        IType typeExp2 = exp2.typecheck(typeEnv);
        IType typeExp3 = exp3.typecheck(typeEnv);
        if (!typeVar.equals(new IntType()))
            throw new MyException("For Statement: Variable " + var + " is not of type int");
        if (!typeExp1.equals(new IntType()))
            throw new MyException("For Statement: Expression 1 is not of type int");
        if (!typeExp2.equals(new BoolType()))
            throw new MyException("For Statement: Expression 2 is not of type bool");
        if (!typeExp3.equals(new IntType()))
            throw new MyException("For Statement: Expression 3 is not of type int");
        stmt.typecheck(typeEnv.clone());
        return typeEnv;
    }





}
