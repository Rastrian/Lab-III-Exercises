import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Map.Entry;

public class Index {
    private TreeMap<String, Event> dictionary;
    private List<String> stopwords;
    private String[] fileList;
    private String fileName = "IndiceInvertido.bin";

    private static int eventAmount = 0;

    public Index() throws IOException {
        dictionary = new TreeMap<String, Event>();
        fileList = getFilesList();
        stopwords = getStopWords();

        if (new File(fileName).exists()) {
            startIndex();
        } else {
            initIndex();
        }
    }

    public int getAmount() {
        return eventAmount;
    }

    public static String cleanASCII(String word) {
        return Normalizer.normalize(word, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    public String[] getFilesList() {
        return (new File("files").list());
    }

    public List<String> getStopWords() throws IOException {
        String temp;
        stopwords = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader("stopwords.txt"))) {
            temp = br.readLine();
            while (temp != null) {
                stopwords.add(temp);
                temp = br.readLine();
            }
        }

        return stopwords;
    }

    private void startIndex() throws IOException {
        RandomAccessFile File = new RandomAccessFile(fileName, "rw");
        eventAmount = File.readInt();

        for (int i = 1; i <= eventAmount; i++) {
            String word = File.readUTF();
            ArrayList<Integer> events = new ArrayList<Integer>();
            int countEvents = File.readInt();
            for (int j = 1; j <= countEvents; j++) {
                events.add(File.readInt());
            }
            dictionary.put(word, new Event(events));
        }

        File.close();
    }

    private void saveFile() throws IOException {
        RandomAccessFile File = new RandomAccessFile(new File(fileName), "rw");
        File.writeInt(eventAmount);
        
        for (Entry<String, Event> E : dictionary.entrySet()) {
            File.writeUTF((String) E.getKey()); // word
            File.writeInt(dictionary.get(E.getKey()).getEvents().size()); // times used
            for (Integer i : dictionary.get(E.getKey()).getEvents()) {
                File.writeInt(i); // saving each time used
            }
        }
    }

    private void initIndex() throws IOException {
        String temp;
        StringTokenizer parser;

        for (int i = 0; i < fileList.length; i++) {
            try(BufferedReader br = new BufferedReader(new FileReader("files\\" + fileList[i]))) {
                String line = br.readLine();
                while (line != null) {
                    
                    parser = new StringTokenizer(line, "\" . ,() * / \\ = - + : ; —  |");
                    
                    while (parser.hasMoreTokens()) {
                        temp = cleanASCII(parser.nextToken().toLowerCase());
                        if (temp != " " && temp != "") {
                            if (!stopwords.contains(temp)) {
                                if (dictionary.containsKey(temp)) {
                                    if (!dictionary.get(temp).verifyEvent(i+1)) {
                                        dictionary.get(temp).saveEvent(i+1);
                                    } else {
                                        dictionary.put(temp, new Event(i+1));
                                        eventAmount++;
                                    }
                                }
                            }
                        } 
                    }

                    line = br.readLine();
                }
            }
        }
        saveFile();
    }
}