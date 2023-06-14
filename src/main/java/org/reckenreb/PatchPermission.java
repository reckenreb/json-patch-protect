package org.reckenreb;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PatchPermission {

    private String path;

    private final Set<PatchOperationType> operations = new HashSet<>();

    public static PatchPermission ofPath(String path) {
        PatchPermission p = new PatchPermission();
        p.path = path;
        return p;
    }

    public PatchPermission ofOperation(PatchOperationType op) {
        this.operations.add(op);
        return this;
    }

    public PatchPermission ofOperations(PatchOperationType... ops) {
        this.operations.clear();
        this.operations.addAll(List.of(ops));
        return this;
    }

    public PatchPermission permitAll() {
        this.operations.clear();
        this.operations.addAll(List.of(PatchOperationType.values()));
        return this;
    }


    public String getPath() {
        return path;
    }

    public Set<PatchOperationType> getOperations() {
        return operations;
    }
}
