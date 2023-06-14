package org.reckenreb;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class JsonPatchResult {

    JsonNode result;

    List<JsonOperation> permittedOperations;

    public static JsonPatchResult of(JsonNode result, List<JsonOperation> permittedOperations) {
        JsonPatchResult r = new JsonPatchResult();
        r.result = result;
        r.permittedOperations = permittedOperations;
        return r;
    }

    public JsonNode getResult() {
        return result;
    }

    public List<JsonOperation> getPermittedOperations() {
        return permittedOperations;
    }

}
