package org.reckenreb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PatchOperationType {
    ADD("add"),
    REMOVE("remove"),
    REPLACE("replace"),
    MOVE("move"),
    COPY("copy"),
    TEST("test");

    private final String value;

    PatchOperationType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static PatchOperationType forValue(String value) {
        if (value == null) {
            return null;
        }
        return valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return value;
    }
}
