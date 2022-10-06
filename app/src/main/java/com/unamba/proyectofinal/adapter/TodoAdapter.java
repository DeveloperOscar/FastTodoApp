package com.unamba.proyectofinal.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.unamba.proyectofinal.R;
import com.unamba.proyectofinal.models.entities.Todo;

import java.util.List;
import java.util.function.Consumer;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TodoAdapter extends RecyclerView.Adapter<TODOViewHolder> {
  private final List<Todo> listOfTODO;
  private final Consumer<Todo> handleDelete;
  private final Consumer<Todo> handleOnClick;

  public TodoAdapter(List<Todo> listOfTODO,Consumer<Todo> handleDelete,Consumer<Todo> handleOnClick) {
    this.listOfTODO = listOfTODO;
    this.handleDelete = handleDelete;
    this.handleOnClick = handleOnClick;
  }

  @NonNull
  @Override
  public TODOViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    return new TODOViewHolder(layoutInflater.inflate(R.layout.todo_item, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull TODOViewHolder holder, int position) {
    Todo item = listOfTODO.get(position);
    holder.render(item,this.handleDelete,this.handleOnClick);
  }

  @Override
  public int getItemCount() {
    return listOfTODO.size();
  }

  public List<Todo> getListOfTODO(){return this.listOfTODO;}
}
