package model.Statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.Exceptions.AssignException;
import model.Exceptions.LogicException;
import model.Exceptions.MyException;
import model.Exceptions.VariableException;
import model.Expressions.IExpression;
import model.State.PrgState;
import model.Types.IType;
import model.Values.IValue;

public class AssignStmt implements IStmt {
    private final String id;
    private final IExpression exp;

    public AssignStmt(String id, IExpression exp) {
        this.id = id;
        this.exp = exp;
    }

    @Override
    public PrgState execute(PrgState state) throws AssignException, LogicException, VariableException {
        MyIDictionary<String, IValue> symTbl = state.getSymTable().peek();
        MyIHeap heap = state.getHeap();
        if (symTbl.containsKey(id)) {
            IValue val = exp.eval(symTbl, heap);
            IType typeId = symTbl.get(id).getType();
            if (val.getType().equals(typeId))
                symTbl.update(id, val);
            else
                throw new AssignException("Incompatible types for variable " + id);
        } else {
            throw new AssignException("Variable " + id + " is not declared.");
        }
        return state;
    }

    @Override
    public String toString() {
        return id + "=" + exp.toString();
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVar = typeEnv.get(id);
        IType typeExp = exp.typecheck(typeEnv);
        if (typeVar.equals(typeExp))
            return typeEnv;
        else
            throw new MyException("Assignment: right hand side and left hand side have different types: " + typeVar + " != " + typeExp + "in statement: " + this.toString());
    }
}
