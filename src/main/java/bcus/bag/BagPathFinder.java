package bcus.bag;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ble on 4/15/15.
 */
public class BagPathFinder {
    List<String[]> conveyorPathList = new ArrayList<String[]>();
    Map<String, String> flightDepartureMap = new HashMap<String, String>();
    List<String[]> bagFlightList = new ArrayList<String[]>();

    protected void readSections(String file) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        // skip first # section comment
        br.readLine();

        // read conveyor section
        while (!(line = br.readLine()).startsWith("#")) {
            conveyorPathList.add(line.split(" "));
        }

        // read departure section
        while (!(line = br.readLine()).startsWith("#")) {
            String[] departureArr = line.split(" ");
            flightDepartureMap.put(departureArr[0], departureArr[1]);  // only use first and second column for flight -> destination node mapping
        }

        // read bags section
        while ((line = br.readLine()) != null) {
            bagFlightList.add(line.split(" "));  // only use first and second column for flight -> destination node mapping
        }


    }












}
