package model.Statements;

import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.Expressions.IExpression;
import model.State.PrgState;
import model.Types.IType;
import model.Values.BoolValue;
import model.Values.IValue;


public class IfStmt implements IStmt {
    IExpression exp;
    IStmt thenS;
    IStmt elseS;
    
    public IfStmt(IExpression exp, IStmt thenS, IStmt elseS) {
        this.exp = exp;
        this.thenS = thenS;
        this.elseS = elseS;
    }
    
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue cond = exp.eval(state.getSymTable().peek(), state.getHeap());
        if (cond instanceof BoolValue) {
            boolean boolCond = ((BoolValue) cond).getVal();
            state.getExeStack().push(boolCond ? thenS : elseS);
        } else {
            throw new MyException("Condition is not boolean");
        }
        return state;
    }
    
    @Override
    public String toString() {
        return "(IF(" + exp.toString() + ") THEN(" + thenS.toString() + ") ELSE(" + elseS.toString() + "))";
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = exp.typecheck(typeEnv);
        if (typeExp.equals(new model.Types.BoolType())) {
            thenS.typecheck(typeEnv.clone());
            elseS.typecheck(typeEnv.clone());
            return typeEnv;
        } else {
            throw new MyException("If Statement: condition is not a boolean: " + typeExp + "in statement: " + this.toString());
        }
    }
}

