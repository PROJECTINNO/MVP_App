package p5e610.balance;


public class Doctor extends User {
    private ArrayList <Patient> PatientList

    public Doctor(String name, String surname, String username, String email, String password, ArrayList<Patient> patientList) {
        super(name, surname, username, email, password);
        PatientList = patientList;
    }

    public ArrayList <Patient> getPatientList() {return PatientList;}

    public void setPatientList(ArrayList <Patient> PatientList) {this.PatientList = PatientList}

    @Override
    public String toString() {
        return "Doctor : PatientList = " + PatientList
}
