package model.Statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIStack;
import model.Exceptions.MyException;
import model.State.PrgState;
import model.Types.IType;

public class CompStmt implements IStmt{
    IStmt first;
    IStmt snd;
    
    public CompStmt(IStmt first, IStmt snd) {
        this.first = first;
        this.snd = snd;
    }
    
    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIStack<IStmt> stack = state.getExeStack();
        stack.push(snd);
        stack.push(first);
        return state;
    }
    
    @Override
    public String toString() {
        return first.toString() + ";\n" + snd.toString();
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, IType> typeEnv) throws MyException {
        return snd.typecheck(first.typecheck(typeEnv));
    }
}

