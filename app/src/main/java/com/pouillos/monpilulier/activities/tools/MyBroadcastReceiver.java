package com.pouillos.monpilulier.activities.tools;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.pouillos.monpilulier.R;
import com.pouillos.monpilulier.activities.MainActivity;

import java.util.Date;


public class MyBroadcastReceiver extends BroadcastReceiver {
    MediaPlayer mp;
    private NotificationCompat.Builder notBuilder;
    private static final int MY_NOTIFICATION_ID = 12345;
    private static final int MY_REQUEST_CODE = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        mp=MediaPlayer.create(context, R.raw.alarm);
        mp.start();
        Toast.makeText(context, "Alarm....", Toast.LENGTH_LONG).show();
        //if (intent.hasExtra("activity")) {
            //createNotificationChannel(context);

           // this.notBuilder = new NotificationCompat.Builder(context, "notifTest");
            // The message will automatically be canceled when the user clicks on Panel
          // this.notBuilder.setAutoCancel(true);
        //    Class classe = (Class) intent.getSerializableExtra("activity");
         //   MainActivity.startNotification();
        //}

    }

    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //CharSequence name = getString(R.string.channel_name);
            CharSequence name = "channelName";
            //String description = getString(R.string.channel_description);
            String description = "channelDescription";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifTest", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void startNotification(Context context,Class classe) {
        // --------------------------
        // Prepare a notification
        // --------------------------

        this.notBuilder.setSmallIcon(R.mipmap.ic_launcher);
        this.notBuilder.setTicker("This is a ticker");
        this.notBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Set the time that the event occurred.
        // Notifications in the panel are sorted by this time.

        //Date dateJour = new Date();
        Date dateNotif = new Date();
        Long  longNotif = dateNotif.getTime();

        //  this.notBuilder.setWhen(new DateUtils().ajouterHeure(dateJour, 1).getTime() + 10*1000);

        // this.notBuilder.setWhen(System.currentTimeMillis()+ 300* 1000);
        this.notBuilder.setWhen(longNotif);

        //this.notBuilder.setShowWhen(false);
        this.notBuilder.setShowWhen(true);
        this.notBuilder.setContentTitle("This is title");
        String messageNotif = "";
        messageNotif += "prog: "+dateNotif.toString();

        this.notBuilder.setContentText(messageNotif);
        //this.notBuilder.setContentText("This is content text ....");

        // Create Intent
        Intent intent = new Intent(context, MyBroadcastReceiver.class);

        // PendingIntent.getActivity(..) will start an Activity, and returns PendingIntent object.
        // It is equivalent to calling Context.startActivity(Intent).
        PendingIntent pendingIntent = PendingIntent.getActivity(context, MY_REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        this.notBuilder.setContentIntent(pendingIntent);

        // Get a notification service (A service available on the system).
        NotificationManager notificationService  =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Builds notification and issue it

        Notification notification =  notBuilder.build();
        notificationService.notify(MY_NOTIFICATION_ID, notification);
    }
}