package p5e610.user;

import java.util.ArrayList;

/**
 * Created by mathiasloh on 2/5/17.
 */

public class Upload{
    public String url;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Upload() {
    }

    public Upload(String url) {
        this.url= url;
    }


    public String getUrl() {
        return url;
    }
}