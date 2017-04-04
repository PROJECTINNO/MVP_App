package p5e610.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import p5e610.balance.User;

/**
 * Created by katelynweingart on 2/14/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;

    private static final String DATABASE_NAME = "user_data";
    private static final String USER_TABLE_NAME = "users";
    private static final String USER_NAME_COL = "name";
    private static final String USER_SURNAME_COL = "surname";
    private static final String USER_USERNAME_COL = "username";
    private static final String USER_EMAIL_COL = "email";
    private static final String USER_PASSWORD_COL = "password";
    private static final String USER_DOCTOR_COL = "is_doctor";
    private static final String USER_DATE_COL = "date_added";

    private static final String[] USER_COLUMNS = {USER_NAME_COL, USER_SURNAME_COL, USER_USERNAME_COL, USER_EMAIL_COL, USER_DOCTOR_COL};

    private static final int DATABASE_VERSION = 1;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
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

    private static Date getDateTime(String dateString) throws ParseException{
        return getDateFormat().parse(dateString);
    }

    public long addUser(String name, String surname, String username, String email, String password, boolean isDoctor) {
        SQLiteDatabase db = sInstance.getWritableDatabase();

        if (db == null) {
            throw new SQLiteCantOpenDatabaseException();
        }

        ContentValues row = new ContentValues();
        row.put(USER_NAME_COL, name);
        row.put(USER_SURNAME_COL, surname);
        row.put(USER_USERNAME_COL, username);
        row.put(USER_EMAIL_COL, email);
        row.put(USER_PASSWORD_COL, password.hashCode());
        row.put(USER_DOCTOR_COL, isDoctor);
        row.put(USER_DATE_COL, getDateString());

        return db.insert(USER_TABLE_NAME, null, row);
    }

    private void checkDatabaseConnection(SQLiteDatabase db) throws SQLiteCantOpenDatabaseException {
        if (db == null) {
            throw new SQLiteCantOpenDatabaseException();
        }
    }

    public boolean usernameTaken(String username) {
        return (queryUser(username) != null);
    }

    public boolean passwordMatches(String username, String password) {
        SQLiteDatabase db = sInstance.getWritableDatabase();
        checkDatabaseConnection(db);

        String selection = USER_PASSWORD_COL + "=? AND " + USER_USERNAME_COL + "=?";
        Integer pwHash = password.hashCode();
        String[] selectionArgs = {pwHash.toString(), username};

        String limit = "1";
        Cursor c = db.query(USER_TABLE_NAME, USER_COLUMNS, selection, selectionArgs, null, null, null, limit);
        boolean notEmpty = c.moveToFirst();

        c.close();
        return notEmpty;
    }

    private User queryUser(String selection, String[] selectionArgs) {
        SQLiteDatabase db = sInstance.getWritableDatabase();
        checkDatabaseConnection(db);

        String limit = "1";
        Cursor c = db.query(USER_TABLE_NAME, USER_COLUMNS, selection, selectionArgs, null, null, null, limit);

        return getUserFromCursor(c);
    }

    private User getUserFromCursor(Cursor cursor) {
        Map<String,String> res = new HashMap<>();
        if(!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        for(String col : USER_COLUMNS) {
            int i = cursor.getColumnIndex(col);
            res.put(col, cursor.getString(i));
        }

        cursor.close();
        return new User(res.get(USER_NAME_COL), res.get(USER_SURNAME_COL), res.get(USER_USERNAME_COL), res.get(USER_EMAIL_COL));
    }

    public User queryUser(Long id) {
        String selection = "id =?";
        String[] selectionArgs = {id.toString()};

        return queryUser(selection, selectionArgs);
    }

    public User queryUser(String username) {
        String selection = USER_USERNAME_COL + "=?";
        String[] selectionArgs = {username};

        return queryUser(selection, selectionArgs);
    }

    private void createUserTable(SQLiteDatabase db) {
        db.execSQL(
                "create table " + USER_TABLE_NAME + " " +
                        "(id integer primary key autoincrement," +
                        USER_NAME_COL + " text," +
                        USER_SURNAME_COL + " text," +
                        USER_USERNAME_COL + " text," +
                        USER_EMAIL_COL + " text," +
                        USER_PASSWORD_COL + " integer," +
                        USER_DOCTOR_COL + " integer," +
                        USER_DATE_COL + " datetime)"
        );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createUserTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
