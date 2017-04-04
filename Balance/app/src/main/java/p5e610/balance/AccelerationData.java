package p5e610.balance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static java.lang.Math.round;

/**
 * Created by Cecile on 07/02/2017.
 * a class to represent the data collected by the accelerometer
 */

public class AccelerationData {
    private int size = 0;

    public static class Coordinate {
        Double x;
        Double y;
        Double z;
        long timestamp;

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }

        public Double getZ() {
            return z;
        }

        public void setZ(Double z) {
            this.z = z;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return String.format("x: %d, y: %d, z: %d, timestamp: %s", x, y, z, new Date(timestamp));
        }

        public Coordinate(Double x, Double y, Double z, long timestamp) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.timestamp = timestamp;
        }
    }

    private List<Coordinate> coordinates;

    public AccelerationData() {
        this.coordinates = new ArrayList<Coordinate>();
    }

    public Coordinate get(int i) {
        return coordinates.get(i);
    }

    public int size() {
        return size;
    }

    public ArrayList<Double> getAccX() {
        ArrayList<Double> accx = new ArrayList<>();
        for (int i = 0; i < this.coordinates.size(); i++) {
            accx.add(this.get(i).getX());
        }
        return accx;
    }

    public ArrayList<Double> getAccY() {
        ArrayList<Double> accy = new ArrayList<>();
        for (int i = 0; i < this.coordinates.size(); i++) {
            accy.add(this.get(i).getY());
        }
        return accy;
    }

    public ArrayList<Double> getAccZ() {
        ArrayList<Double> accz = new ArrayList<>();
        for (int i = 0; i < this.coordinates.size(); i++) {
            accz.add(this.get(i).getZ());
        }
        return accz;
    }

    public void add(Coordinate data) {
        coordinates.add(data);
    }

    public void add(Double x, Double y, Double z, long timestamp) {
        coordinates.add(new Coordinate(x, y, z, timestamp));
    }

    @Override
    public String toString() {
        return "AccelerationData{" + coordinates + '}';
    }

    /**
     * functions relatives to the construciton of the Ellipse
     */

    public static double mean(ArrayList<Double> arr) {
        double sum = 0.0;
        for (int i = 0; i < arr.size(); i++) {
            sum = sum + arr.get(i);
        }

        return sum / arr.size();
    }

    public static double covar(ArrayList<Double> arr1, ArrayList<Double> arr2) {
        double m1 = mean(arr1);
        double m2 = mean(arr2);

        double sumsq = 0.0;
        for (int i = 0; i < arr1.size(); i++) {
            sumsq += (m1 - arr1.get(i)) * (m2 - arr2.get(i));
        }

        return sumsq / arr1.size();
    }

    public static Double[][] covarMatrix(ArrayList<Double> arr1, ArrayList<Double> arr2) {
        return new Double[][]{
                {covar(arr1, arr1), covar(arr1, arr2)},
                {covar(arr1, arr2), covar(arr2, arr2)}};
    }


    public static Double[] eigenvalues(ArrayList<Double> arr1, ArrayList<Double> arr2) {
        Double[][] cov = covarMatrix(arr1, arr2);
        Double covXX = cov[0][0];
        Double covXY = cov[0][1];
        Double covYY = cov[1][1];

        Double delta = square(covXX - covYY) + 4 * square(covXY);

        return new Double[]{
                ((covXX + covYY) + Math.sqrt(delta)) / 2,
                (((covXX + covYY) - Math.sqrt(delta)) / 2)};
    }

    public static Double[] mainDirection(ArrayList<Double> arr1, ArrayList<Double> arr2) {
        Double[][] cov = covarMatrix(arr1, arr2);
        Double lambda = eigenvalues(arr1, arr2)[0];

        Double covXX = cov[0][0];
        Double covXY = cov[0][1];
        Double[] res = {1., 0.};

        if (covXY != 0) {
            Double y = (lambda - covXX) / covXY;
            Double norm = 1. / Math.sqrt(1. + square(y));
            res[0] = norm;
            res[1] = norm * (lambda - covXX) / covXY;
        }

        return res;
    }

    public static Double angle(ArrayList<Double> arr1, ArrayList<Double> arr2) {
        Double[] A = mainDirection(arr1, arr2);
        if (A[0] == 0) {
            return Math.abs(A[1]) * Math.PI / 2;
        } else {
            Double B = A[1] / A[0];
            Double res = A[0] * Math.atan(B) / Math.abs(A[0]);
            return res;
        }

    }

    public static Boolean inEllipse(Double x,
                                    Double y,
                                    Double a,
                                    Double b,
                                    Double theta,
                                    Double X0,
                                    Double Y0) {

        Double X = Math.cos(theta) * x + Math.sin(theta) * y;
        Double Y = Math.cos(theta) * y - Math.sin(theta) * x;

        return (square(X - X0) / square(a) + square(Y - Y0) / square(b)) <= 1.;
    }

    private static Double square(Double x) {
        return x * x;
    }

    public static double percentage(ArrayList<Double> X, ArrayList<Double> Y, Double a, Double b, Double theta, Double p) {
        double inEllipseCount = 0;
        double xMean = mean(X);
        double yMean = mean(Y);

        for (int j = 0; j < X.size(); j++) {
            if (inEllipse(X.get(j), Y.get(j), p * a, p * b, theta, xMean, yMean)) {
                inEllipseCount = inEllipseCount + 1.0;
            }
        }
        return inEllipseCount / X.size();
    }

    public static Double[] zeros(ArrayList<Double> X, ArrayList<Double> Y) {
        Double[] counter = {0.0, 0.0};

        for (int i = 0; i < X.size() - 1; i++) {
            if (X.get(i) * X.get(i + 1) < 0) {
                counter[0] += 1;
            }
            if (Y.get(i) * Y.get(i + 1) < 0) {
                counter[1] += 1;
            }
        }
        return counter;
    }


    public static double MaximumModulus(ArrayList<Double> X, ArrayList<Double> Y) {
        double res = 0;
        for (int i = 0; i < X.size() - 1; i++) {
            double a = Math.sqrt(Math.pow(X.get(i), 2) + Math.pow(Y.get(i), 2));
            if (a > res) {
                res = a;
            }
        }
        return res;
    }

    public static double range(ArrayList<Double> X) {
        return Collections.max(X) - Collections.min(X);
    }
}