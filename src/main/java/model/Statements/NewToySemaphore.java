package model.Statements;

import model.ADTs.MyIDictionary;
import model.ADTs.MyIHeap;
import model.ADTs.MyToySemaphoreTbl;
import model.ADTs.Tuple;
import model.Exceptions.MyException;
import model.Expressions.IExpression;
import model.State.PrgState;
import model.Types.IntType;
import model.Values.IValue;
import model.Values.IntValue;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;

public class NewToySemaphore implements IStmt{
    private final String var;
    private final IExpression firstExp;
    private final IExpression secondExp;
    private static final Lock lock = new ReentrantLock();

    public NewToySemaphore(String var, IExpression firstExp, IExpression secondExp){
        this.var = var;
        this.firstExp = firstExp;
        this.secondExp = secondExp;
    }

    @Override
    public String toString(){
        return "newToySemaphore(" + var + ", " + firstExp.toString() + ", " + secondExp.toString() + ")";
    }
    @Override
    public PrgState execute(PrgState state) throws MyException {
        lock.lock();
        try {
            MyIDictionary<String, IValue> symTable = state.getSymTable().peek();
            MyIHeap heap = state.getHeap();
            MyToySemaphoreTbl semaphoreTable = state.getToySemaphoreTbl();

            if (!symTable.containsKey(var)) {
                throw new MyException("Variable " + var + " is not defined");
            }

            if (!symTable.get(var).getType().equals(new IntType())) {
                throw new MyException("Variable " + var + " is not of type int");
            }

            IValue val1 = firstExp.eval(symTable, heap);
            IValue val2 = secondExp.eval(symTable, heap);

            if (!(val1 instanceof IntValue) || !(val2 instanceof IntValue)) {
                throw new MyException("Expressions must evaluate to integers");
            }

            int number1 = ((IntValue) val1).getVal();
            int number2 = ((IntValue) val2).getVal();

            int newFreeLocation = semaphoreTable.getFreeLocation();
            semaphoreTable.update(newFreeLocation, new Tuple<>(number1, new ArrayList<>(), number2));

            symTable.put(var, new IntValue(newFreeLocation));
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public MyIDictionary<String, model.Types.IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) throws MyException {
        model.Types.IType typeVar = typeEnv.get(var);
        model.Types.IType typeFirstExp = firstExp.typecheck(typeEnv);
        model.Types.IType typeSecondExp = secondExp.typecheck(typeEnv);
        if(typeVar.equals(new IntType())){
            if(typeFirstExp.equals(new IntType())){
                if(typeSecondExp.equals(new IntType())){
                    return typeEnv;
                }
                else{
                    throw new MyException("Second expression is not an integer");
                }
            }
            else{
                throw new MyException("First expression is not an integer");
            }
        }
        else{
            throw new MyException("Variable is not an integer");
        }
    }

}
