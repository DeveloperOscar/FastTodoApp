package com.unamba.proyectofinal.service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.unamba.proyectofinal.R;
import com.unamba.proyectofinal.view.MainActivity;

public class NotificationService extends IntentService {
  private NotificationManager notificationManager;
  private PendingIntent pendingIntent;
  private static int NOTIFICATION_ID = 1;
  Notification notification;


  public NotificationService(String name) {
    super(name);
  }

  public NotificationService() {
    super("SERVICE");
  }

  @TargetApi(Build.VERSION_CODES.O)
  @Override
  protected void onHandleIntent(Intent intent2) {
    String NOTIFICATION_CHANNEL_ID = getApplicationContext().getString(R.string.app_name);
    Context context = this.getApplicationContext();
    notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    Intent mIntent = new Intent(this, MainActivity.class);
    Resources res = this.getResources();
    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    String message = intent2.getStringExtra("text");
    String title = intent2.getStringExtra("title");

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      final int NOTIFY_ID = intent2.getIntExtra("idNotification",0); // ID of notification
      String id = NOTIFICATION_CHANNEL_ID; // default_channel_id
      String titleNotification = NOTIFICATION_CHANNEL_ID; // Default Channel
      PendingIntent pendingIntent;
      NotificationCompat.Builder builder;
      NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      if (notifManager == null) {
        notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
      }
      int importance = NotificationManager.IMPORTANCE_HIGH;
      NotificationChannel mChannel = notifManager.getNotificationChannel(id);
      if (mChannel == null) {
        mChannel = new NotificationChannel(id, titleNotification, importance);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        notifManager.createNotificationChannel(mChannel);
      }
      builder = new NotificationCompat.Builder(context, id);
      mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      pendingIntent = PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
      builder.setContentTitle(title).setCategory(Notification.CATEGORY_SERVICE)
              .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)   // required
              .setContentText(message)
              .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.iconapp))
              .setDefaults(Notification.DEFAULT_ALL)
              .setAutoCancel(true)
              .setSound(soundUri)
              .setStyle(new NotificationCompat.BigTextStyle())
              .setGroup("Fast Todo App Reminder")
              .setContentIntent(pendingIntent)
              .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
      Notification notification = builder.build();
      notifManager.notify(NOTIFY_ID, notification);

      startForeground(1, notification);

    } else {
      pendingIntent = PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
      notification = new NotificationCompat.Builder(this)
              .setContentIntent(pendingIntent)
              .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
              .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.iconapp))
              .setSound(soundUri)
              .setAutoCancel(true)
              .setContentTitle(title).setCategory(Notification.CATEGORY_SERVICE)
              .setContentText(message).build();
      notificationManager.notify(NOTIFICATION_ID, notification);
    }
  }
}

