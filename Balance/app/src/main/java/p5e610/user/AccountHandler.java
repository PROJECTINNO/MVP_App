package p5e610.user;

/**
 * Created by mathiasloh on 28/2/17.
 */

public final class AccountHandler {
    static User user;
    static Boolean login;
    static Boolean returnFromTest = false;

    private AccountHandler(){
        user = null;
        login = false;
        returnFromTest = false;
    }

    public static void setLogin(Boolean bool){
        login = bool;
    }

    public static Boolean getLogin(){
        return login;
    }

    public static Boolean getReturnFromTest() {return returnFromTest;}

    public static User getUser() {
        return user;
    }

    public static void setUser(User newUser) {
        user = newUser;
    }

    public static void setReturnFromTest(Boolean bool){returnFromTest = bool;}
}
