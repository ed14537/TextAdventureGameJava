import java.util.ArrayList;

class Entity {

    private String Name;
    private String Description;

    Entity(String name, String description) {
        Name = name;
        Description = description;
    }

    String getName() {
        return Name;
    }

    String getDescription() {
        return Description;
    }

}
