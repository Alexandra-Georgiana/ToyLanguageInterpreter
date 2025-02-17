package model.Statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.ADTs.MyIProcTbl;
import model.Exceptions.MyException;
import model.Expressions.IExpression;
import model.State.PrgState;

import java.util.List;

public class CallProcStmt implements IStmt {
    private final String procName;
    private final List<IExpression> params;

    public CallProcStmt(String procName, List<IExpression> params){
        this.procName = procName;
        this.params = params;
    }

    @Override
    public String toString(){
        return "call " + procName + "(" + params.toString() + ")";
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        MyIDictionary<String, model.Values.IValue> symTable = state.getSymTable().peek();
        MyIHeap heap = state.getHeap();
        MyIProcTbl procTbl = state.getProcTbl();
        if(!procTbl.isDefined(procName))
            throw new RuntimeException("Procedure " + procName + " is not defined.");

        List<String> procParams = procTbl.get(procName).getKey();
        IStmt procBody = procTbl.get(procName).getValue();
        MyIDictionary<String, model.Values.IValue> newSymTable = symTable.clone();
        for(String param : procParams){
            int index = procParams.indexOf(param);
            newSymTable.update(param, params.get(index).eval(symTable, heap));
        }

        state.getSymTable().push(newSymTable);
        state.getExeStack().push(new ReturnStmt());
        state.getExeStack().push(procBody);
        return null;
    }

    @Override
    public MyIDictionary<String, model.Types.IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) {
        return typeEnv;
    }
}
