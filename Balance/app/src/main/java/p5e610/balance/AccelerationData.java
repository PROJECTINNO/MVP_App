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
    private Double xSum = 0.0;
    private Double ySum = 0.0;
    private Double zSum = 0.0;
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

    public static double xAverage(int i, int n, List<Coordinate> ls) {
        double sum = 0;

        for (int j = -(n - 1) / 2; j <= (n - 1) / 2; j++) {
            sum += ls.get(i + j).getX();
        }
        return sum/n;
    }

    public static double yAverage(int i, int n, List<Coordinate> ls) {
        double sum = 0;

        for (int j = -(n - 1) / 2; j <= (n - 1) / 2; j++) {
            sum += ls.get(i + j).getY();
        }
        return sum/n;
    }

    public static double zAverage(int i, int n, List<Coordinate> ls) {
        double sum = 0;

        for (int j = -(n - 1) / 2; j <= (n - 1) / 2; j++) {
            sum += ls.get(i + j).getZ();
        }
        return sum/n;
    }


    public ArrayList<Double> calcXAverage() {
        int n = coordinates.size();
        int N = 9;
        ArrayList<Double> acc = new ArrayList<Double>();

        for (int i = 0; i < n; i++) {
            Coordinate coord = coordinates.get(i);
            if (i < (N - 1) / 2) {
                acc.add(coord.getX());
            } else if (i > n - 1 - ((N - 1) / 2)) {
                acc.add(coord.getX());
            } else {
                acc.add(xAverage(i, N, coordinates));
            }
        }
        return acc;
    }

    public ArrayList<Double> calcYAverage() {
        int n = coordinates.size();
        int N = 9;
        ArrayList<Double> acc = new ArrayList<Double>();

        for (int i = 0; i < n; i++) {
            Coordinate coord = coordinates.get(i);
            if (i < (N - 1) / 2) {
                acc.add(coord.getY());
            } else if (i > n - 1 - ((N - 1) / 2)) {
                acc.add(coord.getY());
            } else {
                acc.add(yAverage(i, N, coordinates));
            }
        }
        return acc;
    }

    public ArrayList<Double> calcZAverage() {
        int n = coordinates.size();
        int N = 9;
        ArrayList<Double> acc = new ArrayList<Double>();

        for (int i = 0; i < n; i++) {
            Coordinate coord = coordinates.get(i);
            if (i < (N - 1) / 2) {
                acc.add(coord.getZ());
            } else if (i > n - 1 - ((N - 1) / 2)) {
                acc.add(coord.getZ());
            } else {
                acc.add(zAverage(i, N, coordinates));
            }
        }
        return acc;
    }

    public void add(Coordinate data){
        xSum += data.getX();
        ySum += data.getY();
        zSum += data.getZ();

        coordinates.add(data);
    }

    public void add(Double x, Double y, Double z, long timestamp){
        xSum += x;
        ySum += y;
        zSum += z;

        coordinates.add(new Coordinate(x, y, z, timestamp));
    }

    @Override
    public String toString() {
        return "AccelerationData{" + coordinates +
                '}';
    }
}

