package com.unamba.proyectofinal.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AlarmReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    Intent notificationService = new Intent(context,NotificationService.class);
    notificationService.setData(Uri.parse("custom://" + System.currentTimeMillis()));
    notificationService.putExtra("title",intent.getExtras().getString("title"));
    notificationService.putExtra("text",intent.getExtras().getString("text"));
    notificationService.putExtra("idNotification",intent.getExtras().getInt("idNotification"));
    ContextCompat.startForegroundService(context,notificationService);
  }

}
