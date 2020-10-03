import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Objects;

public class Person implements Comparable {
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

    public static Person clean() {
        return new Person(-1, "", "");
    }

    public void saveToFile(RandomAccessFile file) throws IOException {
        file.seek(file.length());
        file.writeInt(getId());
        file.writeUTF(getName());
        file.writeUTF(getData());
    }

    public static Person readFromFile(RandomAccessFile data) throws IOException {
        Person aux = Person.clean();
        int id = data.readInt();
        String name = data.readUTF();
        String Data = data.readUTF();
        aux = new Person(id, name, Data);
        return aux;
    }

    public static Person readFromFile(RandomAccessFile data, long index) throws IOException {
        Person aux = Person.clean();
        data.seek(index);
        int id = data.readInt();
        String name = data.readUTF();
        String Data = data.readUTF();
        aux = new Person(id, name, Data);
        return aux;
    }

    public boolean checkSeparator() {
        return (this.id == -1 ? true : false);
    }

    @Override
    public int compareTo(Object o) {
        Person other = (Person) o;
        if (this.id != -1 && other.id == -1) return -1;
        else if (this.id == -1 && other.id != -1) return 1;
        if (this.Name.compareTo(other.Name) < 0) return -1;
        else if (this.Name.compareTo(other.Name) > 0) return 1;
        return 0;
    }
}