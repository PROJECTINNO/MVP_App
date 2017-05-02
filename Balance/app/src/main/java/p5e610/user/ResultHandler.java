package p5e610.user;

import java.util.ArrayList;

/**
 * Created by mathiasloh on 2/5/17.
 */

public class ResultHandler {
    ArrayList<String> resultnames;

    public ResultHandler(){}

    public ResultHandler(ArrayList<String> resultnames){this.resultnames = resultnames;}

    public void addResultEntry(String result){
        resultnames.add(result);
    }

    public void removeResultEntry(String result){
        resultnames.remove(result);
    }

    public ArrayList<String> getResults(){
        return resultnames;
    }

    public void setResults(ArrayList<String> results){
        this.resultnames = results;
    }
}
