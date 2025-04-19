package com.example.a5_3_project_two_by_dawson_kennedy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;
import android.app.AlertDialog;

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
            // Show an explanation before requesting permission
            new AlertDialog.Builder(this)
                    .setTitle("SMS Permission Needed")
                    .setMessage("This app requires SMS permission to send notifications. Do you want to allow it?")
                    .setPositiveButton("Allow", (dialog, which) ->
                            ActivityCompat.requestPermissions(SMSPermissionActivity.this,
                                    new String[] {Manifest.permission.SEND_SMS}, SMS_PERMISSION_REQUEST_CODE))
                    .setNegativeButton("Deny", (dialog, which) ->
                            Toast.makeText(SMSPermissionActivity.this, "Permission denied. SMS won't be sent.", Toast.LENGTH_SHORT).show())
                    .show();
        } else {
            confirmAndSendSMS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantedResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantedResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantedResults.length > 0 && grantedResults[0] == PackageManager.PERMISSION_GRANTED) {
                confirmAndSendSMS();
            } else {
                Toast.makeText(this, "Permission denied. You can enable SMS permission in settings.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void confirmAndSendSMS() {
    new AlertDialog.Builder(this)
            .setTitle("Send SMS Notification")
            .setMessage("Are you sure you want to send an SMS notification?")
            .setPositiveButton("Send", (dialog, which) -> {
                // Logic to send SMS
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("Phone_Number", null, "Your alert message", null, null);
                Toast.makeText(SMSPermissionActivity.this, "SMS sent successfully!", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Cancel", (dialog, which) ->
                    Toast.makeText(SMSPermissionActivity.this, "SMS sending canceled.", Toast.LENGTH_SHORT).show())
            .show();
    }
}
