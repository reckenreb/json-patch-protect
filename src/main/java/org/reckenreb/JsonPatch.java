package org.reckenreb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.reckenreb.util.JsonPatchUtil;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Holds a list of JsonPatchUtil operations.
 *
 */
public class JsonPatch {
    private final List<JsonOperation> patchOperations;

    private final JsonPatchUtil util = new JsonPatchUtil();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @JsonCreator
    public JsonPatch(List<JsonOperation> patchOperations) {
        this.patchOperations = patchOperations;
    }

    public JsonNode applyTo(JsonNode node) {
        return util.apply(objectMapper.createArrayNode().addAll((ArrayNode) objectMapper.valueToTree(getPatchOperations())), node);
    }

    public JsonNode applyTo(JsonNode node, Collection<PatchPermission> permissions) {
        return util.apply(objectMapper.createArrayNode().addAll((ArrayNode) objectMapper.valueToTree(getPatchOperations())), node);
    }

    public List<JsonOperation> getPatchOperations() {
        return patchOperations;
    }

    @Override
    public String toString() {
        String patchOperations = getPatchOperations().stream().map(JsonOperation::toString).collect(Collectors.joining(", "));
        return "JsonPatch [" +
                patchOperations +
                ']';
    }

}
