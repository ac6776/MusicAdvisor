package advisor.entities;

public abstract class ApiObject {
    private String name;

    public ApiObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract String toString();
}
