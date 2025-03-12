package com.example.qrprojekt;

import android.os.Bundle;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CreateEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Find views
        EditText eventNameEditText = findViewById(R.id.editTextText);
        EditText eventDescriptionEditText = findViewById(R.id.editTextText2);
        Button createEventButton = findViewById(R.id.button3);

        // Initialize DBHelper and get writable database
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Set onClickListener for Create Event button
        createEventButton.setOnClickListener(v -> {
            // Get user input from EditText
            String eventName = eventNameEditText.getText().toString();
            String eventDescription = eventDescriptionEditText.getText().toString();

            // Check if both fields are not empty
            if (!eventName.isEmpty() && !eventDescription.isEmpty()) {
                // Generate a random unique event ID (large number)
                long eventId = generateRandomEventId();

                // Vytvoření ContentValues na ukládání dat
                ContentValues values = new ContentValues();
                values.put("event_name", eventName);
                values.put("event_description", eventDescription);
                values.put("event_id", eventId); // ukládání dat

                // vložení dat do databáze
                long insertedRowId = db.insert("events", null, values);

                if (insertedRowId != -1) {
                    // Event inserted successfully
                    Toast.makeText(this, "Event Created with ID: " + eventId, Toast.LENGTH_LONG).show();
                } else {
                    // Event insertion failed
                    Toast.makeText(this, "Failed to create event", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle empty fields
                Toast.makeText(this, "Please enter both event name and description", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private long generateRandomEventId() { //funkce generování ID pro připojení k eventu
        // Gennerování náhodného ID = 0 - max hodnota longu
        return (long) (Math.random() * Long.MAX_VALUE);
    }
}