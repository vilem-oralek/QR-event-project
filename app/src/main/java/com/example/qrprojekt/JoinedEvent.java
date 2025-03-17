package com.example.qrprojekt;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class JoinedEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_event);

        TextView eventNameTextView = findViewById(R.id.textViewEventName);
        TextView eventDescriptionTextView = findViewById(R.id.textViewEventDescription);

        // Získání dat z intentu
        String eventName = getIntent().getStringExtra("event_name");
        String eventDescription = getIntent().getStringExtra("event_description");

        // Zobrazení dat v TextView
        eventNameTextView.setText(eventName);
        eventDescriptionTextView.setText(eventDescription);
    }
}