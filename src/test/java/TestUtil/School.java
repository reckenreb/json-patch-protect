package TestUtil;

public class School {
    public int id;

    public String name;

    public Integer pupils;

    public School(int id, String name, int pupils) {
        this.id = id;
        this.name = name;
        this.pupils = pupils;
    }

    public School() {

    }

    @Override
    public String toString() {
        return "School{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pupils=" + pupils +
                '}';
    }
}
