package com.example.qrprojekt;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class DBHelper extends SQLiteOpenHelper {

    // SQL query to create the Events table
    private static final String CREATE_EVENTS_TABLE =   //definování "Events" tabulky
            "CREATE TABLE events (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "event_name TEXT, " +
                    "event_description TEXT, " +
                    "event_id INTEGER NOT NULL UNIQUE);";   // unikátní event id pro připojení k eventu

    // SQL query to create the QR Codes table
    private static final String CREATE_QR_CODES_TABLE =     //definování "QR codes" tabulky
            "CREATE TABLE qr_codes (" +
                    "qr_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "qr_code TEXT UNIQUE, " +   // QR kód hodnota (string)
                    "qr_status TEXT, " +        // status QR kódu (použitý, nepoužitý)
                    "event_id INTEGER, " +      // Event ID k danému QR kódu
                    "FOREIGN KEY (event_id) REFERENCES events(event_id));"; // Cizí klíč k propojení tabulek

    public DBHelper(Context context) {
        super(context, "events.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // provedení SQL příkazů pro vytvoření tabulek
        db.execSQL(CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_QR_CODES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Dropnutí existujících tabulek a vytvoření nových
        db.execSQL("DROP TABLE IF EXISTS events");
        db.execSQL("DROP TABLE IF EXISTS qr_codes");
        onCreate(db);
    }

    public long addQrCode(SQLiteDatabase db, String qrCode, long eventId) {  // eventId je typu long
        ContentValues values = new ContentValues();
        values.put("qr_code", qrCode);
        values.put("qr_status", "active"); // Nastavení kódu jako aktivní
        values.put("event_id", eventId);   // event_id je typu long

        return db.insert("qr_codes", null, values);  // Vkládáme do tabulky qr_codes
    }
}