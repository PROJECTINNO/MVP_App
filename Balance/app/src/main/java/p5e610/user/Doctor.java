package p5e610.user;


import java.util.ArrayList;

import p5e610.user.Patient;
import p5e610.user.User;

public class Doctor extends User {
    private ArrayList<Patient> PatientList;

    public Doctor(String name, String surname, String username, String email, ArrayList<Patient> patientList) {
        super(name, surname, username, email);
        PatientList = patientList;
    }

    public ArrayList<Patient> getPatientList() {return PatientList;}

    public void setPatientList(ArrayList<Patient> PatientList) {this.PatientList = PatientList;}

    @Override
    public String toString() {
        return "Doctor : PatientList = " + PatientList;
    }

}
