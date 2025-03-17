package com.example.qrprojekt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class JoinEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);

        EditText eventIdInput = findViewById(R.id.editTextEventId);
        Button joinEventButton = findViewById(R.id.buttonJoinEvent);

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        joinEventButton.setOnClickListener(v -> {
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
                    intent.putExtra("event_id", eventId);  // PŘIDEJ event_id !!!
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Event ID neexistuje!", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            } else {
                Toast.makeText(this, "Zadejte event ID!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}