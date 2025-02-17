package model.Expressions;


import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.Exceptions.LogicException;
import model.Exceptions.MyException;
import model.Exceptions.VariableException;
import model.Types.IType;
import model.Values.IValue;

public interface IExpression {
    IValue eval(MyIDictionary<String, IValue> table, MyIHeap heap) throws ArithmeticException, LogicException, VariableException;

    @Override
    String toString();

    IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException;
}
