package org.reckenreb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.reckenreb.exception.JsonPatchPermissionException;
import org.reckenreb.util.JsonPatchUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Holds a list of JsonPatchUtil operations.
 *
 */
public class JsonPatch {
    private final List<PatchOperation> patchOperations;

    private final JsonPatchUtil util = new JsonPatchUtil();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @JsonCreator
    public JsonPatch(List<PatchOperation> patchOperations) {
        this.patchOperations = patchOperations;
    }

    public JsonNode applyTo(JsonNode node) {
        return util.apply(objectMapper.createArrayNode().addAll((ArrayNode) objectMapper.valueToTree(getPatchOperations())), node);
    }

    public JsonPatchResult applyTo(JsonNode node, PatchRules rules) throws JsonPatchPermissionException {
        List<PatchOperation> permittedOperations = new ArrayList<>();
        Iterator<PatchOperation> operationIterator = getPatchOperations().iterator();
        while (operationIterator.hasNext()) {
            PatchOperation operation = operationIterator.next();
            Optional<PatchPermission> permitted = rules.getPermissions().stream().filter(permission -> permission.getPath().equals(operation.getPath()) && permission.getOperations().contains(operation.getOp())).findAny();
            if (permitted.isEmpty()) {
                if (rules.isThrowException()) {
                    throw new JsonPatchPermissionException(operation.getOp().toValue(), operation.getPath());
                } else {
                    permittedOperations.add(operation);
                    operationIterator.remove();
                }
            }
        }
        return JsonPatchResult.of(util.apply(objectMapper.createArrayNode().addAll((ArrayNode) objectMapper.valueToTree(getPatchOperations())), node), permittedOperations);
    }

    public List<PatchOperation> getPatchOperations() {
        return patchOperations;
    }

    @Override
    public String toString() {
        String patchOperations = getPatchOperations().stream().map(PatchOperation::toString).collect(Collectors.joining(", "));
        return "JsonPatch [" +
                patchOperations +
                ']';
    }

}
