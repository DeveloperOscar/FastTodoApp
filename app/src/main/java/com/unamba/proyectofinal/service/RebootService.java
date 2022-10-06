package com.unamba.proyectofinal.service;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unamba.proyectofinal.models.provider.TodoProvider;
import com.unamba.proyectofinal.utils.UtilApp;

import java.time.LocalDateTime;

@RequiresApi(api = Build.VERSION_CODES.N)
public class RebootService extends IntentService {
  public RebootService(String name) {
    super(name);
    startForeground(1, new Notification());
  }

  public RebootService() {
    super("RebootService");
  }

  @Override
  public void onCreate() {
    super.onCreate();
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Override
  protected void onHandleIntent(Intent intent) {
    String intentType = intent.getExtras().getString("caller");
    if (intentType == null) return;
    if (intentType.equals("RebootReceiver")) {
      TodoProvider todoProvider = new TodoProvider(getApplicationContext());
      todoProvider.getAll().forEach(todo -> {
        UtilApp.setAlarm(todo.id,UtilApp.getLocalDateTimeInMillis(LocalDateTime.parse(todo.dateReminder)),getApplicationContext(),todo);
      });
    }
  }
}
