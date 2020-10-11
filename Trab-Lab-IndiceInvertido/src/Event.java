import java.util.ArrayList;

public class Event {
    ArrayList<Integer> events;
    
    public Event(Integer file) {
        events = new ArrayList<Integer>();
        events.add(file);
    }

    public Event(ArrayList<Integer> events) {
        this.events = events;
    }

    public boolean verifyEvent(Integer file) {
        return this.events.contains(file);
    }

    public void saveEvent(Integer file) {
        events.add(file);
    }

    public ArrayList<Integer> getEvents() {
        return events;
    }

    public String returnEvents() {
        String temp =  "";

        for (Integer event : events) {
            temp += event + "/";
        }

        return temp;
    }
}