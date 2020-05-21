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

public class PriseNotificationBroadcastReceiver extends BroadcastReceiver {
    public String NOTIFICATION_CHANNEL_ID = "10098" ;
    private final static String prise_notification_channel_id = "prise" ;
    boolean connected = true;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint( "UnsafeProtectedBroadcastReceiver" )
    @Override
    public void onReceive (Context context , Intent intent) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, prise_notification_channel_id);
        builder.setContentTitle("Rappel - Traitement");
        String detail = intent.getStringExtra("detail");
        //String echeance = intent.getStringExtra("echeance");
        if (detail != null) {
            builder.setContentText(detail);
        } else {
            builder.setContentText("consulter les Ordonnances");
        }
        builder.setSmallIcon(R.drawable.home_pill_notif);
        builder.setAutoCancel(true);
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        Notification notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_PRISE", importance);
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
