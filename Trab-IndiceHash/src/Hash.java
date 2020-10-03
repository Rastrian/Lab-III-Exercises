import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;

public class Hash {
    private int RG;
    private long index;

    public Hash(int RG, long index) {
        this.RG = RG;
        this.index = index;
    }

    public int getRG() {
        return this.RG;
    }

    public void setRG(int RG) {
        this.RG = RG;
    }

    public long getIndex() {
        return this.index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    public Hash RG(int RG) {
        this.RG = RG;
        return this;
    }

    public Hash index(long index) {
        this.index = index;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Hash)) {
            return false;
        }
        Hash hash = (Hash) o;
        return RG == hash.RG && index == hash.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(RG, index);
    }

    @Override
    public String toString() {
        return "{" +
            " RG='" + getRG() + "'" +
            ", index='" + getIndex() + "'" +
            "}";
    }

    public boolean saveToFile(RandomAccessFile file,long posHash) throws IOException {
        file.writeInt(this.RG);
        file.writeLong(this.index);
        this.index = posHash; 
        return true;
    }
    
}