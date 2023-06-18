package TestUtil;

import java.time.Month;

public class Child {

    public String name;

    public Integer age;

    public School school;

    public Child(String name, Integer age, School school) {
        this.name = name;
        this.age = age;
        this.school = school;
    }

    public Child(String name, Integer age) {
        this(name, age, null);
    }

    public Child() {
    }

    @Override
    public String toString() {
        return "Child{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", school=" + school +
                '}';
    }
}
