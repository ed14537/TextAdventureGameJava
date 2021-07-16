import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

class DotParser {

    private String EntityParse;

    DotParser(String input) {
        EntityParse = input;
    }

    ArrayList<Location> parseEntities(ArrayList<Path> Paths) {
        ArrayList<Location> Locations = null;
        try {
            Parser parser = new Parser();
            FileReader reader = new FileReader(EntityParse);
            parser.parse(reader);
            ArrayList<Graph> graphs = parser.getGraphs();
            ArrayList<Graph> subGraphs = graphs.get(0).getSubgraphs();
            Locations = new ArrayList<>();
            for (Graph g : subGraphs) {
                ArrayList<Graph> subGraphs1 = g.getSubgraphs();
                parseSubgraph1(subGraphs1, Locations);
                ArrayList<Edge> edges = g.getEdges();
                for (Edge e : edges) {
                    Paths.add(new Path(e.getSource().getNode().getId().getId(), e.getTarget().getNode().getId().getId()));
                }
            }
        } catch (FileNotFoundException | ParseException fnfe) {
            System.out.println(fnfe);
        }
        return Locations;
    }

    private void parseSubgraph1(ArrayList<Graph> subGraphs1, ArrayList<Location> Locations) {
        for (Graph g1 : subGraphs1) {
            ArrayList<Node> nodesLoc = g1.getNodes(false);
            Node nLoc = nodesLoc.get(0);
            Location current = new Location(nLoc.getId().getId(), nLoc.getAttribute("description"));
            ArrayList<Graph> subGraphs2 = g1.getSubgraphs();
            parseSubgraph2(subGraphs2, Locations, current);
        }
    }

    private void parseSubgraph2(ArrayList<Graph> subGraphs2, ArrayList<Location> Locations, Location current) {

        for (Graph g2 : subGraphs2) {
            ArrayList<Node> nodesEnt = g2.getNodes(false);
            for (Node nEnt : nodesEnt) {
                if (g2.getId().getId().equals("artefacts")) {
                    current.setArtifacts(new Artifact(nEnt.getId().getId(), nEnt.getAttribute("description")));
                }
                if (g2.getId().getId().equals("furniture")) {
                    current.setFurnitureList(new Furniture(nEnt.getId().getId(), nEnt.getAttribute("description")));
                }
                if (g2.getId().getId().equals("characters")) {
                    current.setCharactersHere(new Characters(nEnt.getId().getId(), nEnt.getAttribute("description")));
                }
            }
        }
        Locations.add(current);
    }

}
