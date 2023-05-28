package org.reckenreb.exception;

public class JsonPatchPermissionException extends Throwable {

    public JsonPatchPermissionException(String op, String path) {
        super("Operation '" + op + "' not allowed for path '" + path + "'");
    }
}
