package model.Statements;

import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.State.PrgState;
import model.Types.IType;

public class ReturnStmt implements IStmt{
    @Override
    public String toString(){
        return "return";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        try{
            state.getSymTable().pop();
        }catch (Exception e){
            throw new MyException("Return Statement: " + e.getMessage());
        }
        return null;
    }

    @Override
    public MyIDictionary<String, IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) throws MyException {
        return typeEnv;
    }
}
