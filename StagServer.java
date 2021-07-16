import java.io.*;
import java.net.*;
import java.util.*;

class StagServer
{

    private ArrayList<Path> Paths = new ArrayList<>();
    private ArrayList<Location> Locations;
    private ArrayList<Action> actionsList = new ArrayList<>();
    private ArrayList<Player> Players = new ArrayList<>();

    public static void main(String args[])
    {
        if(args.length != 2) System.out.println("Usage: java StagServer <entity-file> <action-file>");
        else new StagServer(args[0], args[1], 8888);
    }

    public StagServer(String entityFilename, String actionFilename, int portNumber)
    {
        try {

            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            DotParser DotParser = new DotParser(entityFilename);
            Locations = DotParser.parseEntities(Paths);
            for(Path p: Paths) {
                for(Location l : Locations) {
                    if(p.getStart().equals(l.getName())) {
                        l.setPathList(p);
                    }
                }
            }
            ActionsParser AParser = new ActionsParser(actionFilename);
            AParser.parseActions(actionsList);

            while(true) acceptNextConnection(ss);
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private void acceptNextConnection(ServerSocket ss)
    {
        try {
            // Next line will block until a connection is received
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in, out);
            out.close();
            in.close();
            socket.close();
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    private String parseLine(String s, boolean flag) {

        String name;

        if(!flag) {
            name = s.substring(0, s.indexOf(':'));
        } else {
            name = s.substring(s.indexOf(':')+2);
        }

        return name;
    }

    private Player findPlayer(String name) {

        Player player = null;

        for(Player p : Players) {
            if (p.getName().equals(name) && p.getDescription().equals("Player")) {
                player = p;
            }
        }

        return player;

    }

    private boolean playerAlreadyExists(String name) {

        for(Location l : Locations) {
            for(Characters c : l.getCharactersHere()) {
                if(c.getName().equals(name) && c.getDescription().equals("Player")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkStandardCommands(String line, String name, BufferedWriter out) throws IOException {

        Player p = findPlayer(name);
        Location loc = p.getLocation();

        if(line.contains("inventory") || line.contains("inv")) {
            p.getInventory(out);
        }
        executeGet(p, line, out, loc);
        executeDrop(p, line, out);
        executeGoTo(p, line, out, loc);
        executeLook(line, out, loc);

    }

    private void executeGet(Player p, String line, BufferedWriter out, Location loc) throws IOException {

        if(line.contains("get")) {
            Iterator<Artifact> it = loc.getArtifacts().iterator();
            while(it.hasNext()) {
                Artifact artifact = it.next();
                if(line.contains(artifact.getName())) {
                    p.addToInventory(loc.getArtifact(artifact.getName()), out);
                    return;
                }
            }
            out.write("There is no artifact with that name at this location.\n");
        }
    }

    private void executeDrop(Player p, String line, BufferedWriter out) throws IOException {
        if(line.contains("drop")) {
            Iterator<Artifact> it = p.returnInventory().iterator();
            while(it.hasNext()) {
                Artifact artifact = it.next();
                if(line.contains(artifact.getName())) {
                    p.removeFromInventory(artifact, out);
                    return;
                }
            }
            out.write("You don't have that item in your inventory.\n");
        }
    }

    private void executeLook(String line, BufferedWriter out, Location loc) throws IOException {

        if(line.contains("look") || line.equals("look")) {
            loc.lookAround(out);
            out.write(loc.getPaths() + "\n");
        }
    }

    //execute our Go To command
    private void executeGoTo(Player p, String line, BufferedWriter out, Location loc) throws IOException {

        if(line.contains("goto")) {
            for(Location location : Locations) {
                if(line.contains(location.getName())) {
                    p.getLocation().getCharactersHere().remove(p);
                    p.setLocation(location);
                    location.addPlayer(p);
                    out.write("You are now in " + location.getName() + "\n");
                    return;
                }
            }
            out.write("That location doesn't exist.\n");
            out.write(loc.getPaths());
        }
    }

    private void processNextCommand(BufferedReader in, BufferedWriter out) throws IOException
    {
        Player currentPlayer = null;
        String line = in.readLine();
        String name = parseLine(line, false);
        String command = parseLine(line, true);

        //Create new player if they're not already in the game, else set current player to our parsed command from client
        if(!playerAlreadyExists(name)) {
            Player p = new Player(name, "Player");
            Locations.get(0).getCharactersHere().add(p);
            Players.add(p);
            p.setLocation(Locations.get(0));
            currentPlayer = p;
        } else {
            for(Player p : Players) {
                if(name.equals(p.getName())) {
                    currentPlayer = p;
                }
            }
        }

        System.out.println(command);
        checkStandardCommands(command.toLowerCase(), name, out);
        SpecialCommands sc = new SpecialCommands(currentPlayer);
        sc.checkSpecialCommands(actionsList, Locations, command, out);
        assert currentPlayer != null;
        currentPlayer.checkHealth(out, Locations.get(0));
    }
}
