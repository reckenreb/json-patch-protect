import TestUtil.Child;
import TestUtil.Person;
import TestUtil.School;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reckenreb.*;
import org.reckenreb.exception.JsonPatchPermissionException;
import org.reckenreb.exception.TestValidationException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SuppressWarnings("all")
public class JsonPatchTest {

    private static final String JSON_PATCH_TEST_1 = "[{\"op\":\"replace\",\"path\":\"/firstName\",\"value\":\"patched\"}, " +
            "{\"op\":\"replace\",\"path\":\"/ssn\",\"value\":\"1234\"}]";


    @Test
    @DisplayName("Person - Basic test")
    void basicTest() {
        Person p = new Person();
        p.id = 1L;
        p.firstName = "John";
        p.lastName = "Doe";
        p.ssn = "0000100292";

        ObjectMapper mapper = new ObjectMapper();
        JsonPatch patch;
        try {
            patch = mapper.readValue(JSON_PATCH_TEST_1, JsonPatch.class);
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
    @DisplayName("Person - ssn path permitted")
    void ssnPermittedTest() {
        String json = JSON_PATCH_TEST_1;

        Person p = new Person();
        p.id = 1L;
        p.firstName = "John";
        p.lastName = "Doe";
        p.ssn = "0000100292";
        ObjectMapper mapper = new ObjectMapper();
        JsonPatch patch = generateJsonPatch(JSON_PATCH_TEST_1);

        PatchPermission permission1 = PatchPermission.ofPath("/firstName").ofOperation(PatchOperationType.REPLACE);
        List<PatchPermission> permissions = new ArrayList<>();
        permissions.add(permission1);
        permissions.add(PatchPermission.ofPath("/lastName").permitAll());

        JsonPatchResult result = null;
        try {
            result = patch.applyTo(mapper.valueToTree(p), PatchRules.ofPermissions(permissions).throwException(false));
        } catch (JsonPatchPermissionException e) {
            e.printStackTrace();
        }
        Person pNew = mapper.convertValue(result.getResult(), Person.class);
        assertThat(pNew.firstName).isEqualTo("patched");
        assertThat(pNew.ssn).isEqualTo("0000100292");
        assertThat(pNew.lastName).isEqualTo("Doe");
        assertThat(pNew.id).isEqualTo(1L);

        List<PatchOperation> permittedOperations = result.getPermittedOperations();
        assertThat(permittedOperations).hasSize(1);
        assertThat(permittedOperations.get(0).getOp()).isEqualTo(PatchOperationType.REPLACE);
        assertThat(permittedOperations.get(0).getPath()).isEqualTo("/ssn");
        assertThat(permittedOperations.get(0).getValue()).isEqualTo("1234");
    }

    @Test
    @DisplayName("Person - change properties of child and school")
    void personChildSchoolTest() {
        Person before = new Person();
        School school = new School(11, "School 1", 200);
        Child c1 = new Child("Child 1", 12, school);
        Child c2 = new Child("Child 2", 15, school);
        Child c3 = new Child("Child 3", 17, school);
        before.firstName = "John";
        before.lastName = "Doe";
        before.ssn = "0000100292";
        before.children.add(c1);
        before.children.add(c2);
        before.children.add(c3);

        System.out.println("before: " +  before);

        String jsonPatch = "[" +
                "{\"op\":\"replace\",\"path\":\"/children/0/age\",\"value\":13}, " +
                "{\"op\":\"replace\",\"path\":\"/children/0/school/name\",\"value\":\"School 2\"}," +
                "{\"op\":\"replace\",\"path\":\"/children/0/school/id\",\"value\":99}," +
                "{\"op\":\"remove\",\"path\":\"/children/0/school/pupils\"}," +
                "{\"op\":\"remove\",\"path\":\"/children/2\"}" +
                "]";

        JsonPatch patch = generateJsonPatch(jsonPatch);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode patched = patch.applyTo(mapper.valueToTree(before));
        Person after = mapper.convertValue(patched, Person.class);

        System.out.println("after: " +  after);
        assertThat(after.firstName).isEqualTo("John");
        assertThat(after.ssn).isEqualTo("0000100292");
        assertThat(after.lastName).isEqualTo("Doe");
        assertThat(after.children).hasSize(2);
        assertThat(after.children.get(0).name).isEqualTo("Child 1");
        assertThat(after.children.get(0).age).isEqualTo(13);
        assertThat(after.children.get(0).school.name).isEqualTo("School 2");
        assertThat(after.children.get(0).school.id).isEqualTo(99);
        assertThat(after.children.get(0).school.pupils).isNull();
        assertThat(after.children.get(1).name).isEqualTo("Child 2");
        assertThat(after.children.get(1).age).isEqualTo(15);
        assertThat(after.children.get(1).school.name).isEqualTo("School 1");
        assertThat(after.children.get(1).school.id).isEqualTo(11);
        assertThat(after.children.get(1).school.pupils).isEqualTo(200);


    }

    @Test
    @DisplayName("Person - add child")
    void addChildToPerson(){
        Person before = new Person();
        before.id = 1L;
        before.firstName = "John";
        before.lastName = "Doe";
        before.ssn = "0000100292";

        String jsonPatch = "[{\"op\":\"add\",\"path\":\"/children/-\",\"value\":{\"name\":\"Child 1\",\"age\":12}}]";

        System.out.println("before: " +  before);

        JsonPatch patch = generateJsonPatch(jsonPatch);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode patched = patch.applyTo(mapper.valueToTree(before));
        Person after = mapper.convertValue(patched, Person.class);

        System.out.println("after: " +  after);

        assertThat(after.firstName).isEqualTo("John");
        assertThat(after.ssn).isEqualTo("0000100292");
        assertThat(after.lastName).isEqualTo("Doe");
        assertThat(after.children).hasSize(1);
        assertThat(after.children.get(0).name).isEqualTo("Child 1");
        assertThat(after.children.get(0).age).isEqualTo(12);
        assertThat(after.children.get(0).school).isNull();
    }

    @Test
    @DisplayName("Person - json-patch 'test' operation fails")
    void jsonPatchTestOperationFails() {
        Person before = new Person();
        before.id = 1L;
        before.firstName = "John";
        before.lastName = "Doe";
        before.ssn = "0000100292";

        String jsonPatch = "[{\"op\":\"replace\", \"path\":\"/firstName\", \"value\":\"Mustermann\"}, " +
                "{\"op\":\"test\",\"path\":\"/firstName\",\"value\":\"Max\"}]";

        System.out.println("before: " +  before);

        JsonPatch patch = generateJsonPatch(jsonPatch);
        ObjectMapper mapper = new ObjectMapper();
        assertThrows(TestValidationException.class,() -> patch.applyTo(mapper.valueToTree(before)));
    }





    private JsonPatch generateJsonPatch(String jsonPatch) {
        ObjectMapper mapper = new ObjectMapper();
        JsonPatch patch;
        try {
            patch = mapper.readValue(jsonPatch, JsonPatch.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return patch;
    }


}
