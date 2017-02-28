package p5e610.balance;

import java.util.ArrayList;
import java.util.Arrays;
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
    private Doctor doctor;
    private Patient patient;

    public Test(String position, AccelerationData genericData, String comments, double duration, Doctor doctor, Patient patient) {
        this.date = new java.util.Date(); // getting current time, I hope
        this.position = position;
        this.genericData = genericData;
        this.ellipseData = new double[6];
        this.comments = comments;
        this.duration = duration;
        this.patient = patient;
        this.doctor = doctor;
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
        int size = genericData.size();

        for (int j = 0; j<6;j++) {

            for (int i = j*(size/6); i < (j+1)*(size/6); i++) {
                ellipseData[j] = EllipseConstruction.angle((ArrayList) accx.subList(j*(size/6), (j+1)*(size/6)), (ArrayList) accy.subList(j*(size/6), (j+1)*(size/6)));
            }
        }
    }

    @Override
    public String toString() {
        return "Test of " + patient + " followed by " + doctor + ". \nDone on the " + date + ". \nThe position is" + position + ".\n" +
                "The comments are " + comments + ".\n" +
                "The ellipse angles every " + duration / 6 + " are " + Arrays.toString(ellipseData);
    }

}

