package com.unamba.proyectofinal.models.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.unamba.proyectofinal.models.dao.TodoDao;
import com.unamba.proyectofinal.models.entities.Todo;

@Database(entities = {Todo.class}, version = 1)
public abstract class FastTodoDatabase  extends RoomDatabase {
  public abstract TodoDao todoDao();
}
