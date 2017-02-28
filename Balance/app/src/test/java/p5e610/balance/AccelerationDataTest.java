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
    public void addX_is_correct() throws Exception {
        AccelerationData data = new AccelerationData();
        ArrayList<Double> x = new ArrayList<Double>();
        data.addX(1.0);
        x.add(1.0);
        assertEquals(x, data.getX());
    }

    @Test
    //public void isAddTimestampValid() throws Exception {
    public void addtimestamp_is_correct() throws Exception {
        AccelerationData data = new AccelerationData();
        ArrayList<Long> timestamp = new ArrayList<Long>();
        double d = 1.0;
        long l = (new Double(d)).longValue();
        data.addTimestamp(l);
        timestamp.add(l);
        assertEquals(timestamp, data.getTimestamp());
    }
}