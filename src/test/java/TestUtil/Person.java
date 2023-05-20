package TestUtil;

public class Person {

    public Long id;
    public String firstName;
    public String lastName;
    public String ssn;

    @Override
    public String toString() {
        return "Person {" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", ssn='" + ssn + '\'' +
                '}';
    }
}
