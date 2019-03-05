package com.example.okcomputers.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public final String CHANNEL_ID = "personal_notification";
    public static final int NOTIFICATION_ID = 001;

    //for direct reply action in notification
    public static final String TAP_REPLY = "text_reply";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void DisplayNotification(View view) {

        createNotificationChannel();

        //tap on notification opens up activity
        Intent i = new Intent(this, LandingActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        //define intent using pending intent
        //param 1:context, 2:request_code 3:intent 4:flag

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);


        //creating action buttons yes and no and then add action button on builder.addActionButton
        //yes intent
        Intent yesintent = new Intent(this, yesActivity.class);
        yesintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //define intent using pending intent
        //param 1:context, 2:request_code 3:intent 4:flag
        PendingIntent yespendIntent = PendingIntent.getActivity(this, 0, yesintent, PendingIntent.FLAG_ONE_SHOT);

        //no intent
        Intent nointent = new Intent(this, yesActivity.class);
        nointent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //define intent using pending intent
        //param 1:context, 2:request_code 3:intent 4:flag
        PendingIntent nopendIntent = PendingIntent.getActivity(this, 0, nointent, PendingIntent.FLAG_ONE_SHOT);

        //second parameter is necessary i.e channel id from version 8.0 to display noti we need channel id as 2nd param
       final NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        // builder.setSmallIcon(R.drawable.sms_notify);
        builder.setSmallIcon(R.drawable.downlaod);//progressbar notify
        // builder.setContentTitle("Simple Notification");
        builder.setContentTitle("Image Downloading");//progressbar notify
        // builder.setContentText("This is a simple notification");
        builder.setContentText("Download in Progress");//progressbar notify
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT); //below 8.0 use set prioroty and above 8.0 use channel importance
        final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
//setting progress bar
        final int max_progress = 100;//progressbar notify
        int current_progress = 0;//progressbar notify
        builder.setProgress(max_progress, current_progress, false);//progressbar notify
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());

        //if we want to have progress bar process fast pass builder.setprogress above as 0,0,true and below after sleep remove two lines
        //progressbar notification
        Thread thread = new Thread() {
            @Override
            public void run() {
                int count = 0;
                try {
                    while (count <= 100) {
                        count = count + 10;
                        sleep(1000);
                        builder.setProgress(max_progress,count,false);
                        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                    }
                    builder.setContentText("Download Completed");
                    builder.setProgress(0,0,false);
                    notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();



      /*  builder.setAutoCancel(true); //when tap notification is cancelled.
        builder.setContentIntent(pendingIntent); //to open actv on tap
        builder.addAction(R.drawable.sms_notify,"Yes",yespendIntent); //logo, charsequence, pendingintent
        builder.addAction(R.drawable.sms_notify,"No",nopendIntent); //to close notification manualyy by tap yes or no goto there specific actv*/


        //on tap reply option notifcation code in if condition

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            RemoteInput remoteInput = new RemoteInput.Builder(TAP_REPLY).setLabel("Reply").build();

            Intent replyintent = new Intent(MainActivity.this,RemoteReciever.class);
            replyintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent replypendingIntent =  PendingIntent.getActivity(this,0,replyintent,PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.sms_notify,"Reply",replypendingIntent)
                    .addRemoteInput(remoteInput).build();

            builder.addAction(action);
        }
        //notify in lower than 8.0

    }

    //above than 8.0

    private void createNotificationChannel()
    {
        //version 8.0 or above
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            //specify channel name
            CharSequence channelname = "Personal Notifications";
            String channeldescp = "Include all the personal notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            //consist of 3 param : 1: channel_id 2: Channel_name 3: importance
            NotificationChannel notificationChannel =new NotificationChannel(CHANNEL_ID,channelname,importance);

            //set descp
            notificationChannel.setDescription(channeldescp);

            NotificationManager notificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            notificationManager.createNotificationChannel(notificationChannel);

        }
    }
}
