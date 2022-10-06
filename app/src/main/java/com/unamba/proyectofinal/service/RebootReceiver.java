package com.unamba.proyectofinal.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class RebootReceiver extends BroadcastReceiver {
  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  public void onReceive(Context context, Intent intent) {
    Intent serviceIntent = new Intent(context, RebootService.class);
    serviceIntent.putExtra("caller", "RebootReceiver");
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      context.startForegroundService(serviceIntent);
    } else {
      context.startService(serviceIntent);
    }
  }
}
