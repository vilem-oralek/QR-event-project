package com.example.qrprojekt;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "events.db";
    private static final int DATABASE_VERSION = 1;

    // SQL query to create the Events table
    private static final String CREATE_EVENTS_TABLE =
            "CREATE TABLE events (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +  // Auto-increment ID for internal usage
                    "event_name TEXT, " +                       // Event name
                    "event_description TEXT, " +                // Event description
                    "event_id INTEGER NOT NULL UNIQUE);";       // Custom long event_id for external use

    // SQL query to create the QR Codes table
    private static final String CREATE_QR_CODES_TABLE =
            "CREATE TABLE qr_codes (" +
                    "qr_id INTEGER PRIMARY KEY AUTOINCREMENT, " + // Auto-incremented QR code ID
                    "qr_code TEXT UNIQUE, " +                    // QR code value (string)
                    "qr_status TEXT, " +                         // Status of the QR code (e.g., active, used, blocked)
                    "event_id INTEGER, " +                       // Event ID associated with the QR code
                    "FOREIGN KEY (event_id) REFERENCES events(event_id));"; // Foreign key linking to the events table

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the SQL queries to create tables
        db.execSQL(CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_QR_CODES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables if they exist and create new ones
        db.execSQL("DROP TABLE IF EXISTS events");
        db.execSQL("DROP TABLE IF EXISTS qr_codes");
        onCreate(db);
    }
}