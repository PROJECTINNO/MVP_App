package p5e610.balance;


import java.util.ArrayList;

public class Patient extends User {
    private Doctor doctor;
    private ArrayList <AccelerationData> historique;

    public Patient(String name, String surname, String username, String email, Doctor doctor, ArrayList<AccelerationData> historique) {
        super(name, surname, username, email);
        this.doctor = doctor;
        this.historique = historique;
    }

    public ArrayList<AccelerationData> getHistorique() {return historique;}

    public void setHistorique(ArrayList<AccelerationData> historique) {this.historique = historique;}

    public Doctor getDoctor() {return doctor;}

    public void setDoctor(Doctor doctor){
        this.doctor = doctor;
    }

//    @Override
//    public String toString() {
//        return "Patient : Doctor = " + doctor + "Historique =" + historique;
//    }
}
