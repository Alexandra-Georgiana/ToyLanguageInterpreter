package model.Expressions;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.Exceptions.LogicException;
import model.Exceptions.MyException;
import model.Exceptions.VariableException;
import model.Types.IType;
import model.Types.RefType;
import model.Values.IValue;
import model.Values.RefValue;


public class ReadHeapExpression implements IExpression {
    private final IExpression expr;

    public ReadHeapExpression(IExpression expr) {
        this.expr = expr;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> table, MyIHeap heap) throws ArithmeticException, LogicException, VariableException {
        IValue value = expr.eval(table, heap);
        if (!(value instanceof RefValue))
            throw new VariableException("Expression is not a reference");
        return heap.getHeap().get(((RefValue) value).getAddress());
        }

    @Override
    public String toString() {
        return "rH(" + expr + ")";
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType type = expr.typecheck(typeEnv);
        if (!(type instanceof RefType))
            throw new VariableException("Expression is not a reference: " + expr);
        return ((RefType) type).getInner();
    }
}
