import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListExercise {

    private static void createFile() {
        try {
            File file = new File("output-list.txt");
            if (file.createNewFile()) {
                System.out.println("New file created: " + file.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeData(String data, String fileName) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.newLine();
            writer.append(data);
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void readData(List<Integer> numbers) {
        try {
            FileReader reader = new FileReader("input-list.txt");
            BufferedReader inputStream = new BufferedReader(reader);
            String linha = inputStream.readLine();
            while (!linha.equals("FIM")) {
                numbers.add(Integer.parseInt(linha));
                linha = inputStream.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.printf("Error: can't open file: %s.\n", e.getMessage());
        }
    }

    private static void loadData(List<Integer> numbers, List<Integer> counters) {
        String message;
        for (int i = 0; i < numbers.size(); i++) {
            if (numbers.get(i) != null){
                message = numbers.get(i) + " (" + counters.get(i) + "x)";
                System.out.println(message);
                writeData(message, "output-list.txt");
            }
        }
    }

    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<Integer>();
        List<Integer> counters = new ArrayList<Integer>();
        readData(numbers);
        createFile();
        Collections.sort(numbers);

        Integer aux; int counter;
        for (int i = 0; i < numbers.size(); i++) {
            counter = 0;
            aux = numbers.get(i);
            for (int j = 0; j < numbers.size(); j++) {
                if (aux == numbers.get(j)){
                    if (numbers.get(j) != null){
                        counter++;
                        if (counter > 1){
                            numbers.set(j, null);
                        }
                    }
                }
            }
            counters.add(counter);
        }

        loadData(numbers, counters);
    }
}
