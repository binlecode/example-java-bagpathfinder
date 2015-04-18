package bcus.bag;

import bcus.dijkstra.Vertex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by ble on 2015/4/17.
 */
public class TestBagShortestPathCache {

    private BagShortestPathCache cache = new BagShortestPathCache();
    private String id1 = "source1,destination1";
    private BagShortestPath path1 = new BagShortestPath(id1, new ArrayList<Vertex>(), 128);



    @Before
    public void beforeEach() {
    }

    @Test
    public void testConstainsCheck() {
        Assert.assertFalse(cache.containsPathForId("BAD,ID"));
    }

    @Test
    public void testPutPath() {
        Assert.assertFalse(cache.containsPathForId(id1));
        Assert.assertNull(cache.getPathById(id1));

        cache.putPath(path1);
        Assert.assertEquals(1, cache.size());
        Assert.assertTrue(cache.containsPathForId(id1));
        Assert.assertTrue(cache.containsPath(path1));

        cache.putPath(path1);   // should have no duplicates
        Assert.assertEquals(1, cache.size());
    }

    @Test
    public void tetGetPathbyId() {
        cache.putPath(path1);
        Assert.assertNotNull(cache.getPathById(id1));
        Assert.assertEquals(id1, cache.getPathById(id1).getId());
    }

}
