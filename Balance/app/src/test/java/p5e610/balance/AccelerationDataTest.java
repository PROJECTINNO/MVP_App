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
<<<<<<< HEAD
    public void add_is_correct() throws Exception {
        AccelerationData data = new AccelerationData();
        ArrayList<Double> x = new ArrayList<Double>();
        assert(data.addX(1.0),x.add(1.0));
=======
    public void addX_is_correct() throws Exception {
        AccelerationData data = new AccelerationData();
        ArrayList<Double> x = new ArrayList<Double>();
        data.addX(1.0);
        x.add(1.0);
        assertEquals(data,x);
    }
    @Test
    public void addtimestamp_is_correct() throws Exception {
        AccelerationData data = new AccelerationData();
        ArrayList<Long> timestamp = new ArrayList<Long>();
        double d=1.0;
        long l = (new Double(d)).longValue();
        data.addTimestamp(l);
        timestamp.add(l);
        assertEquals(data,timestamp);
>>>>>>> 6fa39412b2fbfdf7cbb8f98d130ff424fbbd2c84
    }
}
