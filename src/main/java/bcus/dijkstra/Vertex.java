package bcus.dijkstra;

/**
 * Created by ble on 4/15/15.
 */
public class Vertex {

    // immutable fields
    private final String name;
    private final String id;

    public Vertex(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    // override equals and hash code according to id, to support Set operation

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        if (id != null ? !id.equals(vertex.id) : vertex.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
