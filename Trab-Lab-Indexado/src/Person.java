import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;

public class Person {
    private int id;
    private String Name;
    private String data;
    
    public Person(String[] value) {
        this.id = Integer.parseInt(value[0]);
        this.Name = value[1];
        this.data = value[2];
    }

    public Person(Integer id, String Name, String data) {
        this.id = id;
        this.Name = Name;
        this.data = data;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Person id(Integer id) {
        this.id = id;
        return this;
    }

    public Person Name(String Name) {
        this.Name = Name;
        return this;
    }

    public Person data(String data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Person)) {
            return false;
        }
        Person person = (Person) o;
        return id == person.id && Objects.equals(Name, person.Name) && Objects.equals(data, person.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, Name, data);
    }

    @Override
    public String toString() {
        return "{" +
            " RG ='" + getId() + "'" +
            ", Nome ='" + getName() + "'" +
            ", Aniversario ='" + getData() + "'" +
            "}";
    }

    public void saveToFile(RandomAccessFile file) throws IOException {
        file.seek(file.length());
        file.writeInt(getId());
        file.writeUTF(getName());
        file.writeUTF(getData());
    }
}