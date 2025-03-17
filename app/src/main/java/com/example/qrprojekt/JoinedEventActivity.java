package com.example.qrprojekt;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.UUID;

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
        long eventId = getIntent().getLongExtra("event_id", -1);

        if (eventName != null && eventDescription != null && eventId != -1) {
            // přepsaní názvu a popisu
            eventNameText.setText(eventName);
            eventDescriptionText.setText(eventDescription);

            // Vložení QR kódu do databáze
            String uniqueQrCode = generateUniqueQrCode();
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            long qrId = dbHelper.addQrCode(db, uniqueQrCode, eventId);

            if (qrId != -1) {
                // Generování a zobrazení QR kódu
                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(uniqueQrCode, BarcodeFormat.QR_CODE, 300, 300);
                    qrCodeImage.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "QR kód se nepodařilo vytvořit", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Chyba při ukládání QR kódu", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Chyba při načítání eventu", Toast.LENGTH_SHORT).show();
        }
    }

    // Metoda pro generování unikátního QR kódu
    private String generateUniqueQrCode() {
        // Generování náhodného UUID
        return UUID.randomUUID().toString();
    }
}