import java.io.IOException;

import profiles.person.PersonWorker;

public class App {
    public static void main(String[] args) {
        PersonWorker person = new PersonWorker();

        try {
            person.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
}