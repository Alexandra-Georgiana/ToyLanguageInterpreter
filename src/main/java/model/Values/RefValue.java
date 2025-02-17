package model.Values;

import model.Types.IType;
import model.Types.RefType;

public class RefValue implements IValue{
    int address;
    IType innerType;

    public RefValue(int address, IType innerType) {
        this.address = address;
        this.innerType = innerType;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof RefValue) {
            return address == ((RefValue) other).getAddress() && innerType.equals(((RefValue) other).getInnerType());
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + address + ", " + innerType.toString() + ")";
    }

    public int getAddress() {
        return address;
    }

    public IType getInnerType() {
        return innerType;
    }
    
    @Override
    public IType getType() {
        return new RefType(innerType);
    }
}
