import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

class SpecialCommands {

    private Player currentPlayer;

    SpecialCommands(Player p) {
        currentPlayer = p;
    }

    void checkSpecialCommands(ArrayList<Action> actions, ArrayList<Location> locations, String command, BufferedWriter out)
    throws IOException {

        for (Action a : actions) {
            for (String s : a.getTriggers()) {
                if(command.contains(s)) {
                    isActionPossible(locations, a, command, out);
                }
            }
        }

    }

    private void isActionPossible(ArrayList<Location> locations, Action a, String command, BufferedWriter out)
    throws IOException {

        if(checkSubjects(a, locations, command)) {
            out.write(a.getNarration() + "\n");
        } else {
            out.write("You aren't able to do that.\n");
        }
    }

    private boolean checkSubjects(Action action, ArrayList<Location> locations, String command) {

        int requisiteSubjects = 0;

        for(String s : action.getSubjects()) {
            if(command.contains(s)) {
                requisiteSubjects++;
            }
        }

        if(requisiteSubjects == action.getSubjects().size()) {
            if(checkForEntities(command) == action.getSubjects().size()) {
                checkConsumed(action);
                checkProduced(action, locations);
                return true;
            }
        }
        return false;
    }

    private int checkForEntities(String command) {

        int subjectsPresent = 0;

        Location loc = currentPlayer.getLocation();

        for(Artifact a : currentPlayer.returnInventory()) {
            if(command.contains(a.getName())) {
                subjectsPresent++;
            }
        }

        for(Furniture f : loc.getFurnitureList()) {
            if(command.contains(f.getName())) {
                subjectsPresent++;
            }
        }

        for(Characters c : loc.getCharactersHere()) {
            if(command.contains((c.getName()))) {
                subjectsPresent++;
            }
        }

        return subjectsPresent;

    }

    private void checkConsumed(Action action) {

        if(action.getConsumed().size() == 0) {
            return;
        }

        for (String s : action.getConsumed()) {
            if(s.equals("health")) {
                currentPlayer.modHealth(false);
            }
            for (Artifact a : currentPlayer.returnInventory()) {
                if (s.equals(a.getName())) {
                    currentPlayer.returnInventory().remove(a);
                    break;
                }
            }
            for (Artifact a : currentPlayer.getLocation().getArtifacts()) {
                if (s.equals(a.getName())) {
                    currentPlayer.getLocation().getArtifacts().remove(a);
                    break;
                }
            }
            for (Furniture f : currentPlayer.getLocation().getFurnitureList()) {
                if (s.equals(f.getName())) {
                    currentPlayer.getLocation().getFurnitureList().remove(f);
                    break;
                }
            }
        }
    }

    private void checkProduced(Action action, ArrayList<Location> locations) {

        if(action.getProduced().size() == 0) {
            return;
        }

        for(String s : action.getProduced()) {
            if(s.equals("health")) {
                currentPlayer.modHealth(true);
            }

            for(Location l : locations) {
                if(s.equals(l.getName())) {
                    Path p = new Path(currentPlayer.getLocation().getName(), s);
                    currentPlayer.getLocation().setPathList(p);
                }
                if(l.getName().equals("unplaced")) {
                    searchUnplaced(l, s);
                }
            }
        }
    }

    private void searchUnplaced(Location l, String s) {

        for(Artifact a : l.getArtifacts()) {
            if(s.equals(a.getName())) {
                currentPlayer.getLocation().setArtifacts(a);
                l.getArtifacts().remove(a);
                return;
            }
        }
        for(Furniture f : l.getFurnitureList()) {
            if(s.equals(f.getName())) {
                currentPlayer.getLocation().setFurnitureList(f);
                l.getFurnitureList().remove(f);
                return;
            }
        }
        for(Characters c : l.getCharactersHere()) {
            if(s.equals(c.getName())) {
                currentPlayer.getLocation().setCharactersHere(c);
                l.getCharactersHere().remove(c);
                return;
            }
        }
    }

}
