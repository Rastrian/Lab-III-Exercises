import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Index {
    private static Hash[] HashArray;
    private static RandomAccessFile HashFile;
    private static String TempPath;

    public Index(String temp) {
        Index.TempPath = temp;
        HashArray = new Hash[800077];
        fill();
    }

    private void fill() {
        for (int i = 0; i < HashArray.length; i++) {
            HashArray[i] = new Hash(-1, 0);
        }
    }

    public static void storePerson(int RG, long index) {
        int lastPos = hashIt(RG);
        HashArray[lastPos] = new Hash(RG, index);
    }

    public static void storeAllPersons() throws IOException {
        long indexHash;
        HashFile = new RandomAccessFile(new File(FilesPath.HASHFILE.getPath()), "rw");
        HashFile.setLength(0L);

        for (int i = 0; i < HashArray.length; i++) {
            indexHash = HashFile.getFilePointer();
            HashArray[i].saveToFile(HashFile, indexHash);
        }
        HashFile.close();
    }

    public static Person returnPerson(int RG) throws IOException {
        RandomAccessFile data = new RandomAccessFile(new File(TempPath), "r");
        HashFile = new RandomAccessFile(new File(FilesPath.HASHFILE.getPath()), "rw");
        int Separator = Separator(RG);
        int hash = (int) ((Math.pow(Separator, 2) % HashArray.length));
        boolean found = false;

        while (!found) {
            if (hash >= HashArray.length) return new Person(-1, "", "");
            found = HashArray[hash].getRG() == RG ? true : false;
            if (!found)
                hash++;
        }

        long DataIndex = returnDataIndex(HashArray[hash].getIndex());
        return (Person.readFromFile(data, DataIndex));
    }

    private static long returnDataIndex(long index) throws IOException {
        HashFile.seek(index);
        HashFile.readInt();
        return HashFile.readLong();
    }

    public static int hashIt(int RG) {
        boolean found = false;
        int Separator = Separator(RG);
        int hash = (int) ((Math.pow(Separator, 2) % HashArray.length));

        while (!found) {
            if (HashArray[hash].getRG() == -1) {
                found = true;
            } else {
                hash++;
            }
        }
        return hash;

    }

    private static int Separator(int RG) {
        String n = "";
        String number = String.valueOf(RG);
        for (int i = 2; i <= 5; i++) {
            n += String.valueOf(number.charAt(i));
        }
        return Integer.parseInt(n);
    }
}