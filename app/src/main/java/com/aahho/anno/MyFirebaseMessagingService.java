package com.aahho.anno;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by souvikdas on 29/10/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.

        if(remoteMessage.getData().size() > 0){
            String sender, message;

            sender = remoteMessage.getData().get("messageTitle");
            message = remoteMessage.getData().get("messageBody");

            Intent intent = new Intent(this, AnnoMainScreenActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setContentTitle(sender);
            notificationBuilder.setContentText(message);
            notificationBuilder.setContentIntent(pendingIntent);
            notificationBuilder.setSound(soundUri);
            notificationBuilder.setSmallIcon(R.drawable.logofinal1282);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());


        }
    }


    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, AnnoMainScreenActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Notification n  = new Notification.Builder(this)
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject")
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.message, "Call", pIntent)
                .addAction(R.drawable.message, "More", pIntent)
                .addAction(R.drawable.message, "And more", pIntent).build();


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);

    }
}
