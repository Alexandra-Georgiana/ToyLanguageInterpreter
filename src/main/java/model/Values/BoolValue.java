package model.Values;

import model.Types.BoolType;
import model.Types.IType;


public class BoolValue implements IValue {
    Boolean val;
    
    public BoolValue(boolean v) {
        this.val = v;
    }
    
    public boolean getVal() {
        return val;
    }
    
    @Override
    public IType getType() {
        return new BoolType();
    }
    
    @Override
    public String toString() {
        return Boolean.toString(val);
    }

    public boolean equals(BoolValue another) {
        return this.val == another.getVal();
    }

}

