package p5e610.balance;

import java.util.ArrayList;

/**
 * Created by Cecile on 07/02/2017.
 * a class to represent the data collected by the accelerometer
 */

public class AccelerationData {

    private ArrayList<Long> timestamp; //the array of the time at which the data is taken
    private ArrayList<Double> x; //array of x acceleration
    private ArrayList<Double> y; //array of y acceleration
    private ArrayList<Double> z; //array of z acceleration

    //Constructor having all the data
    public AccelerationData(ArrayList<Long> timestamp, ArrayList<Double> x, ArrayList<Double> y, ArrayList<Double> z) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //Empty constuctor
    public AccelerationData() {
        this.timestamp = new ArrayList<Long>();
        this.x = new ArrayList<Double>();
        this.y = new ArrayList<Double>();
        this.z = new ArrayList<Double>();
    }

    /**
     * getters and setters
     */

    public ArrayList<Long> getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ArrayList<Long> timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<Double> getX() {
        return x;
    }

    public void setX(ArrayList<Double> x) {
        this.x = x;
    }

    public ArrayList<Double> getY() {
        return y;
    }

    public void setY(ArrayList<Double> y) {
        this.y = y;
    }

    public ArrayList<Double> getZ() {
        return z;
    }

    public void setZ(ArrayList<Double> z) {
        this.z = z;
    }

<<<<<<< HEAD
=======
    public void addX(double data){
        ArrayList<Double> old = this.getX();
        old.add(data);
        this.setX(old);
    }

    public void addY(double data){
        ArrayList<Double> old = this.getY();
        old.add(data);
        this.setY(old);
    }

    public void addZ(double data){
        ArrayList<Double> old = this.getZ();
        old.add(data);
        this.setZ(old);
    }

    public void addTimestamp(long data){
        ArrayList<Long> old = this.getTimestamp();
        old.add(data);
        this.setTimestamp(old);
    }

>>>>>>> 69aea7727fe9c7f93471c6cd7f0181ee7060cf86
    @Override
    public String toString() {
        return "AccelerationData{" +
                "timestamp=" + timestamp +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}

