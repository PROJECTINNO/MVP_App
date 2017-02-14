package p5e610.balance;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

import p5e610.balance.Ellipseconstruction;

/**
 * Created by Julien on 14/02/2017.
 */

public class EllipseConstructionTest {
    @Test
    public void ellipse_is_correct() throws Exception {
        Ellipseconstruction data = new Ellipseconstruction();
        ArrayList<Double> x = new ArrayList<Double>();
        ArrayList<Double> y = new ArrayList<Double>();
        Double [] vectp = {1.0,0.0};
        x.add(0.0);
        x.add(2.0);
        y.add(0.0);
        y.add(0.0);

        assertEquals(2.0, data.valeurspropres()[0]);
        assertEquals(0.0, data.valeurspropres()[1]);
        assertEquals(vectp, data.MainDirection());
}
