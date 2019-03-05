package com.example.okcomputers.notification;

import android.app.NotificationManager;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class RemoteReciever extends AppCompatActivity {


    //getting reply from notification
    private TextView txtview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_reciever);
        txtview = findViewById(R.id.textView3);
        Bundle remoteReply = RemoteInput.getResultsFromIntent(getIntent());

        if (remoteReply != null) {
            String message = remoteReply.getCharSequence(MainActivity.TAP_REPLY).toString();
            txtview.setText(message);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(MainActivity.NOTIFICATION_ID);
    }
}
