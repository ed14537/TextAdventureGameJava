import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

class Location extends Entity {

    private ArrayList<Artifact> artifacts = new ArrayList<>();
    private ArrayList<Furniture> furnitureList = new ArrayList<>();
    private ArrayList<Path> PathList = new ArrayList<>();
    private ArrayList<Characters> CharactersHere = new ArrayList<>();

    Location(String name, String description) {
        super(name, description);

    }

    ArrayList<Artifact> getArtifacts() {
        return artifacts;
    }

    ArrayList<Furniture> getFurnitureList() {
        return furnitureList;
    }

    ArrayList<Characters> getCharactersHere() {
        return CharactersHere;
    }

    void setArtifacts(Artifact a) {
        artifacts.add(a);
    }

    void setFurnitureList(Furniture f) {
        furnitureList.add(f);
    }

    void setPathList(Path p) {
        PathList.add(p);
    }

    void setCharactersHere(Characters c) {
        CharactersHere.add(c);
    }

    String getPaths() {

        StringBuilder availablePaths = new StringBuilder("Your available paths are: ");

        if (PathList.size() == 0) {
            return "No available paths";
        } else {
            for (Path p : PathList) {
                availablePaths.append(p.getEnd());
                availablePaths.append(", ");
            }
        }

        return availablePaths.toString();
    }

    Artifact getArtifact(String name) {
        for(Artifact a : artifacts) {
            if(a.getName().equals(name)) {
                return a;
            }
        }
        return null;
    }

    void addPlayer(Player p) {
        CharactersHere.add(p);
    }


    void lookAround(BufferedWriter out) throws IOException {

        out.write("Artifacts:\n");
        for (Artifact a: artifacts) {
            out.write("--Name-- " + a.getName() + "\n");
            out.write("--Description-- " + a.getDescription() + "\n");
        }
        out.write("");

        out.write("Furniture:\n");
        for(Furniture f: furnitureList) {
            out.write("--Name-- " + f.getName() + "\n");
            out.write("--Description-- " + f.getDescription() + "\n");
        }
        out.write("\n");

        out.write("Characters:\n");
        for(Characters c: CharactersHere) {
            out.write("--Name-- " + c.getName() + "\n");
            out.write("--Description-- " + c.getDescription() + "\n");
        }
        out.write("\n");
    }

}
