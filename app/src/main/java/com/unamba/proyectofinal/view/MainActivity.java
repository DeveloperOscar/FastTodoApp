package com.unamba.proyectofinal.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;

import com.unamba.proyectofinal.R;
import com.unamba.proyectofinal.models.database.Database;


@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Database.connect(getApplicationContext());
    setContentView(R.layout.activity_main);
  }

  public void changeFragment(Fragment newFragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.replace(R.id.containerFragment, newFragment);
    fragmentTransaction.addToBackStack(null);
    fragmentTransaction.commit();
  }


}