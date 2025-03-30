package com.example.qrprojekt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class JoinEventActivity extends AppCompatActivity {
    private EditText eventIdInput;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);

        eventIdInput = findViewById(R.id.editTextEventId);

        dbHelper = new DBHelper(this);

    }
    public void joinEvent(View view) { // onClick metoda - připojení k eventu
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String eventId = eventIdInput.getText().toString();

        if (!eventId.isEmpty()) {
            // kontrola event_id
            Cursor cursor = db.rawQuery("SELECT * FROM events WHERE event_id = ?", new String[]{eventId});

            if (cursor.moveToFirst()) {
                Toast.makeText(this, "Připojeno k eventu!", Toast.LENGTH_SHORT).show();

                // Předání názvu a popisu eventu do JoinedEvent
                Intent intent = new Intent(this, JoinedEventActivity.class);
                intent.putExtra("event_name", cursor.getString(cursor.getColumnIndexOrThrow("event_name")));
                intent.putExtra("event_description", cursor.getString(cursor.getColumnIndexOrThrow("event_description")));
                intent.putExtra("event_id", cursor.getLong(cursor.getColumnIndexOrThrow("event_id")));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Event ID neexistuje!", Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Zadejte event ID!", Toast.LENGTH_SHORT).show();
        }
    }
}