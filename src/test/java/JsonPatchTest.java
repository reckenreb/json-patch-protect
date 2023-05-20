import TestUtil.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reckenreb.JsonPatch;

import static org.assertj.core.api.Assertions.assertThat;


public class JsonPatchTest {




    @Test
    @DisplayName("Basic test")
    void basicTest() {
        String json = "[{\"op\":\"replace\",\"path\":\"/firstName\",\"value\":\"patched\"}, " +
                "{\"op\":\"replace\",\"path\":\"/ssn\",\"value\":\"1234\"}]";

        Person p = new Person();
        p.id = 1L;
        p.firstName = "John";
        p.lastName = "Doe";
        p.ssn = "0000100292";

        ObjectMapper mapper = new ObjectMapper();
        JsonPatch patch;
        try {
            patch = mapper.readValue(json, JsonPatch.class);
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
}
