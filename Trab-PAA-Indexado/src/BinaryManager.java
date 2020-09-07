import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.Scanner;

public class BinaryManager {

    public static Map<Integer, Long> init(Map<Integer, Long> tempStore) {
        String line; int count = 0; String[] split;
        try {
            RandomAccessFile binary = new RandomAccessFile((new File("./bin/binary.bin")), "rw");
            Scanner input = new Scanner(new File("PessoasPAA.txt"));

            binary.setLength(0);
            binary.writeInt(0);

            while (input.hasNextLine()){
                line = input.nextLine();   
                split = line.split(";");

                Person newPerson = new Person(split);
                long position = binary.getFilePointer();

                newPerson.saveToFile(binary);
                tempStore.put(newPerson.getId(), position);
    //            System.out.println(newPerson.getId() + " inserida na posição " + position);
                count++;
            }

            input.close();
            binary.seek(0);
            binary.writeInt(count);
            binary.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempStore;
    }

    public static Person getPerson(RandomAccessFile binary, int id){
        long searchEndTime, searchStartTime = System.currentTimeMillis();

        boolean process_running = true;

        try {
            binary.seek(Integer.BYTES);

            while (process_running) {

                int Pid = binary.readInt();
                String name = binary.readUTF();
                String data = binary.readUTF();

                searchEndTime = System.currentTimeMillis();

                if (id == Pid) {
                    System.out.println(String.format("Tempo de procura: %f segundos.",
                        (searchStartTime - searchEndTime) / 1000.0));

                    return new Person(Pid, name, data);
                }
            }
        } catch (IOException e) {
            process_running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(String.format("Tempo de procura: %f segundos.",
            (searchStartTime - System.currentTimeMillis()) / 1000.0));
        return null;
    }

    public static Person getPerson(RandomAccessFile binary, long pos){
        long searchEndTime, searchStartTime = System.currentTimeMillis();
        
        try {
            binary.seek(pos);

            int Pid = binary.readInt();
            String name = binary.readUTF();
            String data = binary.readUTF();

            Person person = new Person(Pid, name, data);

            searchEndTime = System.currentTimeMillis();

            if (person != null)
                System.out.println(String.format("Tempo de procura: %f segundos.",
                    (searchStartTime - searchEndTime) / 1000.0));

                return new Person(Pid, name, data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchEndTime = System.currentTimeMillis();

        System.out.println(String.format("Tempo de procura: %f segundos.",
            (searchStartTime - searchEndTime) / 1000.0));
        return null;
    }
}
