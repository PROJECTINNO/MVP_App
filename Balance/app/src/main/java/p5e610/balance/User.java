package p5e610.balance;

public abstract class User {

   private String name;
   private String surname;
   private String username;
   private String email;
    private Integer hashedPw;

    public User(String name, String surname, String username, String email, Integer hashedPw) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.hashedPw = hashedPw;
    }

    public User() { }

     public String getName() {return name;}

     public void setName(String name) {this.name = name;}

     public String getSurname() {return surname;}

     public void setSurname(String surname) {this.surname = surname;}

     public String getUsername() {return username;}

     public void setUsername(String username) {this.username = username;}

     public String getEmail() {return email;}

    public abstract boolean isDoctor();

     public void setEmail(String email) {this.email = email;}

     @Override
     public String toString() {
         return "Courier : name = " + name + ", surname = " + surname + ", username = " + username +
                 ", email = " + email;
     }

}