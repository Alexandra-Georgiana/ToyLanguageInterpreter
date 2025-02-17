package model.Expressions;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.Exceptions.LogicException;
import model.Exceptions.MyException;
import model.Exceptions.VariableException;
import model.Types.BoolType;
import model.Types.IType;
import model.Values.BoolValue;
import model.Values.IValue;

public class NegateExp implements  IExpression{
    IExpression exp;

    public NegateExp(IExpression exp){
        this.exp = exp;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> table, MyIHeap heap) throws LogicException, VariableException {
        IValue value = exp.eval(table, heap);
        if (value.getType().equals(new BoolType())){
            return new BoolValue(!((BoolValue)value).getVal());
        }
        else{
            throw new LogicException("Expression is not a boolean");
        }
    }

    @Override
    public String toString(){
        return "!" + exp.toString();
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType type = exp.typecheck(typeEnv);
        if (type.equals(new BoolType())){
            return new BoolType();
        }
        else{
            throw new MyException("Negate: expression is not a boolean");
        }
    }
}
