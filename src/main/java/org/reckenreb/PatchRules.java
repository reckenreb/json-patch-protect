package org.reckenreb;

import java.util.ArrayList;
import java.util.List;

public class PatchRules {

    private List<PatchPermission> permissions = new ArrayList<>();

    private boolean throwException = false;

    public PatchRules() {
    }

    public static PatchRules ofPermissions(List<PatchPermission> permissions) {
       PatchRules p = new PatchRules();
       p.permissions = permissions;
       return p;
    }

    public PatchRules throwException(boolean throwException) {
        this.throwException = throwException;
        return this;
    }

    public PatchRules throwException() {
        this.throwException = true;
        return this;
    }

    public List<PatchPermission> getPermissions() {
        return permissions;
    }

    boolean isThrowException() {
        return throwException;
    }

}
