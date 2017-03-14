package p5e610.balance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Cecile on 07/02/2017.
 * a class to represent the data collected by the accelerometer
 */

public class AccelerationData {
    private int size = 0;

    /**
     * Coordinate represents a set of 4 datas,
     * accelerations in x, y, z and the time this data has been taken
     */
    public static class Coordinate {
        Double x;
        Double y;
        Double z;
        long timestamp;

        public Double getX() {return x;}
        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {return y;}
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

    /**
     * AccelerationData is then just an array of coordinates
     */
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

    public ArrayList<Double> getAccX(){
        ArrayList<Double> accx = new ArrayList<>();
        for (int i = 0; i < this.coordinates.size(); i++){
            accx.add(this.get(i).getX());
        }
        return accx;
    }

    public ArrayList<Double> getAccY(){
        ArrayList<Double> accy = new ArrayList<>();
        for (int i = 0; i < this.coordinates.size(); i++){
            accy.add(this.get(i).getY());
        }
        return accy;
    }

    public ArrayList<Double> getAccZ(){
        ArrayList<Double> accz = new ArrayList<>();
        for (int i = 0; i < this.coordinates.size(); i++){
            accz.add(this.get(i).getZ());
        }
        return accz;
    }

    public void add(Coordinate data){
        coordinates.add(data);
    }

    public void add(Double x, Double y, Double z, long timestamp){
        coordinates.add(new Coordinate(x, y, z, timestamp));
    }

    @Override
    public String toString() {
        return "AccelerationData{" + coordinates + '}';
    }

    /**
     * here are the function used to calculate the confidence ellipse
     */

    public static double mean(ArrayList<Double> arr) {
        double sum  = 0.0;
        for (int i = 0 ; i < arr.size(); i++) {
            sum += arr.get(i);
        }

        return sum / arr.size();
    }

    public double covar() {
        ArrayList<Double> arrX = this.getAccX();
        ArrayList<Double> arrY = this.getAccY();
        double m1 = mean(arrX);
        double m2 = mean(arrY);

        double sumsq = 0.0;
        for (int i = 0; i < arrX.size(); i++){
            sumsq += (m1 - arrX.get(i)) * (m2 - arrY.get(i));
        }

        return sumsq;
    }

    public Double[][] covarMatrix() {
        return new Double[][] {
                { covar(), covar() },
                { covar(), covar() } };
    }


    public Double[] eigenvalues() {
        Double[][] cov = covarMatrix();
        Double covXX = cov[0][0];
        Double covXY = cov[0][1];
        Double covYY = cov[1][1];

        Double delta = square(covXX - covYY) + 4 * square(covXY);

        return new Double[] {
                ((covXX + covYY) + Math.sqrt(delta)) / 2,
                (((covXX + covYY) - Math.sqrt(delta)) / 2)};
    }

    public Double[] mainDirection() {
        Double[][] cov = covarMatrix();
        Double lambda = eigenvalues()[0];

        Double covXX = cov[0][0];
        Double covXY = cov[0][1];
        Double[] res = {1., 0.};

        if (covXY != 0)  {
            Double y = (lambda - covXX) / covXY;
            Double norm = 1. / Math.sqrt(1. + square(y));
            res[0] = norm;
            res[1] = norm * (lambda - covXX) / covXY;
        }

        return res;
    }

    public Double angle() {
        Double[] A = mainDirection();
        if (A[0] == 0) {
            return Math.abs(A[1]) * Math.PI / 2 ;
        } else {
            Double B = A[1] / A[0];
            Double res = A[0] * Math.atan(B) / Math.abs(A[0]);
            return res;
        }

    }

    /**
     *
     * @param x coordinates of the point we want to check is in the ellipse or not
     * @param y
     * @param a the big semi axis
     * @param b the small semi axis
     * @param theta
     * @param X0 center of ellipse
     * @param Y0 center of ellispe
     * @return a boolean that says if the point (x,y) is in the Ellipse described by the other parameters
     */
    public Boolean inEllipse( Double x,
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

    private  Double square(Double x) { return x * x; }

    public double percentage(ArrayList <Double> X, ArrayList <Double> Y, Double a, Double b, Double theta, Double p) {
        double inEllipseCount = 0;
        double xMean = mean(X);
        double yMean = mean(Y);

        for (int j = 0; j < X.size(); j++) {
            if(this.inEllipse(X.get(j), Y.get(j), p * a, p * b, theta, xMean, yMean)) {
                inEllipseCount = inEllipseCount + 1.0;
            }
        }
        return inEllipseCount / X.size();
    }
}

