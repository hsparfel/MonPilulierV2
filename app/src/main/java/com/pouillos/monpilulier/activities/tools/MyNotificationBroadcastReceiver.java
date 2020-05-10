package com.pouillos.monpilulier.activities.tools;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import android.os.Bundle;

import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.pouillos.monpilulier.R;

import java.util.Date;

public class MyNotificationBroadcastReceiver  extends BroadcastReceiver {
    //uiliser cette classe pour rdv , rdv analyse et rdv examen

    //public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    public String NOTIFICATION_CHANNEL_ID = "10002" ;
    private final static String default_notification_channel_id = "default" ;
    boolean connected = true;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint( "UnsafeProtectedBroadcastReceiver" )
    @Override
    public void onReceive (Context context , Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context , default_notification_channel_id ) ;
        builder.setContentTitle( "Rappel - RDV" ) ;
        String myTest = intent.getStringExtra("testA");
        String myTest2 = intent.getStringExtra("testB");
      //  Bundle bundle = intent.getExtras();

        //String action = intent.getAction() ;
      //  Log.e("USB" , action) ;
        //assert action != null;
        //builder.setContentText( "Connected" ) ;

        //builder.setContentText( ""+new Date().toString() ) ;
        builder.setContentText( myTest + " - " + myTest2) ;

        //builder.setSmallIcon(R.drawable.ic_launcher_foreground) ;
        builder.setSmallIcon(R.drawable.home_pill_notif) ;
        builder.setAutoCancel( true ) ;
        builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
        //builder.setChannelId( ""+ notif_channel_id ) ;
        Notification notification = builder.build() ;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context. NOTIFICATION_SERVICE ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;

            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME_NEW" , importance) ;
            //NotificationChannel notificationChannel = new NotificationChannel( ""+notif_channel_id , "NOTIFICATION_CHANNEL_NAME"+notif_channel_id , importance) ;
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel) ;
        }
        assert notificationManager != null;
        ///////////
        Long notif_channel_id = new Date().getTime();
        ///////////////
        if ( connected ) {

            //notificationManager.notify( 1 , notification) ;
            notificationManager.notify(notif_channel_id.intValue() , notification) ;
            connected = false;
        } else {
            //notificationManager.cancel( 1 ) ;
            notificationManager.cancel( notif_channel_id.intValue() ) ;
            connected = true;
        }
    }
}
