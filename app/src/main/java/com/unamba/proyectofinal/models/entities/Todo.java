package com.unamba.proyectofinal.models.entities;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.unamba.proyectofinal.adapter.TODOViewHolder;

import java.util.Objects;

@Entity(tableName = "todos",indices = {@Index(value= {"name"},unique = true )})
public class Todo {
  @PrimaryKey(autoGenerate = true)
  public int id;
  @ColumnInfo(name = "name")
  public String name;
  @ColumnInfo(name = "description")
  public String description;
  @ColumnInfo(name = "dateCreation")
  public String dateCreation;
  @ColumnInfo(name = "dateReminder")
  public String dateReminder;
  @ColumnInfo(name = "status")
  public String status;
  @ColumnInfo(name = "priority")
  public String priority;


  @Ignore
  public static final String HIGH_PRIORITY = "ALTA";
  @Ignore
  public static final String MEDIUM_PRIORITY = "MEDIA";
  @Ignore
  public static final String LOW_PRIORITY = "BAJA";
  @Ignore
  public static final String COMPLETED = "COMPLETED";
  @Ignore
  public static final String UNCOMPLETED = "UNCOMPLETED";

  @Ignore
  public boolean isCompleted(){
    return Objects.equals(this.status, Todo.COMPLETED);
  }
  @Ignore
  public void check(boolean status){
    if(status) this.status = Todo.COMPLETED;
    else this.status = Todo.UNCOMPLETED;
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @Ignore
  public String getPriorityColor(){
    if(Objects.equals(this.priority, HIGH_PRIORITY)) return TODOViewHolder.PRIORITY_COLOR.HIGH.getValue();
    if(Objects.equals(this.priority,MEDIUM_PRIORITY)) return TODOViewHolder.PRIORITY_COLOR.MEDIUM.getValue();
    return TODOViewHolder.PRIORITY_COLOR.LOW.getValue();
  }
}
