package org.reckenreb;

/**
 * Represents a single JsonPatch operation.
 *
 */
public class PatchOperation {

    private PatchOperationType op;

    private String path;

    private Object value;

    public PatchOperationType getOp() {
        return op;
    }

    public String getPath() {
        return path;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "{" +
                "op='" + op + '\'' +
                ", path='" + path + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
