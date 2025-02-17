
package model.Statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.Exceptions.MyException;
import model.Expressions.IExpression;
import model.State.PrgState;
import model.Types.IType;
import model.Types.RefType;
import model.Values.IValue;
import model.Values.RefValue;

public class HeapAllocStmt implements IStmt {
    private final String name;
    private final IExpression expression;

    public HeapAllocStmt(String varName, IExpression expression) {
        this.name = varName;
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, IValue> symTable = state.getSymTable().peek();
        MyIHeap heap = state.getHeap();

        if (!symTable.containsKey(name))
            throw new MyException(String.format("Heap Allocation: Variable %s not declared", name));

        IValue varValue = symTable.get(name);
        if (!(varValue.getType() instanceof RefType))
            throw new MyException(String.format("Heap Allocation: Variable %s not of ReferenceType", name));

        IValue evaluated = expression.eval(symTable, heap);

        // If the evaluated value is a reference, directly allocate it
        if (evaluated instanceof RefValue) {
            // Allow chaining references in heap
            Integer newPosition = heap.allocate(evaluated);
            symTable.put(name, new RefValue(newPosition, evaluated.getType()));
        } else {
            // Standard behavior for non-reference values
            IType locationType = ((RefType) varValue.getType()).getInner();
            if (!locationType.equals(evaluated.getType()))
                throw new MyException(String.format("Heap Allocation: %s not of %s", evaluated.getType(), locationType));
            Integer newPosition = heap.allocate(evaluated);
            symTable.put(name, new RefValue(newPosition, locationType));
        }

        return null;
    }


    @Override
    public String toString() {
        return "new(" + name + ", " + expression.toString() + ")";
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        IType typeVariable = typeEnv.get(name);
        IType typeExpression = expression.typecheck(typeEnv);
        if (typeVariable == null) {
            throw new MyException("Heap Allocation: Variable " + name + " not declared");
        }
        if (typeVariable.equals(new RefType(typeExpression))) {
            return typeEnv;
        } else {
            throw new MyException("Heap Allocation: Type of variable " + name + " and type of expression do not match");
        }
    }
}