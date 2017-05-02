package p5e610.user;

/**
 * Created by mathiasloh on 28/2/17.
 */

public final class AccountHandler {
    static User user;
    static Boolean login;
    static Boolean returnUserActivityFromTestActivity = false;

    private AccountHandler(){
        user = null;
        login = false;
        returnUserActivityFromTestActivity = false;
    }

    public static void setLogin(Boolean bool){
        login = bool;
    }

    public static Boolean getLogin(){
        return login;
    }

    public static Boolean getReturnUserActivityFromTestActivity() {return returnUserActivityFromTestActivity;}

    public static User getUser() {
        return user;
    }

    public static void setUser(User newUser) {
        user = newUser;
    }

    public static void setReturnUserActivityFromTestActivity(Boolean bool){
        returnUserActivityFromTestActivity = bool;}
}
