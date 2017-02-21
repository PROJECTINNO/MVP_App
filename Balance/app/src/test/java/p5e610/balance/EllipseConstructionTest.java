package p5e610.balance;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

import p5e610.balance.Ellipseconstruction;

/**
 * Created by Julien on 14/02/2017.
 */

public class EllipseConstructionTest {
    @Test
    public void mean_is_correct() throws Exception {
        Ellipseconstruction data = new Ellipseconstruction();
        ArrayList<Double> x = new ArrayList<Double>();
        x.add(0.0);
        x.add(2.0);
        assert(Ellipseconstruction.mean(x) == 1.0);
    }

    @Test
    public void covar_is_correct() throws Exception {
        Ellipseconstruction data = new Ellipseconstruction();
        ArrayList<Double> x = new ArrayList<Double>();
        ArrayList<Double> y = new ArrayList<Double>();
        //Double[] vectp = {1.0, 0.0};
        x.add(0.0);
        x.add(2.0);
        y.add(0.0);
        y.add(4.0);
        assert(Ellipseconstruction.covar(x,y) == 4.0);
    }

    @Test
    public void valeurspropres_is_correct() throws Exception {
        Ellipseconstruction data = new Ellipseconstruction();
        ArrayList<Double> x = new ArrayList<Double>();
        ArrayList<Double> y = new ArrayList<Double>();
        x.add(0.0);
        x.add(2.0);
        y.add(0.0);
        y.add(4.0);
        Double[] vp = {10.0, 0.0};
        assert(Arrays.equals(data.valeurspropres(x,y),vp));
    }

    @Test
    public void maindirection_is_correct() throws Exception {
        Ellipseconstruction data = new Ellipseconstruction();
        ArrayList<Double> x = new ArrayList<Double>();
        ArrayList<Double> y = new ArrayList<Double>();
        x.add(0.0);
        x.add(2.0);
        y.add(0.0);
        y.add(4.0);
        Double[] vp = {1.0/Math.sqrt(5), 2.0/Math.sqrt(5)};
        assert(Arrays.equals(data.mainDirection(x,y),vp));
    }
    @Test
    public void pourcentage_is_correct() throws Exception {
        ArrayList<Double> x = new ArrayList<Double>();
        ArrayList<Double> y = new ArrayList<Double>();
        x.add(0.0);
        x.add(1.0);
        x.add(-1.0);
        y.add(0.0);
        y.add(10.0);
        y.add(-1.0);
        Double theta = Math.PI / 4;
        Double a = 2.0;
        Double b = 1.0;
        Double p = 1.0;
        assert(Ellipseconstruction.pourcentage(x,y,a,b,theta,p)== 2.0/3.0);
    }
}
