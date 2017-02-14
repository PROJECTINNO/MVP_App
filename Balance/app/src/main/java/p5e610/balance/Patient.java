package p5e610.balance;


import java.util.ArrayList;

public class Patient extends User {
    private Doctor doctor;
    private ArrayList <AccelerationData> historique;

    public Patient(String name, String surname, String username, String email, String password, Doctor doctor, ArrayList<AccelerationData> historique) {
        super(name, surname, username, email, password);
        this.doctor = doctor;
        this.historique = historique;
    }

    public ArrayList<AccelerationData> getHistorique() {return historique;}

    public void setHistorique(ArrayList<AccelerationData> historique) {this.historique = historique;}

    public Doctor getDoctor() {return doctor;}
<<<<<<< HEAD

=======
>>>>>>> 69aea7727fe9c7f93471c6cd7f0181ee7060cf86

    public void setDoctor(Doctor doctor){
        this.doctor = doctor;
    }

//    @Override
//    public String toString() {
//        return "Patient : Doctor = " + doctor + "Historique =" + historique;
//    }
}
