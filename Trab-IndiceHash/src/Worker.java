import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class Worker implements Runnable {
    public static void createBinaryFile(String filePath, String binaryPath) throws IOException {
        File readFile = new File(filePath);
        Scanner reader = new Scanner(readFile);

        File outputBinaryFile = new File(binaryPath);
        RandomAccessFile outputData = new RandomAccessFile(outputBinaryFile, "rw");
        new Index(binaryPath);

        outputData.setLength(0L);
        outputData.writeInt(0);
        
        System.out.println("O arquivo binario e seu indice estão sendo criados, por favor aguarde...");

        int counter = 0;
        while (reader.hasNextLine()) {
            String nextLine = reader.nextLine();
            String []data = nextLine.split(";");
            long index = outputData.getFilePointer();
            Index.storePerson(Integer.parseInt(data[0]), index);
            Person aux = createPerson(data, index);
            aux.saveToFile(outputData);
            counter++;
        }
        reader.close();

        outputData.seek(0);
        outputData.writeInt(counter);
        outputData.close();
        Index.storeAllPersons();
        searchPersons();
    }

    private static Person createPerson(String[] data, long index) {
        Person aux;
        try {
            int rg = Integer.parseInt(data[0]);
            String name = data[1];
            String birthday = data[2];
            aux = new Person(rg, name, birthday);
        } catch (Exception e) {
            System.out.println("Erro na posição " + index);
            aux = null;
        }
        return aux;
    }

    private static void searchPersons() throws IOException {
        Scanner userInput = new Scanner(System.in);
        System.out.println("Insira o RG de uma pessoa:");
        Integer id_search = Integer.parseInt(userInput.nextLine().trim().toLowerCase());

        Person person = Index.returnPerson(id_search);

        System.out.println(person != null ? person.toString() : "Pessoa não encontrada");
        userInput.close();

        searchPersons();
    }

    @Override
    public void run() {
        try {
            createBinaryFile(FilesPath.INPUTFILE.getPath(), FilesPath.BINFILE.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}