package com.unamba.proyectofinal.view;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.unamba.proyectofinal.R;
import com.unamba.proyectofinal.adapter.TODOViewHolder;
import com.unamba.proyectofinal.databinding.FragmentDetailsTodoBinding;
import com.unamba.proyectofinal.models.entities.Todo;
import com.unamba.proyectofinal.models.provider.TodoProvider;
import com.unamba.proyectofinal.utils.UtilApp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DetailsTodoFragment extends Fragment {

  private static final String ARG_ID = "id";
  private final TodoProvider todoProvider;
  private FragmentDetailsTodoBinding binding;
  private int currentIdTodo;
  private final DateTimeFormatter dateFormatter,timeFormatter;

  public DetailsTodoFragment() {
    todoProvider = new TodoProvider();
    dateFormatter = DateTimeFormatter.ofPattern("E,dd MMM yyyy");
    timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
  }

  public static DetailsTodoFragment newInstance(int id) {
    DetailsTodoFragment fragment = new DetailsTodoFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_ID, id);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      currentIdTodo = getArguments().getInt(ARG_ID);
    }
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    binding = FragmentDetailsTodoBinding.inflate(inflater,container,false);
    binding.btnConfirmaChange.setOnClickListener(this::handleConfirmChange);
    binding.btnIconTime.setOnClickListener(this::handleClickTimeButton);
    binding.btnIconDate.setOnClickListener(this::handleClickDateButton);
    binding.todoCalendar.setOnClickListener(this::handleClickDateButton);
    binding.todoTime.setOnClickListener(this::handleClickTimeButton);
    binding.todoPriority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        handleChangePriority(adapterView,view,i,l);
      }
      public void onNothingSelected(AdapterView<?> adapterView) {

      }
    });
    initDetailsView();
    return binding.getRoot();
  }

  @Override
  public void onDestroyView(){
    super.onDestroyView();
    binding = null;
  }


  private void initDetailsView(){
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.priority_array, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    binding.todoPriority.setAdapter(adapter);
    fillFieldDetails(adapter);
  }

  private void handleClickTimeButton(View v){
    DialogFragment newFragment = new TimePickerFragment(localTime -> {
      binding.todoTime.setText(timeFormatter.format(localTime));
    }, LocalTime.parse(binding.todoTime.getText().toString(),timeFormatter));
    newFragment.show(requireActivity().getSupportFragmentManager(), "timePicker");
  }

  private void handleClickDateButton(View v){
    DialogFragment newFragment = new DatePickerFragment(localDate -> {
      binding.todoCalendar.setText(dateFormatter.format(localDate));
    }, LocalDate.parse(binding.todoCalendar.getText().toString(),dateFormatter));
    newFragment.show(requireActivity().getSupportFragmentManager(), "datePicker");
  }

  private void handleChangePriority(AdapterView<?> adapter, View view, int i, long l){
    String selected = String.valueOf(adapter.getSelectedItem());
    int color = Color.parseColor(TODOViewHolder.PRIORITY_COLOR.LOW.getValue());
    if(selected.equals(Todo.HIGH_PRIORITY))
      color = Color.parseColor(TODOViewHolder.PRIORITY_COLOR.HIGH.getValue());
    if(selected.equals(Todo.MEDIUM_PRIORITY))
      color = Color.parseColor(TODOViewHolder.PRIORITY_COLOR.MEDIUM.getValue());
    binding.colorPriority.setBackgroundColor(color);
  }

  private void handleConfirmChange(View view){
    Todo newTodo = getTodoInput();
    Optional<String> error = todoProvider.validate(newTodo);
    if(!error.isPresent()){
        int id = todoProvider.insertOrUpdate(newTodo);
        UtilApp.setAlarm(id,UtilApp.getLocalDateTimeInMillis(LocalDateTime.parse(newTodo.dateReminder)),requireContext(),newTodo);
        toTodoListFragment();
    }else Toast.makeText(getContext(),error.get(),Toast.LENGTH_SHORT).show();
  }

  private void toTodoListFragment(){
    MainActivity activity = (MainActivity)getActivity();
    assert activity != null;
    activity.changeFragment(ListTodoFragment.newInstance());
  }

  private void fillFieldDetails(ArrayAdapter<CharSequence> spinnerAdapter){
    Optional<Todo> todo = todoProvider.findById(this.currentIdTodo);
    if(todo.isPresent()){
      Todo presentTodo = todo.get();
      Spinner priorities = binding.todoPriority;
      LocalDateTime dateTime = LocalDateTime.parse(presentTodo.dateReminder);
      binding.todoName.setText(presentTodo.name);
      binding.todoDescription.setText(presentTodo.description);
      binding.todoTime.setText(timeFormatter.format(dateTime.toLocalTime()));
      binding.todoCalendar.setText(dateFormatter.format(dateTime.toLocalDate()));
      binding.colorPriority.setBackgroundColor(Color.parseColor(presentTodo.getPriorityColor()));
      priorities.setSelection(spinnerAdapter.getPosition(presentTodo.priority));
    }else{
      LocalTime localTime = LocalTime.now();
      int hour = localTime.getHour();
      if(hour >= 23) hour = 0;
      else hour += 1;
      binding.colorPriority.setBackgroundColor(Color.parseColor(TODOViewHolder.PRIORITY_COLOR.LOW.getValue()));
      binding.todoTime.setText(timeFormatter.format(LocalTime.of(hour,localTime.getMinute())));
      binding.todoCalendar.setText(dateFormatter.format(LocalDate.now()));
    }
  }

  private Todo getTodoInput(){
    Todo newTodo = new Todo();
    Optional<Todo> currentTodo = todoProvider.findById(this.currentIdTodo);
    if(currentTodo.isPresent()) newTodo = currentTodo.get();
    LocalDate localDate = LocalDate.parse(binding.todoCalendar.getText(),dateFormatter);
    LocalTime localTime = LocalTime.parse(binding.todoTime.getText(),timeFormatter);
    newTodo.priority = String.valueOf(binding.todoPriority.getSelectedItem());
    newTodo.dateReminder = LocalDateTime.of(localDate,localTime).toString();
    newTodo.description = binding.todoDescription.getText().toString();
    newTodo.name = binding.todoName.getText().toString();
    return newTodo;
  }
}