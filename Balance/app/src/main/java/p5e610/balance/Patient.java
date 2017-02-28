package p5e610.balance;


import java.util.ArrayList;

public class Patient extends User {
    private Doctor doctor;
    private ArrayList <AccelerationData> history;

    public Patient(String name, String surname, String username, String email, Doctor doctor, ArrayList<AccelerationData> historique) {
        super(name, surname, username, email);
        this.doctor = doctor;
        this.history = historique;
    }

    public ArrayList<AccelerationData> getHistory() {return history;}

    public void setHistory(ArrayList<AccelerationData> history) {this.history = history;}

    public Doctor getDoctor() {return doctor;}

    public void setDoctor(Doctor doctor){
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "Patient : Doctor = " + doctor + "History =" + history;
    }
}
