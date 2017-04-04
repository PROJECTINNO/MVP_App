package p5e610.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import p5e610.balance.Doctor;
import p5e610.balance.Patient;
import p5e610.balance.User;

/**
 * Created by katelynweingart on 3/28/17.
 */

public class HerokuDatabaseHelper {
    private static HerokuDatabaseHelper sInstance;
    private static Context context;

    private static final String DATABASE_URL =
            "postgres://ngkmaxuijzenoj:2030be4a3342a33f90ec40d801ca82560046c603d4a7b99d347444d" +
                    "89aa0ec39@ec2-54-221-244-196.compute-1.amazonaws.com:5432/dbeo6e7a6ru1uu";
    private static final String DATABASE_NAME = "user_data";
    private static final String USER_TABLE_NAME = "users";
    private static final String PATIENT_DOCTOR_TABLE_NAME = "doctor_to_patient";
    private static final String USER_NAME_COL = "name";
    private static final String USER_SURNAME_COL = "surname";
    private static final String USER_USERNAME_COL = "username";
    private static final String USER_EMAIL_COL = "email";
    private static final String USER_PASSWORD_COL = "password";
    private static final String USER_DOCTOR_COL = "is_doctor";
    private static final String USER_DATE_COL = "date_added";

    private static final String[] USER_COLUMNS = {USER_NAME_COL, USER_SURNAME_COL, USER_USERNAME_COL, USER_EMAIL_COL, USER_DOCTOR_COL};

    private static final int DATABASE_VERSION = 1;

    public static synchronized HerokuDatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new HerokuDatabaseHelper(context.getApplicationContext());
        }

        return sInstance;
    }


    private static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    }

    private static String getDateString() {
        Date date = new Date();
        return getDateFormat().format(date);
    }

    private static Date getDateFromString(String dateString) throws ParseException {
        return getDateFormat().parse(dateString);
    }

    public long addUser(String name, String surname, String username, String email, String password, boolean isDoctor)
            throws Exception {
        String sqlQuery = String.format("INSERT INTO %s (%s, %s,  %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?, ?)",
                USER_TABLE_NAME, USER_NAME_COL, USER_SURNAME_COL,  USER_USERNAME_COL, USER_EMAIL_COL,
                USER_PASSWORD_COL, USER_DOCTOR_COL, USER_DATE_COL);

        Connection conn = getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1,  name);
        pstmt.setString(2, surname);
        pstmt.setString(3, username);
        pstmt.setString(4, email);
        pstmt.setInt(5, password.hashCode());
        pstmt.setBoolean(6, isDoctor);
        pstmt.setString(7, getDateString());
        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();
        rs.next();
        int auto_id = rs.getInt(1);

        return auto_id;
    }

    public boolean usernameTaken(String username) throws URISyntaxException, SQLException {
        return (queryUser(username) != null);
    }

    public boolean passwordMatches(String username, String password) throws URISyntaxException, SQLException {
        Connection conn = getConnection();
        String sqlQuery = String.format("SELECT COUNT(*) AS total FROM %s WHERE %s=? AND %s=? LIMIT 1",
                                USER_TABLE_NAME,  USER_PASSWORD_COL,  USER_USERNAME_COL);
        PreparedStatement pstmt =  conn.prepareStatement(sqlQuery);
        pstmt.setInt(1,  password.hashCode());
        pstmt.setString(2, username);
        pstmt.execute();
        ResultSet results = pstmt.getResultSet();
        int count = results.getInt("total");

        return count > 0;
    }

    public User queryUser(String username) throws URISyntaxException, SQLException {
        Connection conn = getConnection();
        String sqlQuery = String.format("SELECT * FROM %s WHERE username=? LIMIT 1", USER_TABLE_NAME);
        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
        pstmt.setString(1, username);
        pstmt.executeQuery();
        return getUser(pstmt);
    }

    public User queryUser(Integer id) throws URISyntaxException, SQLException {
        Connection conn = getConnection();
        String sqlQuery = String.format("SELECT * FROM %s WHERE user_id=? LIMIT 1", USER_TABLE_NAME);
        PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
        pstmt.setInt(1, id);
        pstmt.executeQuery();
        return getUser(pstmt);
    }

    public User getUser(PreparedStatement pstmt) throws SQLException {
        ResultSet rst = pstmt.getResultSet();
        if(rst.next()) {
            String username = rst.getString(USER_USERNAME_COL);
            String name = rst.getString(USER_NAME_COL);
            String email = rst.getString(USER_EMAIL_COL);
            String surname = rst.getString(USER_SURNAME_COL);
            boolean isDoctor = rst.getBoolean(USER_DOCTOR_COL);

            if(isDoctor)  {
                return new Doctor(name, surname, username, email, new ArrayList<Patient>());
            } else {
                return new Patient(name, surname,  username,  email);
            }
        } else {
            return null;
        }
    }

    private void createUserTable(Connection conn) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("create table " + USER_TABLE_NAME + " " +
                "(user_id integer primary key autoincrement," +
                USER_NAME_COL + " text," +
                USER_SURNAME_COL + " text," +
                USER_USERNAME_COL + " text," +
                USER_EMAIL_COL + " text," +
                USER_PASSWORD_COL + " integer," +
                USER_DOCTOR_COL + " integer," +
                USER_DATE_COL + " datetime)"
        );

        pstmt.executeQuery();
    }

    private void createPatientDoctorTable(Connection conn) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("create table " + PATIENT_DOCTOR_TABLE_NAME + " " +
                "(id integer primary key autoincrement, " +
                "FOREIGN KEY(patient_id) REFERENCES "  + USER_TABLE_NAME  + "(user_id), " +
                "FOREIGN KEY(doctor_id) REFERENCES " + USER_TABLE_NAME + "(user_id))");

        pstmt.executeQuery();
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    private HerokuDatabaseHelper(Context context) {
        this.context = context;
    }
    private static Connection getConnection() throws URISyntaxException, SQLException {
//        String dbUrl = System.getenv(DATABASE_URL);
        return DriverManager.getConnection(DATABASE_URL);
    }
}
