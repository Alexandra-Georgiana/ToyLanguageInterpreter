package model.Statements;

import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.Expressions.IExpression;
import model.State.PrgState;
import model.Types.BoolType;
import model.Types.IType;
import model.Values.BoolValue;
import model.Values.IValue;


public class WhileStmt implements IStmt {
    IExpression condition;
    IStmt statement;

    public WhileStmt(IExpression condition, IStmt statement) {
        this.statement = statement;
        this.condition = condition;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue value = condition.eval(state.getSymTable().peek(), state.getHeap());
        if (!(value.getType() instanceof BoolType)){
            throw new MyException("Condition is not a boolean");
        }
        if (((BoolValue) value).getVal()){
            state.getExeStack().push(this);
            state.getExeStack().push(statement);
        }
        return state;
    }

    @Override
    public String toString() {
        return "while(" + condition.toString() + "){" + statement.toString() + "}";
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = condition.typecheck(typeEnv);
        if (typeExp.equals(new BoolType())) {
            statement.typecheck(typeEnv.clone());
            return typeEnv;
        } else {
            throw new MyException("While Statement: condition is not a boolean: " + typeExp + "in statement: " + this.toString());
        }
    }
}
