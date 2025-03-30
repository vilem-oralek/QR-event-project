package com.example.qrprojekt;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class CreateEventActivity extends AppCompatActivity {

    private EditText eventNameEditText;
    private EditText eventDescriptionEditText;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        eventNameEditText = findViewById(R.id.editTextEventName);
        eventDescriptionEditText = findViewById(R.id.editTextEventDescription);
        dbHelper = new DBHelper(this);
    }

    public void createEvent(View view) { // onClick metoda - vytvoření eventu
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Získání uživatelského vstupu
        String eventName = eventNameEditText.getText().toString();
        String eventDescription = eventDescriptionEditText.getText().toString();

        // Kontrola, zda pole nejsou prázdná
        if (!eventName.isEmpty() && !eventDescription.isEmpty()) {
            // Generování náhodného unikátního ID eventu a manager ID
            Long eventId = generateRandomId();
            Long managerEventId = generateRandomId();

            // Vytvoření ContentValues na ukládání dat
            ContentValues values = new ContentValues();
            values.put("event_name", eventName);
            values.put("event_description", eventDescription);
            values.put("event_id", eventId);
            values.put("manager_event_id", managerEventId);

            // Vložení dat do databáze
            Long insertedRowId = db.insert("events", null, values);

            if (insertedRowId != -1) {
                Toast.makeText(this, "Event vytvořen, ID: " + eventId, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Selhání při vytváření eventu", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Prosím zadejte název i popis", Toast.LENGTH_SHORT).show();
        }
    }

    public void joinCreatedEvent(View view) { // onClick metoda - připojení k vytvořenému eventu
        EditText createdEventIdInput = findViewById(R.id.editTextCreatedEventId);
        String managerEventId = createdEventIdInput.getText().toString();

        if (!managerEventId.isEmpty()) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM events WHERE manager_event_id = ?", new String[]{managerEventId});

            if (cursor.moveToFirst()) {
                Intent intent = new Intent(this, CreatedEventActivity.class);
                intent.putExtra("event_id", cursor.getLong(cursor.getColumnIndexOrThrow("event_id")));
                intent.putExtra("event_name", cursor.getString(cursor.getColumnIndexOrThrow("event_name")));
                intent.putExtra("event_description", cursor.getString(cursor.getColumnIndexOrThrow("event_description")));
                startActivity(intent);
            } else {
                Toast.makeText(this, "ID eventu neexistuje!", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Zadejte ID vytvořeného eventu!", Toast.LENGTH_SHORT).show();
        }
    }

    private long generateRandomId() { //funkce generování ID pro připojení k eventu
        // Gennerování náhodného ID = 0 až max hodnota longu
        return (long) (Math.random() * Long.MAX_VALUE);
    }
}