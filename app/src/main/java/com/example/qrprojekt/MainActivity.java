package com.example.qrprojekt;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);
        eventList = loadEventsFromDatabase();

        eventAdapter = new EventAdapter(eventList);
        recyclerView.setAdapter(eventAdapter);

    }
    public void createEvent (View view){ //onClick metoda - přesměrování na aktivitu vytvoření eventu
        Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
        startActivity(intent);
    }
    public void joinEvent (View view){ //onClick metoda - přesměrování na aktivitu připojení k eventu
        Intent intent = new Intent(MainActivity.this, JoinEventActivity.class);
        startActivity(intent);
    }

    private List<Event> loadEventsFromDatabase() { // Načte seznam eventů z databáze
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM events", null);

        if (cursor.moveToFirst()) {
            do {
                Long eventId = cursor.getLong(cursor.getColumnIndexOrThrow("event_id"));
                Long eventManagerId = cursor.getLong(cursor.getColumnIndexOrThrow("manager_event_id"));
                String eventName = cursor.getString(cursor.getColumnIndexOrThrow("event_name"));
                String eventDescription = cursor.getString(cursor.getColumnIndexOrThrow("event_description"));

                events.add(new Event(eventId,eventManagerId, eventName, eventDescription));
            } while (cursor.moveToNext());
        }
        cursor.close(); //zavření databáze
        return events;
    }

    // Adaptér pro RecyclerView, který zobrazí seznam eventů
    private class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
        private List<Event> eventList;

        public EventAdapter(List<Event> eventList) {
            this.eventList = eventList;
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Vytvoření vzhledu pro jednotlivé položky v seznamu
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            Event event = eventList.get(position);

            // Vypsání názvu a popisu eventu
            holder.textEventName.setText(event.getName());
            holder.textEventDescription.setText(event.getDescription());

            // Úprava vzhledu a atributů položek v seznamu
            holder.textEventName.setGravity(Gravity.CENTER);
            holder.textEventDescription.setGravity(Gravity.CENTER);
            holder.textEventName.setTextSize(20);
            holder.textEventDescription.setTextSize(16);
            holder.textEventName.setPadding(0,0,0,10);
            holder.textEventDescription.setPadding(0,20,20,10);
            holder.itemView.setBackgroundResource(R.drawable.item_border);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 25);
            holder.itemView.setLayoutParams(layoutParams);

            // otevření eventu po kliknutí a poslání dat intentem
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, CreatedEventActivity.class);
                intent.putExtra("event_id", event.getId());
                intent.putExtra("event_manager_id", event.getManagerId());
                intent.putExtra("event_name", event.getName());
                intent.putExtra("event_description", event.getDescription());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        } //vrácení počtu eventů v seznamu

        class EventViewHolder extends RecyclerView.ViewHolder {
            TextView textEventName, textEventDescription;

            public EventViewHolder(@NonNull View itemView) {
                super(itemView);
                textEventName = itemView.findViewById(android.R.id.text1);
                textEventDescription = itemView.findViewById(android.R.id.text2);
            }
        }
    }

    private class Event {
        private Long id;
        private Long managerId;
        private String name;
        private String description;

        // Třída Event pro uchování informací o eventu
        public Event(Long id, Long managerId, String name, String description) {
            this.managerId = managerId;
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public Long getId() {
            return id;
        }
        public Long getManagerId() {
            return managerId;
        }

        public String getName() {
            return name;
        }
        public String getDescription() {
            return description;
        }
    }
}