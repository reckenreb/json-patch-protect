package org.reckenreb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JsonOperationType {
    ADD("add"),
    REMOVE("remove"),
    REPLACE("replace"),
    MOVE("move"),
    COPY("copy"),
    TEST("test");

    private String value;

    JsonOperationType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static JsonOperationType forValue(String value) {
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
