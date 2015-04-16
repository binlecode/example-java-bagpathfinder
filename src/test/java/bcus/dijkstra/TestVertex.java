package bcus.dijkstra;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by ble on 4/16/15.
 */
public class TestVertex {

    @Test
    public void testConstructor() {

        Vertex vertex = new Vertex("0", "Concourse_A_Ticketing");
        Assert.assertEquals(vertex.getId(), "0");
        Assert.assertEquals(vertex.getName(), "Concourse_A_Ticketing");
        Assert.assertEquals(vertex.hashCode(), new Vertex("0", "Concourse_A_Ticketing").hashCode());


    }

}
