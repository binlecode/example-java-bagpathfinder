package bcus.bag;

import bcus.dijkstra.Vertex;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by ble on 2015/4/17.
 */
public class TestBagPathCache {

    private BagPathCache cache = new BagPathCache();
    private String id1 = "source1,destination1";
    private BagPath path1 = new BagPath(id1, new ArrayList<Vertex>(), 128);

    @Before
    public void beforeEach() {
    }

    @Test
    public void testPutInCache() {
        Assert.assertFalse(cache.hasKey(id1));
        Assert.assertNull(cache.get(id1));

        cache.put(path1);
        Assert.assertEquals(1, cache.getSize());

        cache.put(path1);
        Assert.assertEquals(1, cache.getSize());
    }

    @Test
    public void tetGetFromCache() {
        cache.put(path1);
        Assert.assertNotNull(cache.get(id1));

        Assert.assertEquals(id1, cache.get(id1).getId());

        String id2 = "source2,destination2";
        Assert.assertNull(cache.get(id2));
    }

}
