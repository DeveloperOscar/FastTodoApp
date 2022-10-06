package com.unamba.proyectofinal.view;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.unamba.proyectofinal.R;
import com.unamba.proyectofinal.adapter.TodoAdapter;
import com.unamba.proyectofinal.models.provider.TodoProvider;
import com.unamba.proyectofinal.utils.UtilApp;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListTodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

@RequiresApi(api = Build.VERSION_CODES.O)
public class ListTodoFragment extends Fragment {

  private final TodoProvider todoProvider;


  public ListTodoFragment() {
    todoProvider = new TodoProvider();
  }

  public static ListTodoFragment newInstance() {
    ListTodoFragment fragment = new ListTodoFragment();
    Bundle args = new Bundle();
    //args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      //mParam1 = getArguments().getString(ARG_PARAM1);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_list_todo, container, false);
    initRecyclerView(view);
    view.findViewById(R.id.btnCreate).setOnClickListener((v) -> showDetailsTodo(-1));
    return view;
  }

  private void initRecyclerView(View view) {
    RecyclerView recyclerView = view.findViewById(R.id.recyclerViewTODO);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    TodoAdapter adapter = new TodoAdapter(todoProvider.getAll(), todo -> {
      UtilApp.cancelAlarm(todo.id,getContext());
      todoProvider.delete(todo);
      refreshView();
    }, todo -> {
      showDetailsTodo(todo.id);
    });
    recyclerView.setAdapter(adapter);
  }

  private void showDetailsTodo(int id) {
    MainActivity activity = (MainActivity) getActivity();
    assert activity != null;
    activity.changeFragment(DetailsTodoFragment.newInstance(id));
  }

  private void refreshView() {
    MainActivity activity = (MainActivity) getActivity();
    assert activity != null;
    activity.changeFragment(ListTodoFragment.newInstance());
  }
}