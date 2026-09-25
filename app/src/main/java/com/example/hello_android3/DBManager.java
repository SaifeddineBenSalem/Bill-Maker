package com.example.hello_android3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private Context context;
    private DBHandler dbHandler;

    public DBManager(Context context) {
        this.context = context;
        dbHandler = new DBHandler(context);

    }

    public boolean loginProcess(String email, String password) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {UserColumns.ID_COL};

        // Define the selection criteria
        String selection = UserColumns.EMAIL + " = ? AND " + UserColumns.PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        // Query the database to check if the user exists
        Cursor cursor = db.query(UserColumns.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        // Check if the cursor has any rows (if user exists)
        boolean loggedIn = cursor != null && cursor.getCount() > 0;

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return loggedIn;
    }

    public boolean findByEmail(String email) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {UserColumns.ID_COL};

        // Define the selection criteria
        String selection = UserColumns.EMAIL + " = ?";
        String[] selectionArgs = {email};

        // Query the database to check if the user exists
        Cursor cursor = db.query(UserColumns.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        // Check if the cursor has any rows (if user exists)
        boolean emailExist = cursor != null && cursor.getCount() > 0;

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return emailExist;
    }

    public int getIdByEmail(String email) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {UserColumns.ID_COL};

        // Define the selection criteria
        String selection = UserColumns.EMAIL + " = ?";
        String[] selectionArgs = {email};

        // Query the database to check if the user exists
        Cursor cursor = db.query(UserColumns.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            // Get the index of the ID column
            int idIndex = cursor.getColumnIndex(UserColumns.ID_COL);

            // Retrieve the ID value from the cursor
            int userId = cursor.getInt(idIndex);

            // Close the cursor to release resources
            cursor.close();
            return userId;
        } else
            return -1;
    }

    public int registrationProcess(String firstName, String lastName, String email, String password, String country, String phoneNumber, String securityQuestion, String securityAnswer, String dateOfBirth, String profilePhoto, String gender) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserColumns.FIRST_NAME, firstName);
        values.put(UserColumns.LAST_NAME, lastName);
        values.put(UserColumns.EMAIL, email);
        values.put(UserColumns.PASSWORD, password);
        values.put(UserColumns.COUNTRY, country);
        values.put(UserColumns.PHONE_NUMBER, phoneNumber);
        values.put(UserColumns.SECURITY_QUESTION, securityQuestion);
        values.put(UserColumns.SECURITY_ANSWER, securityAnswer);
        values.put(UserColumns.DATE_OF_BIRTH, dateOfBirth);
        values.put(UserColumns.PROFILE_PHOTO, profilePhoto);
        values.put(UserColumns.GENDER, gender);
        values.put(UserColumns.FIRST_LOGIN, 1);
        db.insert(UserColumns.TABLE_NAME, null, values);
        db.close();
        return getIdByEmail(email);
    }

    public boolean updateProfilePhoto(int userId, String imagePath) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserColumns.PROFILE_PHOTO, imagePath);
        int rowsAffected = db.update(UserColumns.TABLE_NAME, values, "id=?", new String[]{String.valueOf(userId)});
        db.close();
        return rowsAffected > 0;
    }
    public void setAdmin(int userId) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserColumns.ADMIN, 1);
        int rowsAffected = db.update(UserColumns.TABLE_NAME, values, "id=?", new String[]{String.valueOf(userId)});
        db.close();
    }
    public static String getStringFromCursor(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return (columnIndex != -1) ? cursor.getString(columnIndex) : null;
    }

    public static int getIntFromCursor(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return (columnIndex != -1 && !cursor.isNull(columnIndex)) ? cursor.getInt(columnIndex) : -1; // Default value if null or not found
    }

    public User getUserByEmail(String email) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        User user = null;
        String query = "SELECT * FROM " + UserColumns.TABLE_NAME + " WHERE " +
                UserColumns.EMAIL + " = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            int id = getIntFromCursor(cursor, UserColumns.ID_COL);
            String firstName = getStringFromCursor(cursor, UserColumns.FIRST_NAME);
            String lastName = getStringFromCursor(cursor, UserColumns.LAST_NAME);
            String password = getStringFromCursor(cursor, UserColumns.PASSWORD);
            String dateOfBirth = getStringFromCursor(cursor, UserColumns.DATE_OF_BIRTH);
            String country = getStringFromCursor(cursor, UserColumns.COUNTRY);
            String phoneNumber = getStringFromCursor(cursor, UserColumns.PHONE_NUMBER);
            String securityQuestion = getStringFromCursor(cursor, UserColumns.SECURITY_QUESTION);
            String securityAnswer = getStringFromCursor(cursor, UserColumns.SECURITY_ANSWER);
            String profilePhoto = getStringFromCursor(cursor, UserColumns.PROFILE_PHOTO);
            String gender = getStringFromCursor(cursor, UserColumns.GENDER);
            int admin = getIntFromCursor(cursor, UserColumns.ADMIN);
            int banned = getIntFromCursor(cursor, UserColumns.BANNED);
            int firstLogin = getIntFromCursor(cursor, UserColumns.FIRST_LOGIN);
            user = new User(id, firstName, lastName, email, password, dateOfBirth,
                    country, phoneNumber, securityQuestion, securityAnswer, profilePhoto, gender, admin, banned, firstLogin);

            if (cursor != null) {
                cursor.close();
            }
        }

        return user;
    }
    public User getUserById(Integer userId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        User user = null;
        String query = "SELECT * FROM " + UserColumns.TABLE_NAME + " WHERE " +
                UserColumns.ID_COL + " = ?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            String email  = getStringFromCursor(cursor, UserColumns.EMAIL);
            String firstName = getStringFromCursor(cursor, UserColumns.FIRST_NAME);
            String lastName = getStringFromCursor(cursor, UserColumns.LAST_NAME);
            String password = getStringFromCursor(cursor, UserColumns.PASSWORD);
            String dateOfBirth = getStringFromCursor(cursor, UserColumns.DATE_OF_BIRTH);
            String country = getStringFromCursor(cursor, UserColumns.COUNTRY);
            String phoneNumber = getStringFromCursor(cursor, UserColumns.PHONE_NUMBER);
            String securityQuestion = getStringFromCursor(cursor, UserColumns.SECURITY_QUESTION);
            String securityAnswer = getStringFromCursor(cursor, UserColumns.SECURITY_ANSWER);
            String profilePhoto = getStringFromCursor(cursor, UserColumns.PROFILE_PHOTO);
            String gender = getStringFromCursor(cursor, UserColumns.GENDER);
            int admin = getIntFromCursor(cursor, UserColumns.ADMIN);
            int banned = getIntFromCursor(cursor, UserColumns.BANNED);
            int firstLogin = getIntFromCursor(cursor, UserColumns.FIRST_LOGIN);
            user = new User(userId, firstName, lastName, email, password, dateOfBirth,
                    country, phoneNumber, securityQuestion, securityAnswer, profilePhoto, gender,admin,banned,firstLogin);
        }

        if (cursor != null) {
            cursor.close();
        }

        return user;
    }

    public boolean updateUser(String firstName, String lastName, String email, String oldPassword,
                              String country, String birthday, String number, String gender,
                              String securityQuestion, String securityQuestionText,int id) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserColumns.FIRST_NAME, firstName);
        values.put(UserColumns.LAST_NAME, lastName);
        values.put(UserColumns.EMAIL, email);
        values.put(UserColumns.GENDER, gender);
        values.put(UserColumns.PASSWORD, oldPassword);
        values.put(UserColumns.DATE_OF_BIRTH, birthday);
        values.put(UserColumns.COUNTRY, country);
        values.put(UserColumns.PHONE_NUMBER, number);
        values.put(UserColumns.SECURITY_QUESTION, securityQuestion);
        values.put(UserColumns.SECURITY_ANSWER, securityQuestionText);
        int rowsAffected = db.update(UserColumns.TABLE_NAME, values,
                UserColumns.ID_COL + " = ?",
                new String[]{String.valueOf(id)});
        db.close();

        return rowsAffected > 0;
    }

    public boolean banAUser(int id) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserColumns.BANNED,1);
        int rowsAffected = db.update(UserColumns.TABLE_NAME, values,
                UserColumns.ID_COL + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }
    public  boolean unBan(int id){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserColumns.BANNED,0);
        int rowsAffected = db.update(UserColumns.TABLE_NAME, values,
                UserColumns.ID_COL + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }
    public  boolean updateFirstLogin(int id){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserColumns.FIRST_LOGIN,0);
        int rowsAffected = db.update(UserColumns.TABLE_NAME, values,
                UserColumns.ID_COL + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }
    public User[] getAllUsers() {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        User[] users = null;
        String query = "SELECT * FROM " + UserColumns.TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            users = new User[cursor.getCount()];
            int index = 0;
            do {
                int userId = cursor.getInt(cursor.getColumnIndex(UserColumns.ID_COL));
                String email  = cursor.getString(cursor.getColumnIndex(UserColumns.EMAIL));
                String firstName = cursor.getString(cursor.getColumnIndex(UserColumns.FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndex(UserColumns.LAST_NAME));
                String password = cursor.getString(cursor.getColumnIndex(UserColumns.PASSWORD));
                String dateOfBirth = cursor.getString(cursor.getColumnIndex(UserColumns.DATE_OF_BIRTH));
                String country = cursor.getString(cursor.getColumnIndex(UserColumns.COUNTRY));
                String phoneNumber = cursor.getString(cursor.getColumnIndex(UserColumns.PHONE_NUMBER));
                String securityQuestion = cursor.getString(cursor.getColumnIndex(UserColumns.SECURITY_QUESTION));
                String securityAnswer = cursor.getString(cursor.getColumnIndex(UserColumns.SECURITY_ANSWER));
                String profilePhoto = cursor.getString(cursor.getColumnIndex(UserColumns.PROFILE_PHOTO));
                String gender = cursor.getString(cursor.getColumnIndex(UserColumns.GENDER));
                int admin = cursor.getInt(cursor.getColumnIndex(UserColumns.ADMIN));
                int banned = cursor.getInt(cursor.getColumnIndex(UserColumns.BANNED));
                int firstLogin = cursor.getInt(cursor.getColumnIndex(UserColumns.FIRST_LOGIN));
                User user = new User(userId, firstName, lastName, email, password, dateOfBirth,
                        country, phoneNumber, securityQuestion, securityAnswer, profilePhoto, gender, admin, banned,firstLogin);
                users[index++] = user;
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return users;
    }


}
