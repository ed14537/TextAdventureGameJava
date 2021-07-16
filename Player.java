import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

class Player extends Characters {

    private ArrayList<Artifact> Inventory = new ArrayList<>();
    private Location location;
    private int health = 3;

    Player(String name, String description) {
        super(name, description);
    }

    //update player health
    void modHealth(boolean isUp) {
        if(!isUp) {
            health--;
        } else {
            health++;
        }
    }

    ArrayList<Artifact> returnInventory() {
        return Inventory;
    }
    void getInventory(BufferedWriter out) throws IOException {
        System.out.println("Your Inventory contains:");
        for(Artifact a : Inventory) {
            out.write(a.getName() + " -- " + a.getDescription() + "\n");
        }
    }

    void checkHealth(BufferedWriter out, Location loc) throws IOException {
        if(health == 0) {
            location.getArtifacts().addAll(Inventory);
            Inventory.removeAll(Inventory);
            location.getCharactersHere().remove(this);
            location = loc;
            health = 3;
            out.write("You died! You have been returned to where you started.\n");
        }
    }

    void addToInventory(Artifact artifact, BufferedWriter out) throws IOException {
        Inventory.add(artifact);
        location.getArtifacts().remove(artifact);
        out.write(artifact.getName() + " has been added to your inventory.\n");
    }

    void removeFromInventory(Artifact artifact, BufferedWriter out) throws IOException {
        location.getArtifacts().add(artifact);
        Inventory.remove(artifact);
        out.write(artifact.getName() + " has been removed from your inventory.\n");
    }

    void setLocation(Location l) {
        this.location = l;
    }
    Location getLocation() {
        return location;
    }


}
