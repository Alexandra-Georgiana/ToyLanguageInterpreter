package model.Expressions;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.Exceptions.LogicException;
import model.Exceptions.MyException;
import model.Exceptions.VariableException;
import model.Types.BoolType;
import model.Types.IType;
import model.Types.IntType;
import model.Values.BoolValue;
import model.Values.IValue;
import model.Values.IntValue;

public class RationalExpression implements IExpression{
    private final IExpression e1;
    private final IExpression e2;
    private final int op;

    public RationalExpression(IExpression e1, IExpression e2, int op) {
        this.e1 = e1;
        this.e2 = e2;
        this.op = op;
    }

    @Override
    public IValue eval(MyIDictionary<String, IValue> table, MyIHeap heap) throws LogicException, VariableException {
        IValue v1, v2;
        v1 = e1.eval(table, heap);
        if (v1.getType().equals(new IntType())) {
            v2 = e2.eval(table, heap);
            if (v2.getType().equals(new IntType())) {
                IntValue i1 = (IntValue) v1;
                IntValue i2 = (IntValue) v2;

                int n1 = i1.getVal();
                int n2 = i2.getVal();

                return switch (op) {
                    case 1 -> new BoolValue(n1 < n2);
                    case 2 -> new BoolValue(n1 <= n2);
                    case 3 -> new BoolValue(n1 > n2);
                    case 4 -> new BoolValue(n1 >= n2);
                    case 5 -> new BoolValue(n1 == n2);
                    case 6 -> new BoolValue(n1 != n2);
                    
                    default -> throw new LogicException("Invalid operation code");
                };
            } else {
                throw new LogicException("Second operand is not an integer");
            }
        } else {
            throw new LogicException("First operand is not an integer");
        }
    }

    @Override
    public String toString() {
        return switch (op) {
            case 1 -> e1.toString() + "<" + e2.toString();
            case 2 -> e1.toString() + "<=" + e2.toString();
            case 3 -> e1.toString() + ">" + e2.toString();
            case 4 -> e1.toString() + ">=" + e2.toString();
            case 5 -> e1.toString() + "==" + e2.toString();
            case 6 -> e1.toString() + "!=" + e2.toString();
            default -> "Invalid operation code";
        };
    }

    @Override
    public IType typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType type1, type2;
        type1 = e1.typecheck(typeEnv);
        type2 = e2.typecheck(typeEnv);

        if (type1.equals(new IntType())) {
            if (type2.equals(new IntType())) {
                return new BoolType();
            } else {
                throw new MyException("Second operand is not an integer: " + e2.toString());
            }
        } else {
            throw new MyException("First operand is not an integer: " + e1.toString());
        }
    }

}
