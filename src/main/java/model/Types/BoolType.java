package model.Types;

import model.Values.BoolValue;
import model.Values.IValue;

public class BoolType implements IType{

    @Override
    public boolean equals(Object another) {
        return another instanceof BoolType;
    }
    
    @Override
    public String toString() {
        return "bool";
    }

    @Override
    public IValue defaultValue() {
        return new BoolValue(false);
    }
}
