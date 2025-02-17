package model.Statements;

import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.State.PrgState;
import model.Types.IType;

public class NoOperationStatement implements IStmt {
    @Override
    public String toString() {
        return "NO Operation";
    }

    @Override
    public PrgState execute(PrgState state) {
        return state;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return typeEnv;
    }
}
