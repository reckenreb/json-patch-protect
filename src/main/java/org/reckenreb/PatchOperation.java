package org.reckenreb;

/**
 * Represents a single JsonPatch operation.
 *
 */
public class PatchOperation {

    private PatchOperationType op;

    private String path;

    private String value;

    public PatchOperationType getOp() {
        return op;
    }

    public String getPath() {
        return path;
    }

    public String getValue() {
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
