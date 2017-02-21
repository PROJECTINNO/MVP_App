package p5e610.balance;

import java.util.*;


public class Ellipseconstruction {

    public static double mean (ArrayList <Double> X) {
        double fantasticresult  = 0.0;
        for (int i = 0 ; i < X.size(); i++)
        { fantasticresult += X.get(i);}
        return fantasticresult/(X.size());

    }

    public static double covar(ArrayList <Double> v1, ArrayList <Double> v2) {
        double m1 = mean(v1);
        double m2 = mean(v2);
        double sumsq = 0.0;
        for (int i = 0; i < v1.size(); i++){
            sumsq += (m1 - v1.get(i)) * (m2 - v2.get(i));}
        return sumsq;
    }

    public Double[][] covariance (ArrayList <Double> X , ArrayList <Double> Y) {
        Double[][] resultat = {{covar(X,X) , covar(X,Y)}, {covar (X,Y), covar(Y,Y)}};
        return resultat;
    }

    public Double[] valeurspropres (ArrayList <Double> X , ArrayList <Double> Y) {

        Double[][] M = covariance(X,Y);
        Double a = M[0][0];
        Double b = M[0][1];
        Double c = M[1][1];
        Double Delta = (a-c)*(a-c) + 4*b*b;

        Double [] res = {((a+c)+ Math.sqrt(Delta))/2 ,(((a+c) - Math.sqrt(Delta))/2) };

        return res ;
    }

    public Double[] MainDirection (ArrayList <Double> X , ArrayList <Double> Y) {
        Double[][] M = covariance(X,Y);
        Double a = M[0][0];
        Double b = M[0][1];
        Double lambda = valeurspropres(X,Y)[0];
        Double[] res = {1.,0.};
        if (b == 0) {
            return res;
        }
        else {
            Double y = (lambda -a)/b;
            Double N = 1./Math.sqrt(1. + y*y);
            res[0] = N ;
            res[1] = N * (lambda - a)/b ;
            return res ;

        }

    }

    public Double Angle (ArrayList <Double> X , ArrayList <Double> Y) {
        Double[] A = MainDirection(X,Y);
        if (A[0] == 0) {
            return Math.abs(A[1]) * Math.PI/2 ;
        }
        else {
            Double B = A[1]/A[0];
            Double res = A[0] *  Math.atan(B)/ Math.abs(A[0]);
            return res;
        }

    }
}






