package com.example.qrprojekt;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;

import com.journeyapps.barcodescanner.CompoundBarcodeView;

public class CreatedEventActivity extends AppCompatActivity {

    private CompoundBarcodeView barcodeView;
    private DBHelper dbHelper;
    private Long managerEventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_event);

        dbHelper = new DBHelper(this);

        TextView eventNameText = findViewById(R.id.textViewEventName);
        TextView eventDescriptionText = findViewById(R.id.textViewEventDescription);
        TextView eventIdText = findViewById(R.id.textViewEventId);
        barcodeView = findViewById(R.id.barcodeScannerView);

        // Získání detailů eventu pomocí intentu
        String eventName = getIntent().getStringExtra("event_name");
        String eventDescription = getIntent().getStringExtra("event_description");
        Long eventID = getIntent().hasExtra("event_id") ? getIntent().getLongExtra("event_id", -1) : null;
        managerEventId = getIntent().hasExtra("event_manager_id") ? getIntent().getLongExtra("event_manager_id", -1) : -1;

        if (eventName != null && eventDescription != null) {
            eventNameText.setText(eventName);
            eventDescriptionText.setText(eventDescription);
            eventIdText.setText("ID: " + eventID);
        }

        // Spuštění scanneru
        barcodeView.decodeContinuous(result -> {
            String scannedData = result.getText();
            barcodeView.pause();
            validateQRCode(scannedData);
            new android.os.Handler().postDelayed(() -> barcodeView.resume(), 3000);
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        }
    }
    private void validateQRCode(String qrCode) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT qr_status FROM qr_codes WHERE qr_code = ?", new String[]{qrCode});

        if (cursor.moveToFirst()) {
            String qrStatus = cursor.getString(cursor.getColumnIndexOrThrow("qr_status"));
            cursor.close();

            if (qrStatus.equals("active")) { // QR is active
                Toast.makeText(this, "Validní QR kód", Toast.LENGTH_SHORT).show();
                deactivateQRCode(qrCode);
            } else {
                Toast.makeText(this, "NEVALIDNÍ QR KÓD", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "NEVALIDNÍ QR KÓD", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

    private void deactivateQRCode(String qrCode) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("qr_status", "inactive");
        db.update("qr_codes", values, "qr_code = ?", new String[]{qrCode});
        db.close();
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
    public void copyManagerId(View view) {
        if (managerEventId != null && managerEventId != -1) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Manager Event ID", String.valueOf(managerEventId)); // Convert to String
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "ID zkopírováno!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Chyba: ID není dostupné!", Toast.LENGTH_SHORT).show();
        }
    }
}