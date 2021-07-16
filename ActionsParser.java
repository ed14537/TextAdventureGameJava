import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class ActionsParser {

    private String JSON_file;

    ActionsParser(String input) {
        JSON_file = input;
    }

    void parseActions(ArrayList<Action> actionsList) {
         JSONParser parser = new JSONParser();

        try {
            FileReader reader = new FileReader(JSON_file);
            JSONObject ObjectList = (JSONObject) parser.parse(reader);
            JSONArray Actions = (JSONArray) ObjectList.get("actions");
            for(int i = 0; i < Actions.size(); i++) {
                Action currentAction = new Action();
                JSONObject jsonAction = (JSONObject) Actions.get(i);
                ArrayList<String> triggers = (ArrayList<String>) jsonAction.get("triggers");
                ArrayList<String> subjects = (ArrayList<String>) jsonAction.get("subjects");
                ArrayList<String> consumed = (ArrayList<String>) jsonAction.get("consumed");
                ArrayList<String> produced = (ArrayList<String>) jsonAction.get("produced");

                currentAction.setTriggers(triggers);
                currentAction.setSubjects(subjects);
                currentAction.setConsumed(consumed);
                currentAction.setProduced(produced);
                currentAction.setNarration((String) jsonAction.get("narration"));

                actionsList.add(currentAction);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

}
