package org.reckenreb;

import java.util.ArrayList;
import java.util.List;

public class PatchPermission {

    private String path;

    private final List<JsonOperationType> operations = new ArrayList<>();

    public static PatchPermission ofPath(String path) {
        PatchPermission p = new PatchPermission();
        p.path = path;
        return p;
    }

    public PatchPermission ofOperation(JsonOperationType op) {
        this.operations.clear();
        this.operations.add(op);
        return this;
    }

    public PatchPermission ofOperations(JsonOperationType... ops) {
        this.operations.clear();
        this.operations.addAll(List.of(ops));
        return this;
    }

    public PatchPermission permitAll() {
        this.operations.clear();
        this.operations.addAll(List.of(JsonOperationType.values()));
        return this;
    }


    public String getPath() {
        return path;
    }

    public List<JsonOperationType> getOperations() {
        return operations;
    }
}
