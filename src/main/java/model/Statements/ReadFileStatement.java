package model.Statements;

import model.ADTs.MyIDictionary;
import model.Exceptions.MyException;
import model.Expressions.IExpression;
import model.State.PrgState;
import model.Types.StringType;
import model.Values.IValue;
import model.Values.IntValue;
import model.Values.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStatement implements IStmt{
    IExpression expression;
    String name;

    public ReadFileStatement(IExpression expression, String name) {
        this.expression = expression;
        this.name = name;
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
            String line = reader.readLine();
            IValue val;
            if (line == null) {
                val = new IntValue(0);
            } else {
                val = new IntValue(Integer.parseInt(line));
            }
            state.getSymTableTop().put(this.name, val);
        } catch (IOException e) {
            throw new MyException("Error reading from file");
        }
        return null;
    }

    @Override
    public String toString() {
        return "readFile(" + expression.toString() + ")";
    }

    @Override
    public MyIDictionary<String, model.Types.IType> typecheck(MyIDictionary<String, model.Types.IType> typeEnv) throws MyException {
        if (!expression.typecheck(typeEnv).equals(new StringType())) {
            throw new MyException("Filename is not a string: " + expression.toString());
        }
        return typeEnv;
    }
}