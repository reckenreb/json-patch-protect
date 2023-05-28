package org.reckenreb;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.reckenreb.exception.JsonPatchPermissionException;
import org.reckenreb.util.JsonPatchUtil;

import javax.sound.midi.Patch;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
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

    public JsonNode applyTo(JsonNode node, PatchRules rules) throws JsonPatchPermissionException {
        Iterator<JsonOperation> operationIterator = getPatchOperations().iterator();
        while (operationIterator.hasNext()) {
            JsonOperation operation = operationIterator.next();
            Optional<PatchPermission> permitted = rules.getPermissions().stream().filter(permission -> permission.getPath().equals(operation.getPath()) && permission.getOperations().contains(operation.getOp())).findAny();
            if (permitted.isEmpty()) {
                if (rules.isThrowException()) {
                    throw new JsonPatchPermissionException(operation.getOp().toValue(), operation.getPath());
                } else {
                    System.out.println("Operation '" + operation.getOp().toValue() + "' not allowed for path '" + operation.getPath() + "'");
                    operationIterator.remove();
                }
            }
        }
        // TODO neben dem Ergebnis, auch das Resultat von den Permissions zurückgeben, damit man zwar die erlaubten Teile updaten kann, aber dann trotzdem weiß was nicht ging
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
