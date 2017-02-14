package p5e610.balance;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Created by Julien on 14/02/2017.
 */

public class EllipseConstructionTest {
    @Test
    public void ellipse_is_correct() throws Exception {
        AccelerationData data = new EllipseConstruction();
        ArrayList<Double> x = new ArrayList<Double>();
        ArrayList<Double> y = new ArrayList<Double>();
        ArrayList<Double> vp = new ArrayList<Double>();
        x.add(0.0);
        x.add(2.0);
        y.add(0.0);
        y.add(0.0);

        vp.add(1.0);
        vp.add(0.0);

        assertEquals(2.0, data.vp1());
        assertEquals(0.0, data.vp2());
        assertEquals(vp, data.vectp());
}
