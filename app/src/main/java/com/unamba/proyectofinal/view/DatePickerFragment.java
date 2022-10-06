package com.unamba.proyectofinal.view;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.function.Consumer;

@RequiresApi(api = Build.VERSION_CODES.N)
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

  private final Consumer<LocalDate> onDataSet;
  private final LocalDate currentDate;

  public DatePickerFragment(Consumer<LocalDate> onDataSet,LocalDate currentDate){
    this.onDataSet = onDataSet;
    this.currentDate = currentDate;
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current date as the default date in the picker
    final Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);
    return new DatePickerDialog(requireContext(), this, currentDate.getYear(), currentDate.getMonthValue() - 1, currentDate.getDayOfMonth());
  }

  @RequiresApi(api = Build.VERSION_CODES.O)
  public void onDateSet(DatePicker view, int year, int month, int day) {
    this.onDataSet.accept(LocalDate.of(year,month + 1,day));
  }
}
