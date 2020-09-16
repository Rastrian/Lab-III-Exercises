import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CryptExercise {
    private static final String STR = "abcdefghijklmnopqrstuvwxyz";
    private static final String REVERSE = reverse(STR);
    private static final String DECRYPT_OUTPUT = "output-decrypt.txt";
    private static final String ENCRYPT_OUTPUT = "output-crypt.txt";

    public static void main(String[] args) {
        List<String> lines = new ArrayList<String>();
        readData(lines);
        createFile(ENCRYPT_OUTPUT);
        createFile(DECRYPT_OUTPUT);
        char[] charSTR, charREVER;
        charSTR = strToChar(STR);
        charREVER = strToChar(REVERSE);

        List<String> lines_encrypted = new ArrayList<String>();
        List<String> lines_decrypted = new ArrayList<String>();

        lines.forEach(l -> {
            String enc = encrypt(l.toLowerCase(), charSTR, charREVER);
            String dec = decrypt(enc, charSTR, charREVER);
            lines_encrypted.add(enc);
            lines_decrypted.add(dec);
        });

        loadData(lines_encrypted, ENCRYPT_OUTPUT);
        loadData(lines_decrypted, DECRYPT_OUTPUT);
    }

    private static void createFile(String fileName) {
        try {
            File file = new File(fileName);
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

    private static void readData(List<String> lines) {
        try {
            FileReader reader = new FileReader("input-crypt.txt");
            BufferedReader inputStream = new BufferedReader(reader);
            String linha = inputStream.readLine();
            while (!(linha == null)) {
                lines.add(linha);
                linha = inputStream.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.err.printf("Error: can't open file: %s.\n", e.getMessage());
        }
    }

    private static void loadData(List<String> lines, String fileName) {
        String message = "";
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i) != null){
                message = lines.get(i);
                System.out.println(message);
                writeData(message, fileName);
            }
        }
    }

    public static String encrypt(String word, char[] STRChar, char[] reverseChar){
        char[] splitWord = strToChar(word);
        for(int i = 0; i < splitWord.length; i++){
            for(int j = 0; j < reverseChar.length; j++){
                if(splitWord[i] == STRChar[j]){
                    splitWord[i] = reverseChar[j];
                    break;
                }
            }
        }
        String encryptedWord = new String(splitWord);
        return encryptedWord;
    }

    public static String decrypt(String word, char[] STRChar, char[] reverseChar){
        char[] splitWord = strToChar(word);

        for(int i = 0; i < splitWord.length; i++){
            for(int j = 0; j < reverseChar.length; j++){
                if(splitWord[i] == reverseChar[j]){
                    splitWord[i] = STRChar[j];
                    break;
                }
            }
        }
        String decryptedWord = new String(splitWord);
        return decryptedWord;
    }

    public static char[] strToChar(String word){
        char[] strChar = word.toCharArray();
        return strChar;
    }

    public static String reverse(String word) {
        if ((word == null) || (word.length() <= 1)) {
            return word;
        }
        return reverse(word.substring(1)) + word.charAt(0);
    }
}