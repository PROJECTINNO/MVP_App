package p5e610.balance;

public class User {

   private String name;
   private String surname;
   private String username;
   private String email;
   private String password;

// le constructeur

    public User(String name, String surname, String username, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.password = password;
    }

 // les getters and setters
     public String getName() {return name;}

     public void setName(String name) {this.name = name;}

     public String getSurname() {return surname;}

     public void setSurname(String surname) {this.surname = surname;}

     public String getUsername() {return username;}

     public void setUsername(String username) {this.username = username;}

     public String getEmail() {return email;}

     public void setEmail(String email) {this.email = email;}

     public String getPassword() {return password;}

     public void setPassword(String password) {this.password = password;}

     @Override
     public String toString() {
         return "Courier : name = " + name + ", surname = " + surname + ", username = " + username +
                 ", email = " + email + "password = "+ password;
     }

}