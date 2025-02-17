package model.Statements;

import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.State.PrgState;
import model.Types.IType;

public class VariableDeclarationStatement  implements IStmt {
    private final String name;
    private final IType type;

    public VariableDeclarationStatement(String name, IType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "var " + name;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        state.getSymTableTop().put(name, type.defaultValue());
        return state;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        typeEnv.put(name, type);
        return typeEnv;
    }

}
