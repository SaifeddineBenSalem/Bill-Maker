package com.example.hello_android3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RequestsManager {
    private Context context;
    private DBHandler dbHandler;

    public RequestsManager(Context context) {
        this.context = context;
        dbHandler = new DBHandler(context);
    }

    public int addRequest(String name, String descriptionS, String country, String type, String fileName, int poster, String status) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RequestColumns.NAME, name);
        values.put(RequestColumns.DESCRIPTION, descriptionS);
        values.put(RequestColumns.COUNTRY, country);
        values.put(RequestColumns.TYPE, type);
        values.put(RequestColumns.PHOTO, fileName);
        values.put(RequestColumns.POSTER, poster);
        values.put(RequestColumns.STATUS, status);

        try {
            long id = db.insert(RequestColumns.TABLE_NAME, null, values);
            if (id == -1) {
                // Insertion failed
                Log.e("DBInsertError", "Error inserting data into the database");
                // You can also throw a custom exception or handle the error here
            } else {
                // Insertion successful
                Log.d("DBInsertSuccess", "Inserted row ID: " + id);
            }
            return (int) id;
        } catch (SQLiteException e) {
            // Handle SQLite exceptions
            Log.e("DBInsertException", "Error inserting data: " + e.getMessage());
            e.printStackTrace(); // Print the stack trace for debugging
            // You can also throw a custom exception or handle the error here
            return -1; // or any other error code to indicate failure
        } finally {
            // Close the database connection if needed
            // db.close();
        }
    }

    public boolean updatePhoto(int id, String name){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RequestColumns.PHOTO, name);
        int rowsAffected = db.update(RequestColumns.TABLE_NAME, values, "id=?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }
    public Request[] getRequestByStatus (String status1){
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        List<Request> pendingRequestsList = new ArrayList<>();
        String query = "SELECT * FROM " + RequestColumns.TABLE_NAME + " WHERE " + RequestColumns.STATUS + " = '"+status1+"'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(RequestColumns.ID_COL));
                String name = cursor.getString(cursor.getColumnIndex(RequestColumns.NAME));
                String description = cursor.getString(cursor.getColumnIndex(RequestColumns.DESCRIPTION));
                String photo = cursor.getString(cursor.getColumnIndex(RequestColumns.PHOTO));
                String type = cursor.getString(cursor.getColumnIndex(RequestColumns.TYPE));
                String status = cursor.getString(cursor.getColumnIndex(RequestColumns.STATUS));
                String postingDate = cursor.getString(cursor.getColumnIndex(RequestColumns.POSTING_DATE));
                String country = cursor.getString(cursor.getColumnIndex(RequestColumns.COUNTRY));
                int poster = cursor.getInt(cursor.getColumnIndex(RequestColumns.POSTER));
                int actionBy = cursor.getInt(cursor.getColumnIndex(RequestColumns.ACTION_BY));

                Request request = new Request(id, name, description, photo, type, status, postingDate, country, poster, actionBy);
                pendingRequestsList.add(request);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Convert the list to an array
        Request[] pendingRequestsArray = new Request[pendingRequestsList.size()];
        pendingRequestsArray = pendingRequestsList.toArray(pendingRequestsArray);

        return pendingRequestsArray;
    }
    public boolean updateStatus(int id,String status,int adminId){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RequestColumns.STATUS, status);
        values.put(RequestColumns.ACTION_BY,adminId);
        int rowsAffected = db.update(RequestColumns.TABLE_NAME, values,
                UserColumns.ID_COL + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }
    public Request getRequestById(int requestId) {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Request request = null;
        String query = "SELECT * FROM " + RequestColumns.TABLE_NAME + " WHERE " +
                RequestColumns.ID_COL + " = ?";
        String[] selectionArgs = {String.valueOf(requestId)};
        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(RequestColumns.ID_COL));
            String name = cursor.getString(cursor.getColumnIndex(RequestColumns.NAME));
            String description = cursor.getString(cursor.getColumnIndex(RequestColumns.DESCRIPTION));
            String photo = cursor.getString(cursor.getColumnIndex(RequestColumns.PHOTO));
            String type = cursor.getString(cursor.getColumnIndex(RequestColumns.TYPE));
            String status = cursor.getString(cursor.getColumnIndex(RequestColumns.STATUS));
            String postingDate = cursor.getString(cursor.getColumnIndex(RequestColumns.POSTING_DATE));
            String country = cursor.getString(cursor.getColumnIndex(RequestColumns.COUNTRY));
            int poster = cursor.getInt(cursor.getColumnIndex(RequestColumns.POSTER));
            int actionBy = cursor.getInt(cursor.getColumnIndex(RequestColumns.ACTION_BY));

            request = new Request(id, name, description, photo, type, status, postingDate, country, poster, actionBy);
        }

        if (cursor != null) {
            cursor.close();
        }

        return request;
    }

    public void updateARequest(int requestId, String name, String descriptionS, String country, String type, String fileName, int poster, String status, int actionBy) {
            SQLiteDatabase db = dbHandler.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(RequestColumns.NAME, name);
            values.put(RequestColumns.DESCRIPTION, descriptionS);
            values.put(RequestColumns.COUNTRY, country);
            values.put(RequestColumns.TYPE, type);
            values.put(RequestColumns.PHOTO, fileName);
            values.put(RequestColumns.POSTER, poster);
            values.put(RequestColumns.STATUS, status);
            values.put(RequestColumns.ACTION_BY, actionBy);
            String selection = RequestColumns.ID_COL + " = ?";
            String[] selectionArgs = { String.valueOf(requestId) };
            db.update(RequestColumns.TABLE_NAME, values, selection, selectionArgs);
            db.close();
        }
    }
