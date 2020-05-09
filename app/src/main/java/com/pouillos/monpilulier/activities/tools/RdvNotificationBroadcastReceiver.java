package com.pouillos.monpilulier.activities.tools;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.pouillos.monpilulier.R;

import java.util.Date;

public class RdvNotificationBroadcastReceiver extends BroadcastReceiver {

    public String NOTIFICATION_CHANNEL_ID = "100001" ;
    private final static String default_notification_channel_id = "default" ;
    boolean connected = true;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint( "UnsafeProtectedBroadcastReceiver" )
    @Override
    public void onReceive (Context context , Intent intent) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, default_notification_channel_id);
            builder.setContentTitle("Rappel - RDV");
            String contact = intent.getStringExtra("detail");
            String echeance = intent.getStringExtra("echeance");
            if (contact != null && echeance != null) {
                builder.setContentText(contact + " - " + echeance);
            } else {
                builder.setContentText("consulter les Rdv");
            }
            builder.setSmallIcon(R.drawable.home_pill_notif);
            builder.setAutoCancel(true);
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
            Notification notification = builder.build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_RDV", importance);
                assert notificationManager != null;
                notificationManager.createNotificationChannel(notificationChannel);
            }
            assert notificationManager != null;
            Long randomId = new Date().getTime();
            if (connected) {
                notificationManager.notify(randomId.intValue(), notification);
                connected = false;
            } else {
                notificationManager.cancel(randomId.intValue());
                connected = true;
            }
        }
}
