package model.Types;

import model.Values.IValue;

public interface IType {

    @Override
    public boolean equals(Object another);

    @Override
    public String toString();

    public IValue defaultValue();
}
