package model.Statements;

import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.Expressions.IExpression;
import model.State.PrgState;
import model.Types.IType;
import model.Types.RefType;
import model.Values.IValue;
import model.Values.RefValue;

public class HeapReadStmt implements IStmt {
    IExpression expression;

    public HeapReadStmt(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue value = expression.eval(state.getSymTable().peek(), state.getHeap());
        if (value instanceof RefValue) {
            RefValue refValue = (RefValue) value;
            IValue heapValue = state.getHeap().read(refValue.getAddress());
            if (heapValue == null) {
                throw new MyException("Heap Reading: no value found at address " + refValue.getAddress());
            }
            // Use the heapValue as needed, for example, print it or store it in the state
        } else {
            throw new MyException("Heap Reading: expression is not a reference");
        }
        return null;
    }

    @Override
    public String toString() {
        return "readHeap(" + expression.toString() + ")";
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeExp = expression.typecheck(typeEnv);
        if (typeExp instanceof RefType) {
            return typeEnv;
        } else {
            throw new MyException("Heap Reading: expression is not a RefType: " + typeExp + " in statement: " + this.toString());
        }
    }
}