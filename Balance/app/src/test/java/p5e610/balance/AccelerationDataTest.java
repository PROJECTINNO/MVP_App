package p5e610.balance;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by Julien on 14/02/2017.
 */
public class AccelerationDataTest {
    @Test
    public void add_is_correct() throws Exception {
        AccelerationData data = new AccelerationData();
        ArrayList<Double> x = new ArrayList<Double>();
        assert(data.addX(1.0),x.add(1.0));
    }
}
