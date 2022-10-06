package com.unamba.proyectofinal.view;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.unamba.proyectofinal.utils.UtilApp;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.function.Consumer;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

  private final Consumer<LocalTime> onTimeSet;
  private final LocalTime currentTime;

  public TimePickerFragment(Consumer<LocalTime> onTimeSet,LocalTime currentTime){
    this.onTimeSet = onTimeSet;
    this.currentTime = currentTime;
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current time as the default values for the picker
    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);

    // Create a new instance of TimePickerDialog and return it

    return new TimePickerDialog(getActivity(), this,this.currentTime.getHour(),this.currentTime.getMinute(),
            DateFormat.is24HourFormat(getActivity()));
  }

  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    this.onTimeSet.accept(LocalTime.of(hourOfDay,minute));
  }
}
