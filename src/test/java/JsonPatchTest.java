import TestUtil.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reckenreb.JsonOperationType;
import org.reckenreb.JsonPatch;
import org.reckenreb.PatchPermission;
import org.reckenreb.PatchRules;
import org.reckenreb.exception.JsonPatchPermissionException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class JsonPatchTest {

    private static final String JSON = "[{\"op\":\"replace\",\"path\":\"/firstName\",\"value\":\"patched\"}, " +
            "{\"op\":\"replace\",\"path\":\"/ssn\",\"value\":\"1234\"}]";


    @Test
    @DisplayName("Basic test")
    void basicTest() {


        Person p = new Person();
        p.id = 1L;
        p.firstName = "John";
        p.lastName = "Doe";
        p.ssn = "0000100292";

        ObjectMapper mapper = new ObjectMapper();
        JsonPatch patch;
        try {
            patch = mapper.readValue(JSON, JsonPatch.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode patched = patch.applyTo(mapper.valueToTree(p));
        Person pNew = mapper.convertValue(patched, Person.class);
        assertThat(pNew.firstName).isEqualTo("patched");
        assertThat(pNew.ssn).isEqualTo("1234");
        assertThat(pNew.lastName).isEqualTo("Doe");
        assertThat(pNew.id).isEqualTo(1L);

    }

    @Test
    @DisplayName("ssn path permitted")
    void ssnPermittedTest() {
        String json = JSON;

        Person p = new Person();
        p.id = 1L;
        p.firstName = "John";
        p.lastName = "Doe";
        p.ssn = "0000100292";

        ObjectMapper mapper = new ObjectMapper();
        JsonPatch patch;
        try {
            patch = mapper.readValue(JSON, JsonPatch.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


        PatchPermission permission1 = PatchPermission.ofPath("/firstName").ofOperation(JsonOperationType.REPLACE);
        List<PatchPermission> permissions = new ArrayList<>();
        permissions.add(permission1);
        permissions.add(PatchPermission.ofPath("/lastName").permitAll());



        JsonNode patched = null;
        try {
            patched = patch.applyTo(mapper.valueToTree(p), PatchRules.ofPermissions(permissions).throwException(false));
        } catch (JsonPatchPermissionException e) {
            e.printStackTrace();
        }
        Person pNew = mapper.convertValue(patched, Person.class);
        assertThat(pNew.firstName).isEqualTo("patched");
        assertThat(pNew.ssn).isEqualTo("0000100292");
        assertThat(pNew.lastName).isEqualTo("Doe");
        assertThat(pNew.id).isEqualTo(1L);

    }


}
