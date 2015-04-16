package bcus.bag;


import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by ble on 4/15/15.
 */
public class TestBagPathFinder {

    @Test
    public void testReadSections() {

        BagPathFinder bagPathFinder = new BagPathFinder();
        try {
            bagPathFinder.readSections(getClass().getClassLoader().getResource("sample_data.txt").getFile());

            Assert.assertTrue(bagPathFinder.conveyorPathList.size() > 0);
            for (String[] conveyorPathArr : bagPathFinder.conveyorPathList) {
                System.out.println(conveyorPathArr[0] + " : " + conveyorPathArr[1]);
            }

            Assert.assertTrue(bagPathFinder.flightDepartureMap.size() > 0);
            for (String key : bagPathFinder.flightDepartureMap.keySet()) {
                System.out.println(key + " : " + bagPathFinder.flightDepartureMap.get(key));
            }

            Assert.assertTrue(bagPathFinder.bagFlightList.size() > 0);
            for (String[] bagFlightArr : bagPathFinder.bagFlightList) {
                System.out.println(bagFlightArr[0] + " : " + bagFlightArr[1] + " : " + bagFlightArr[2]);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }





}
