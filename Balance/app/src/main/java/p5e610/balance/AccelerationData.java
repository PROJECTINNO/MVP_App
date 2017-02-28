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
}

