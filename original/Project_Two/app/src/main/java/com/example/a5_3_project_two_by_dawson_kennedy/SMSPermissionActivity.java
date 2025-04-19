package com.example.a5_3_project_two_by_dawson_kennedy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SMSPermissionActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_permission);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("SMS Permission");

        findViewById(R.id.requestPermissionButton).setOnClickListener(v -> requestSMSPermission());

    }

    private void requestSMSPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE);
        } else {
            sendSMSNotification();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantedResults) {
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantedResults.length > 0 && grantedResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMSNotification();
            } else {
                Toast.makeText(this, "Permission denied. SMS notifications won't be sent.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendSMSNotification() {
        // logic to send SMS
        // SmsManager smsManager = SmsManager.getDefault();
        // smsManager.sendTextMessage("PHONE_NUMBER", null, "Your alert message", null, null);
        Toast.makeText(this, "SMS sent", Toast.LENGTH_SHORT).show();
    }
}
