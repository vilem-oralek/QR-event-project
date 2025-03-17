package com.example.qrprojekt;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class JoinedEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_event);

        TextView eventNameText = findViewById(R.id.textViewEventName);
        TextView eventDescriptionText = findViewById(R.id.textViewEventDescription);
        ImageView qrCodeImage = findViewById(R.id.qrCodeImageView);

        // Získání dat z intentu
        String eventName = getIntent().getStringExtra("event_name");
        String eventDescription = getIntent().getStringExtra("event_description");
        String eventId = getIntent().getStringExtra("event_id");  // ID eventu


        if (eventName != null && eventDescription != null && eventId != null) {
            eventNameText.setText(eventName);
            eventDescriptionText.setText(eventDescription);

            // Vygenerování QR kódu
            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(eventId, BarcodeFormat.QR_CODE, 300, 300);
                qrCodeImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "QR kód se nepodařilo vytvořit", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Chyba při načítání eventu", Toast.LENGTH_SHORT).show();
        }
    }
}