package p5e610.balance;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Cecile on 28/02/2017.
 * a test is one collection of data, and all the relevant infirmations that go with it
 */

public class Test {

    private Date date ;
    private String position;
    private AccelerationData genericData;
    private double[] ellipseData;
    private String comments;
    private double duration;

    public Test(String position, AccelerationData genericData, String comments, double duration) {
        this.date = new java.util.Date(); // getting current time, I hope
        this.position = position;
        this.genericData = genericData;
        this.ellipseData = new double[6];
        this.comments = comments;
        this.duration = duration;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public AccelerationData getGenericData() {
        return genericData;
    }

    public void setGenericData(AccelerationData genericData) {
        this.genericData = genericData;
    }


    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void calculateEllipseData(){
        ArrayList<Double> accx = genericData.getAccX();
        ArrayList<Double> accy = genericData.getAccY();

    }
}

