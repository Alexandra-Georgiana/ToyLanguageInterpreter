package model.Statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyILockTbl;
import model.ADTs.MyIStack;
import model.Exceptions.MyException;
import model.State.PrgState;
import model.Types.IType;
import model.Values.IValue;
import model.Values.IntValue;

public class Lock implements IStmt {
    private final String var;

    public Lock(String var) {
        this.var = var;
    }

    @Override
    public String toString() {
        return "lock(" + var + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stack = state.getExeStack();
        MyIDictionary<String, IValue> symTable = state.getSymTableTop();
        MyILockTbl lockTable = state.getLockTbl();

        if (!symTable.containsKey(var)) {
            throw new MyException("Variable " + var + " not found in the symbol table.");
        }

        IValue value = symTable.get(var);
        if (!(value instanceof IntValue)) {
            throw new MyException("Variable " + var + " is not of type IntValue.");
        }

        IntValue foundIndex = (IntValue) value;
        int index = foundIndex.getVal();

        synchronized (lockTable) {
            if (!lockTable.isDefined(index)) {
                throw new MyException("Index " + index + " not found in the lock table.");
            }

            if (lockTable.get(index) == -1) {
                lockTable.update(index, state.getId());
            } else {
                stack.push(this);
            }
        }

        return null;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }
}