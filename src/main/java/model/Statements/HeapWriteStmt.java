package model.Statements;

import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.Expressions.IExpression;
import model.State.PrgState;
import model.Types.IType;
import model.Types.RefType;
import model.Values.IValue;
import model.Values.RefValue;

public class HeapWriteStmt implements IStmt{
    IExpression addressExpr;
    IExpression valueExpr;

    public HeapWriteStmt(IExpression address, IExpression expression) {
        this.addressExpr = address;
        this.valueExpr = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException{
        IValue address = addressExpr.eval(state.getSymTable().peek(), state.getHeap());
        IValue value = valueExpr.eval(state.getSymTable().peek(), state.getHeap());
        if (!(address.getType() instanceof RefType))
            throw new MyException("Heap should be accessed using references");
        state.getHeap().write(((RefValue) address).getAddress(), value);
        return null;
        }


    @Override
    public String toString() {
        return "writeHeap(" + addressExpr.toString() + ", " + valueExpr.toString() + ")";
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeAddr = addressExpr.typecheck(typeEnv);
        IType typeVal = valueExpr.typecheck(typeEnv);
        if (typeAddr instanceof RefType) {
            if (((RefType) typeAddr).getInner().equals(typeVal)) {
                return typeEnv;
            } else {
                throw new MyException("HeapWrite Statement: right hand side and left hand side have different types: " + typeAddr + " != " + typeVal + "in statement: " + this.toString());
            }
        } else {
            throw new MyException("HeapWrite Statement: right hand side is not a reference: " + typeAddr + "in statement: " + this.toString());
        }
    }
}
