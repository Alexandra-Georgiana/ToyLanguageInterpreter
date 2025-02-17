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

public class LogicExpression implements IExpression {
    private final IExpression left;
    private final IExpression right;
    private final String operator;

    public LogicExpression(IExpression left, IExpression right, String operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> table, MyIHeap heap) throws LogicException, VariableException {
        IValue leftValue = left.eval(table, heap);
        if (leftValue.getType().equals(new BoolType())) {
            IValue rightValue = right.eval(table, heap);
            if (rightValue.getType().equals(new BoolType())) {
                boolean leftBool = ((BoolValue) leftValue).getVal();
                boolean rightBool = ((BoolValue) rightValue).getVal();
                switch (operator) {
                    case "&&" -> {
                        return new BoolValue(leftBool && rightBool);
                    }
                    case "||" -> {
                        return new BoolValue(leftBool || rightBool);
                    }
                    default -> throw new LogicException("Invalid operator");
                }
            } else {
                throw new LogicException("Right expression is not a boolean");
            }
        } else {
            throw new LogicException("Left expression is not a boolean");
        }
    }

    @Override
    public String toString() {
        return "(" + left + " " + operator + " " + right + ")";
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType type1 = left.typecheck(typeEnv);
        IType type2 = right.typecheck(typeEnv);
        if (type1.equals(new BoolType())) {
            if (type2.equals(new BoolType())) {
                return new BoolType();
            } else {
                throw new LogicException("Second operand is not a boolean: " + right.toString());
            }
        } else {
            throw new LogicException("First operand is not a boolean: " + left.toString());
        }
    }
}
