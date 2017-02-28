package p5e610.balance;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

import p5e610.balance.AccelerationData;

/**
 * Created by Julien on 14/02/2017.
 */
public class AccelerationDataTest {

    @Test
    public void testAddX() throws Exception {
        AccelerationData data = new AccelerationData();
        data.add(1.0, 1.0, 1.0, 2434);
        assertEquals((Double)1.0, data.get(0).getX());
    }

    @Test
    public void testAddY() throws Exception {
        AccelerationData data = new AccelerationData();
        data.add(1.0, 5.0, 1.0, 2434);
        assertEquals((Double)5.0, data.get(0).getY());
    }

    @Test
    public void testAddZ() throws Exception {
        AccelerationData data = new AccelerationData();
        data.add(1.0, 1.0, 7.0, 2434);
        assertEquals((Double)7.0, data.get(0).getZ());
    }


    @Test
    public void testAddTimestamp() throws Exception {
        AccelerationData data = new AccelerationData();
        ArrayList<Long> timestamp = new ArrayList<Long>();
        double d = 1.0;
        long l = (new Double(d)).longValue();
        data.add(1.0, 1.0, 1.0, l);
        timestamp.add(l);
        assertEquals(timestamp, data.get(0).getTimestamp());
    }

    @Test
    public void testgetAccX() throws Exception {
        AccelerationData data = new AccelerationData();
        double d = 100.0;
        long l = (new Double(d)).longValue();
        AccelerationData.Coordinate a = new AccelerationData.Coordinate(1.0, 2.0, 3.0, l);
        AccelerationData.Coordinate b = new AccelerationData.Coordinate(4.0, 5.0, 6.0, l);
        AccelerationData.Coordinate c = new AccelerationData.Coordinate(7.0, 8.0, 9.0, l);
        data.add(a);
        data.add(b);
        data.add(c);
        ArrayList<Double> res = new ArrayList<Double>;
        res.add(1.0);
        res.add(4.0);
        res.add(7.0);
        assertEquals(res, data.getAccX());
    }
}
