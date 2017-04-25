package p5e610.user;

/**
 * Created by mathiasloh on 28/2/17.
 */

public final class AccountHandler {
    static User user;
    static Boolean login;

    private AccountHandler(){
        user = null;
        login = false;
    }

    public static void setLogin(Boolean bool){
        login = bool;
    }

    public static Boolean getLogin(){
        return login;
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User newUser) {
        user = newUser;
    }
}
