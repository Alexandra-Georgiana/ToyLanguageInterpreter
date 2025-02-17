package model.Expressions;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.Exceptions.MyException;
import model.Types.IType;
import model.Values.IValue;

public class ValueExpression implements IExpression {
    private final IValue value;

    public ValueExpression(IValue value) {
        this.value = value;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> table, MyIHeap heap) {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return value.getType();
    }
}
