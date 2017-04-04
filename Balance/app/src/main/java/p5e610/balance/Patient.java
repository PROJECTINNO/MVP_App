package p5e610.balance;


import java.util.ArrayList;

public class Patient extends User {
    private Doctor doctor;
    private ArrayList <AccelerationData> history;

    public Patient(String name, String surname, String username, String email, Integer hashedPw) {
        super(name, surname, username, email, hashedPw);
    }

    public Doctor getDoctor() {return doctor;}

    public boolean isDoctor() {
        return false;
    }
    public void setDoctor(Doctor doctor){
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "Patient : Doctor = " + doctor + "History =" + history;
    }
}
