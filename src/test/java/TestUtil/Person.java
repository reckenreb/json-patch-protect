package TestUtil;

import java.util.ArrayList;
import java.util.List;

public class Person {

    public Long id;
    public String firstName;
    public String lastName;

    public List<Child> children = new ArrayList<>();
    public String ssn;

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", children=" + children +
                ", ssn='" + ssn + '\'' +
                '}';
    }
}
