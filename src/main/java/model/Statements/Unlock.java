package model.Statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyILockTbl;
import model.ADTs.MyIStack;
import model.Exceptions.MyException;
import model.State.PrgState;
import model.Types.IType;
import model.Values.IValue;
import model.Values.IntValue;

public class Unlock implements IStmt {
    private final String var;

    public Unlock(String var) {
        this.var = var;
    }

    @Override
    public String toString() {
        return "unlock(" + var + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stack = state.getExeStack();
        MyIDictionary<String, IValue> symTable = state.getSymTableTop();
        MyILockTbl lockTable = state.getLockTbl();

        if (!symTable.containsKey(var)) {
            throw new MyException("Variable " + var + " not found in the symbol table.");
        }

        IntValue foundIndex = (IntValue) symTable.get(var);
        int index = foundIndex.getVal();

        synchronized (lockTable) {
            if (!lockTable.isDefined(index)) {
                return null; // Do nothing if index is not in the lock table
            }

            if (lockTable.get(index) == state.getId()) {
                lockTable.update(index, -1);
            }
        }

        return null;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) throws MyException {
        return typeEnv;
    }
}
