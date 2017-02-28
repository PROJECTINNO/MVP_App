package p5e610.balance;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

import p5e610.balance.EllipseConstruction;

/**
 * Created by Julien on 14/02/2017.
 */

public class EllipseConstructionTest {
    @Test
    public void testMean() throws Exception {
        EllipseConstruction data = new EllipseConstruction();
        ArrayList<Double> x = new ArrayList<>();
        x.add(0.0);
        x.add(2.0);
        assert(EllipseConstruction.mean(x) == 1.0);
    }

    @Test
    public void testCovariance() throws Exception {
        EllipseConstruction data = new EllipseConstruction();
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        x.add(0.0);
        x.add(2.0);
        y.add(0.0);
        y.add(4.0);
        assert(EllipseConstruction.covar(x,y) == 4.0);
    }

    @Test
    public void testEigenvalues() throws Exception {
        EllipseConstruction data = new EllipseConstruction();
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        x.add(0.0);
        x.add(2.0);
        y.add(0.0);
        y.add(4.0);
        Double[] vp = {10.0, 0.0};
        assert(Arrays.equals(data.eigenvalues(x,y),vp));
    }

    @Test
    public void testMainDirection() throws Exception {
        EllipseConstruction data = new EllipseConstruction();
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        x.add(0.0);
        x.add(2.0);
        y.add(0.0);
        y.add(4.0);
        Double[] vp = {1.0/Math.sqrt(5), 2.0/Math.sqrt(5)};
        assert(Arrays.equals(data.mainDirection(x,y),vp));
    }
    @Test
    public void testPercentage() throws Exception {
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        x.add(0.0);
        x.add(1.0);
        x.add(-1.0);
        y.add(0.0);
        y.add(1.0);
        y.add(-1.0);
        Double theta = Math.PI / 4;
        Double a = 2.0;
        Double b = 1.0;
        Double p = 1.0;
        System.out.println(EllipseConstruction.percentage(x,y,a,b,theta,p));
        assert(EllipseConstruction.percentage(x,y,a,b,theta,p)== 1.0);
    }
}
