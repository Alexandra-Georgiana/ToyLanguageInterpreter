package model.Statements;

import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.Expressions.IExpression;
import model.State.PrgState;
import model.Types.StringType;
import model.Values.IValue;
import model.Values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseFIleStatement implements IStmt {
    
    IExpression expression;

    public CloseFIleStatement(IExpression expression) {
        this.expression = expression;
    }

    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue value = this.expression.eval(state.getSymTable().peek(), state.getHeap());
        if (!(value.getType() instanceof StringType)) {
            throw new MyException("Filename did not evaluate to string");
        }

        MyIDictionary<String, BufferedReader> files = state.getFiles();

        if (!files.containsKey(((StringValue) value).getValue())) {
            throw new MyException("File not opened");
        }

        BufferedReader reader = files.get(((StringValue) value).getValue());
        try {
            reader.close();
            files.remove(((StringValue) value).getValue());
        } catch (IOException e) {
            throw new MyException("Error closing file");
        }

        return null;
    }

    @Override
    public String toString() {
        return "closeFile(" + expression.toString() + ")";
    }

    @Override
    public MyIDictionary<String, model.Types.IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) throws MyException {
        if (!expression.typecheck(typeEnv).equals(new StringType())) {
            throw new MyException("Filename is not a string: " + expression.toString());
        }
        return typeEnv;
    }
}