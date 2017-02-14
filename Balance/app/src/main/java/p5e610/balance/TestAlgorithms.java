package p5e610.balance;

import java.util.ArrayList;

/**
 * Created by mathiasloh on 14/2/17.
 */


public final class TestAlgorithms {
    private TestAlgorithms(){

    }


    public static ArrayList<Double> constantAcceleration(double c, String s, ArrayList<AccelerationData> sensorData){
        int n = sensorData.size();
        ArrayList<Double> acc = new ArrayList<Double>();
        if (s == "x"){
            for (int i = 0; i<n;i++){
                acc.add(c);
            }
        }

        else if (s == "y"){
            for (int i = 0; i<n;i++){
                acc.add(0.0);
            }
        }

        else if (s == "z"){
            for (int i = 0; i<n;i++){
                acc.add(0.0);
            }
        }

        return acc;
    }

    public static ArrayList<Double> sinusoideAcceleration(String s, ArrayList<AccelerationData> sensorData){
        int n = sensorData.size();
        ArrayList<Double> acc = new ArrayList<Double>();
        if(s == "x") {
            for (int i = 0; i < n; i++) {
                acc.add(Math.cos((4.0 * Math.PI * i) / ( n)));
            }
        }

        else if (s == "y"){
            for (int i = 0; i < n; i++) {
                acc.add(0.0);
            }
        }

        else if (s == "z"){
            for (int i = 0; i < n; i++) {
                acc.add(0.0);
            }
        }
        return acc;
    }

    public static double average(int i, int N, ArrayList<Double> alist) {
        double sum = 0;

        for (int j = -(N - 1) / 2; j <= (N - 1) / 2; j++) {
            sum += alist.get(i + j);
        }
        return sum/N;
    }

    public static ArrayList<Double> calculateAverage(ArrayList<Double> alist) {
        int n = alist.size();
        int N = 9;
        ArrayList<Double> acc = new ArrayList<Double>();

        for (int i = 0; i < alist.size(); i++) {
            if (i < (N - 1) / 2) {
                acc.add(alist.get(i));

            } else if (i > n - 1 - ((N - 1) / 2)) {
                acc.add(alist.get(i));
            } else {
                acc.add(average(i, N, alist));
            }
        }
        return acc;
    }
}

