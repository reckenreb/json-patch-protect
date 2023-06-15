package org.reckenreb;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class JsonPatchResult {

    JsonNode result;

    List<PatchOperation> permittedOperations;

    public static JsonPatchResult of(JsonNode result, List<PatchOperation> permittedOperations) {
        JsonPatchResult r = new JsonPatchResult();
        r.result = result;
        r.permittedOperations = permittedOperations;
        return r;
    }

    public JsonNode getResult() {
        return result;
    }

    public List<PatchOperation> getPermittedOperations() {
        return permittedOperations;
    }

}
