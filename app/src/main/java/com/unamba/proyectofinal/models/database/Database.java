package com.unamba.proyectofinal.models.database;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.room.Room;

public class Database {
  private static FastTodoDatabase db = null;
  public static FastTodoDatabase connect(@Nullable Context context){
    if(db == null && context != null){
      db = Room.databaseBuilder(context,
              FastTodoDatabase.class,"FastTodoDatabase").allowMainThreadQueries().build();
    }
    return db;
  }
}
