package p5e610.balance;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import org.junit.Assert;

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
        double d = 1.0;
        long l = (new Double(d)).longValue();
        data.add(1.0, 1.0, 1.0, l);
        assertEquals(l, data.get(0).getTimestamp());
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
        ArrayList<Double> res = new ArrayList<Double>();
        res.add(1.0);
        res.add(4.0);
        res.add(7.0);
        assertEquals(res, data.getAccX());
    }

    @Test
    public void testMean() throws Exception {
        ArrayList<Double> x = new ArrayList<>();
        x.add(0.0);
        x.add(2.3);
        assertEquals(1.15,AccelerationData.mean(x),0.001);
    }

    @Test
    public void testCovariance() throws Exception {
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        x.add(0.0);
        x.add(2.0);
        y.add(0.0);
        y.add(4.0);
        assertEquals(2.0,AccelerationData.covar(x,y),0.001);
    }

    @Test
    public void testEigenvalues() throws Exception {
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        x.add(0.0);
        x.add(2.0);
        y.add(0.0);
        y.add(4.0);
        Double[] vp = {5.0, 0.0};
        Assert.assertArrayEquals(vp,AccelerationData.eigenvalues(x,y));
    }

    @Test
    public void testMainDirection() throws Exception {
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        x.add(0.0);
        x.add(2.0);
        y.add(0.0);
        y.add(4.0);
        Double[] vp = {1.0/Math.sqrt(5), 2.0/Math.sqrt(5)};
        Assert.assertArrayEquals(vp,AccelerationData.mainDirection(x,y));
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
        assertEquals(1.0,AccelerationData.percentage(x,y,a,b,theta,p),0.001);
    }

    @Test
    public void testzeros() throws Exception{
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        x.add(1.2);
        x.add(0.1);
        x.add(-1.0);
        y.add(0.6);
        y.add(-1.0);
        y.add(1.3);
        Double [] count = {1.0,2.0};
        Assert.assertArrayEquals(count,AccelerationData.zeros(x,y));
    }
    @Test
    public void testMaximumModulus() throws Exception{
        ArrayList<Double> x = new ArrayList<>();
        ArrayList<Double> y = new ArrayList<>();
        x.add(10.0);
        x.add(2.0);
        x.add(-3.0);
        y.add(0.0);
        y.add(-1.0);
        y.add(4.0);
        double maxi = 10.0;
        assertEquals(maxi,AccelerationData.maximumModulus(x,y),0.001);
    }
}
