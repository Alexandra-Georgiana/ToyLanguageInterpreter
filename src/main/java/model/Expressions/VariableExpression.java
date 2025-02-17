package model.Expressions;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.Exceptions.MyException;
import model.Exceptions.VariableException;
import model.Types.IType;
import model.Values.IValue;

public class VariableExpression implements IExpression {
    private final String id;

    public VariableExpression(String id) {
        this.id = id;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> table, MyIHeap heap) throws VariableException {
        if (!table.containsKey(id))
            throw new VariableException("Variable " + id + " is not declared.");
        return table.get(id);
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv.get(id);
    }

}
