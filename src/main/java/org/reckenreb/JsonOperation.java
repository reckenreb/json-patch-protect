package org.reckenreb;

/**
 * Represents a single JsonPatch operation.
 *
 */
public class JsonOperation {

    // TODO use enum and throw error in converter if not valid
    private String op;

    private String path;

    private String value;

    public String getOp() {
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
