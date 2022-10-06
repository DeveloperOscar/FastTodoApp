package com.unamba.proyectofinal.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unamba.proyectofinal.models.entities.Todo;
import com.unamba.proyectofinal.service.AlarmReceiver;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class UtilApp {
  @RequiresApi(api = Build.VERSION_CODES.O)
  public static long getLocalDateTimeInMillis(final LocalDateTime dateTime) {
    Instant instant = dateTime.atZone(ZoneId.systemDefault()).toInstant();
    Date date = Date.from(instant);
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.getTimeInMillis();
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  public  static long[] getHourMinutesSeconds(LocalDateTime date1, LocalDateTime date2){
    long milliseconds = Math.abs(getLocalDateTimeInMillis(date1) - getLocalDateTimeInMillis(date2));
    long seconds = 0, minutes = 0,hours = 0;
    if(milliseconds > 1000) {
      seconds = milliseconds / 1000;
    }
    if(seconds >= 60) {
      minutes = seconds / 60;
      seconds = seconds % 60;
    }
    if(minutes >= 60){
      hours = minutes / 60;
      minutes = minutes % 60;
    }
    return new long[]{hours,minutes,seconds};
  }



  @RequiresApi(api = Build.VERSION_CODES.O)
  public static void setAlarm(int i, long timestamp, Context context, Todo todo){
    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    Intent alarmIntent = new Intent(context, AlarmReceiver.class);
    alarmIntent.putExtra("title",todo.name);
    alarmIntent.putExtra("text",todo.description);
    alarmIntent.putExtra("idNotification",todo.id);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context,i,alarmIntent,PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
    alarmIntent.setData(Uri.parse("custom://" + System.currentTimeMillis()));
    alarmManager.setExact(AlarmManager.RTC_WAKEUP,timestamp,pendingIntent);
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  public static void cancelAlarm(int id, Context context){
    Intent intent = new Intent(context,AlarmReceiver.class);
    PendingIntent sender = PendingIntent.getBroadcast(context,id,intent,PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
    ((AlarmManager)context.getSystemService(Context.ALARM_SERVICE)).cancel(sender);
  }
}
