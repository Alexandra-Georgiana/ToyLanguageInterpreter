package model.Values;

import model.Types.IType;
import model.Types.StringType;

public class StringValue implements IValue {
    private final String value;

    public StringValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public IType getType() {
        return new StringType();
    }

    @Override
    public String toString() {
        return value;
    }

    public boolean equals(StringValue another) {
        return another.getValue().equals(value);
    }
}
