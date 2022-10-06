package com.unamba.proyectofinal.models.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.unamba.proyectofinal.models.entities.Todo;

import java.util.List;

@Dao
public interface TodoDao {
  @Query("SELECT * FROM todos")
  public List<Todo> getAll();

  @Query("SELECT * FROM todos where id IN (:todoIds)")
  public List<Todo> loadAllByIds(int[] todoIds);

  @Query("SELECT * FROM todos WHERE id = :id")
  public Todo findById(int id);

  @Query("SELECT * FROM todos WHERE name LIKE :name LIMIT 1")
  public Todo findByName(String name);

  @Query("SELECT * FROM todos WHERE name LIKE :name and name != :excludeName LIMIT 1")
  public Todo findByNameExclude(String name,String excludeName);

  @Insert
  public long insert(Todo todo);

  @Delete
  public void delete(Todo todo);

  @Update
  public int update(Todo todo);

}
