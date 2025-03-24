package com.example.qrprojekt;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import com.journeyapps.barcodescanner.CompoundBarcodeView;

public class CreatedEventActivity extends AppCompatActivity {

    private CompoundBarcodeView barcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_event);

        TextView eventNameText = findViewById(R.id.textViewEventName);
        TextView eventDescriptionText = findViewById(R.id.textViewEventDescription);
        barcodeView = findViewById(R.id.barcodeScannerView);

        // Získání detailů eventu pomocí intentu
        String eventName = getIntent().getStringExtra("event_name");
        String eventDescription = getIntent().getStringExtra("event_description");

        if (eventName != null && eventDescription != null) {
            eventNameText.setText(eventName);
            eventDescriptionText.setText(eventDescription);
        }

        // Spuštění scanneru
        barcodeView.decodeContinuous(result -> {
            String scannedData = result.getText();
            Toast.makeText(this, "Naskenováno", Toast.LENGTH_SHORT).show();
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }
}