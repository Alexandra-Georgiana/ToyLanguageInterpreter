package model.Statements;

import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.State.PrgState;
import model.Types.IType;

import java.io.IOException;

public interface IStmt {
    PrgState execute(PrgState state) throws MyException, IOException;

    @Override
    String toString();

    MyIDictionary  typecheck(MyIDictionary<String, IType> typeEnv) throws MyException;
}
