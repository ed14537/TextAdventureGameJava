import java.util.ArrayList;

class Action {

    private ArrayList<String> triggers = new ArrayList<>();
    private ArrayList<String> subjects = new ArrayList<>();
    private ArrayList<String> consumed = new ArrayList<>();
    private ArrayList<String> produced = new ArrayList<>();
    private String narration;

    ArrayList<String> getTriggers() {
        return triggers;
    }

    ArrayList<String> getSubjects() {
        return subjects;
    }

    ArrayList<String> getConsumed() {
        return consumed;
    }

    ArrayList<String> getProduced() {
        return produced;
    }

    String getNarration() {
        return narration;
    }

    void setTriggers(ArrayList<String> t) {
        triggers.addAll(t);
    }

    void setSubjects(ArrayList<String> s) {
        subjects.addAll(s);
    }

    void setConsumed(ArrayList<String> c) {
        consumed.addAll(c);
    }

    void setProduced(ArrayList<String> p) {
        produced.addAll(p);
    }

    void setNarration(String n) {
        narration = n;
    }


}
