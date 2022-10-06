package com.unamba.proyectofinal.adapter;

import android.graphics.Color;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.unamba.proyectofinal.databinding.TodoItemBinding;
import com.unamba.proyectofinal.models.dao.TodoDao;
import com.unamba.proyectofinal.models.database.Database;
import com.unamba.proyectofinal.models.entities.Todo;
import com.unamba.proyectofinal.models.provider.TodoProvider;
import com.unamba.proyectofinal.utils.UtilApp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TODOViewHolder extends RecyclerView.ViewHolder {
  private final TodoItemBinding binding;
  private DateTimeFormatter dateTimeFormatter;
  private TodoProvider todoProvider;

  public enum PRIORITY_COLOR {
    HIGH("#ff6961"), MEDIUM("#fdfd96"), LOW("#84b6f4");
    private final String value;

    PRIORITY_COLOR(String value) {
      this.value = value;
    }

    public String getValue() {
      return this.value;
    }
  }


  public TODOViewHolder(View view) {
    super(view);
    binding = TodoItemBinding.bind(view);
    dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm E,  dd MMM yyyy");
    todoProvider = new TodoProvider();
  }

  public void render(Todo todo, Consumer<Todo> handleDelete, Consumer<Todo> handleOnClick) {
    binding.tvTODO.setText(todo.name);
    binding.cbStatusTODO.setChecked(todo.isCompleted());
    this.renderDateReminder(todo);
    int color = Color.parseColor(PRIORITY_COLOR.LOW.getValue());
    switch (todo.priority) {
      case Todo.HIGH_PRIORITY:
        color = Color.parseColor(PRIORITY_COLOR.HIGH.getValue());
        break;
      case Todo.MEDIUM_PRIORITY:
        color = Color.parseColor(PRIORITY_COLOR.MEDIUM.getValue());
        break;
    }
    binding.cardViewContainer.setCardBackgroundColor(color);
    binding.cardViewContainer.setOnClickListener(view -> handleOnClick.accept(todo));
    binding.btnDelete.setOnClickListener(view -> handleDelete.accept(todo));
    binding.cbStatusTODO.setOnCheckedChangeListener((cb, checked) -> {
      todo.check(checked);
      todoProvider.update(todo);
    });
  }

  private void renderDateReminder(Todo todo) {
    LocalDateTime dateReminder = LocalDateTime.parse(todo.dateReminder);
    LocalDateTime currenDate = LocalDateTime.now();
    if (dateReminder.isBefore(currenDate)) {
      long[] hoursMinutesSeconds = UtilApp.getHourMinutesSeconds(dateReminder, currenDate);
      long seconds = hoursMinutesSeconds[2];
      long minutes = hoursMinutesSeconds[1];
      long hours = hoursMinutesSeconds[0];
      String text = "Expiro hace ";
      if (hours == 1) text += hours + " hora ";
      else if (hours > 1) text += hours + " horas ";
      if (minutes == 1) text += minutes + " minuto ";
      else if (minutes > 1) text += minutes + " minutos ";
      text += seconds + " segundos.";
      binding.tvReminder.setText(text);
    } else {
      binding.tvReminder.setText(dateTimeFormatter.format(LocalDateTime.parse(todo.dateReminder)).toString());
    }
  }
}
