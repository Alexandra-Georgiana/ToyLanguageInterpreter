package model.Types;

import model.Values.IValue;
import model.Values.IntValue;

public class IntType implements IType{
    @Override
    public boolean equals(Object another) {
        return another instanceof IntType;
    }
    
    @Override
    public String toString() {
        return "int";
    }

    @Override
    public IValue defaultValue() {
        return new IntValue(0);
    }
}
