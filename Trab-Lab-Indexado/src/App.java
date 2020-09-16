import java.io.File;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        Map < Integer, Long > tempStore = new HashMap < Integer, Long > ();

        tempStore = BinaryManager.init(tempStore);
        RandomAccessFile binary = new RandomAccessFile(new File("./bin/binary.bin"), "rw");

        String input = "";
        
        System.out.println("Numero de Registros: " + binary.readInt());

        try {
            Scanner userInput = new Scanner(System.in);
            System.out.println("Insira o RG/ID de uma pessoa:");
            input = userInput.nextLine().trim().toLowerCase();
            Integer id_search = Integer.parseInt(input);
            userInput.close();

            Person person = BinaryManager.getPerson(binary, id_search);

            System.out.println(person != null ? person.toString() : "Pessoa não encontrada");

            person = BinaryManager.getPerson(binary, (tempStore.containsKey(id_search) ? tempStore.get(id_search) : null));

            System.out.println(person != null ? person.toString() : "Pessoa não encontrada");
        } catch (Exception e) {
            e.getStackTrace();
        }
        binary.close();
    }
}