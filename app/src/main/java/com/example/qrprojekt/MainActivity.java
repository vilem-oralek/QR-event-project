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
    public void createEvent (View view){
        Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
        startActivity(intent);
    }
    public void joinEvent (View view){
        Intent intent = new Intent(MainActivity.this, JoinEventActivity.class);
        startActivity(intent);
    }

    private List<Event> loadEventsFromDatabase() {
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM events", null);

        if (cursor.moveToFirst()) {
            do {
                long eventId = cursor.getLong(cursor.getColumnIndexOrThrow("event_id"));
                String eventName = cursor.getString(cursor.getColumnIndexOrThrow("event_name"));
                String eventDescription = cursor.getString(cursor.getColumnIndexOrThrow("event_description"));

                events.add(new Event(eventId, eventName, eventDescription));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return events;
    }

    private class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
        private List<Event> eventList;

        public EventAdapter(List<Event> eventList) {
            this.eventList = eventList;
        }

        @NonNull
        @Override
        public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
            return new EventViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
            Event event = eventList.get(position);
            holder.textEventName.setText(event.getName());
            holder.textEventDescription.setText(event.getDescription());
            holder.textEventName.setGravity(Gravity.CENTER);
            holder.textEventDescription.setGravity(Gravity.CENTER);
            holder.textEventName.setTextSize(20);

            // otevření eventu po kliknutí
            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, JoinedEventActivity.class);
                intent.putExtra("event_id", event.getId());
                intent.putExtra("event_name", event.getName());
                intent.putExtra("event_description", event.getDescription());
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

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
        private long id;
        private String name;
        private String description;

        public Event(long id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }
    }
}