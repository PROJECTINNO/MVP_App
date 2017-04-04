package p5e610.balance;


import java.util.ArrayList;

public class Patient extends User {
    private Doctor doctor;
    private ArrayList <AccelerationData> history;

    public Patient(String name, String surname, String username, String email) {
        super(name, surname, username, email);
    }

    public Doctor getDoctor() {return doctor;}

    public void setDoctor(Doctor doctor){
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "Patient : Doctor = " + doctor + "History =" + history;
    }
}
