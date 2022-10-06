package com.unamba.proyectofinal.models.provider;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.unamba.proyectofinal.models.dao.TodoDao;
import com.unamba.proyectofinal.models.database.Database;
import com.unamba.proyectofinal.models.entities.Todo;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiresApi(api = Build.VERSION_CODES.N)
public class TodoProvider {
  private final TodoDao todoDao;

  public TodoProvider() {
    this.todoDao = Database.connect(null).todoDao();
  }

  public TodoProvider(Context context){
    this.todoDao = Database.connect(context).todoDao();
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  public int insertOrUpdate(Todo todo) {
    Optional<Todo> persistTodo = findById(todo.id);
    if (!persistTodo.isPresent()) {
      todo.dateCreation = LocalDate.now().toString();
      todo.status = Todo.UNCOMPLETED;
      return (int)(todoDao.insert(todo));
    } else {
      todoDao.update(todo);
      return todo.id;
    }
  }

  public void delete(Todo todo) {
    todoDao.delete(todo);
  }

  public Optional<Todo> findById(int id) {
    return Optional.ofNullable(todoDao.findById(id));
  }

  public Optional<Todo> findByName(String name) {
    return Optional.ofNullable(todoDao.findByName(name));
  }

  public List<Todo> getAll() {
    return todoDao.getAll();
  }

  public Optional<String> validate(Todo todo) {
    Optional<Todo> persistTodo = findById(todo.id);
    if ((persistTodo.isPresent() && findByNameExclude(todo.name, persistTodo.get().name).isPresent()
            || (!persistTodo.isPresent() && findByName(todo.name).isPresent())
    )) {
      return Optional.of("Ya existe la tarea " + todo.name);
    }
    if(todo.name.isEmpty())
      return Optional.of("el campo nombre de tarea no puede estar vacio");
    return Optional.empty();
  }

  public void update(Todo todo){
    todoDao.update(todo);
  }

  public Optional<Todo> findByNameExclude(String name, String excludeName) {
    return Optional.ofNullable(todoDao.findByNameExclude(name, excludeName));
  }
}
