package model.Statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyILockTbl;
import model.ADTs.MyIStack;
import model.Exceptions.MyException;
import model.State.PrgState;
import model.Types.IType;
import model.Types.IntType;
import model.Values.IValue;
import model.Values.IntValue;

public class NewLock implements IStmt {
    private final String var;

    public NewLock(String var) {
        this.var = var;
    }

    @Override
    public String toString() {
        return "newLock(" + var + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stack = state.getExeStack();
        MyIDictionary<String, IValue> symTable = state.getSymTable().peek();
        MyILockTbl lockTable = state.getLockTbl();

        int newLockAddress = lockTable.getFreeLocation();
        lockTable.update(newLockAddress, -1);

        if (symTable.containsKey(var)) {
            IValue value = symTable.get(var);
            if (value.getType().equals(new IntType())) {
                symTable.update(var, new IntValue(newLockAddress));
            } else {
                throw new MyException("Variable " + var + " is not of type IntType.");
            }
        } else {
            symTable.put(var, new IntValue(newLockAddress));
        }

        return null;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        if (!typeEnv.containsKey(var)) {
            throw new MyException("Variable " + var + " not defined in type environment.");
        }
        if (!typeEnv.get(var).equals(new IntType())) {
            throw new MyException("Variable " + var + " is not of type IntType.");
        }
        return typeEnv;
    }
}