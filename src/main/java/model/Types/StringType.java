package model.Types;

import model.Values.IValue;
import model.Values.StringValue;

public class StringType implements IType{
    @Override
    public boolean equals(Object another) {
        return another instanceof StringType;
    }
    
    @Override
    public String toString() {
        return "string";
    }

    @Override
    public IValue defaultValue() {
        return new StringValue("");
    }

}
