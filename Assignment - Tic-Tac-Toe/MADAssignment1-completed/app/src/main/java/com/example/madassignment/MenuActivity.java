package com.example.madassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.security.Key;

public class MenuActivity extends AppCompatActivity {

    private SharedViewModel viewModel;//testing2/4

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        viewModel.getGameAgainst().observe(this, value -> {
            Log.d("again","bool: "+value);
            SharedPreferences sharedPreferences = getSharedPreferences("gameAgainst", MODE_PRIVATE);//testing3/4
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putBoolean("gameAgainst", value);
            myEdit.apply();
        });

        viewModel.getWinCondition().observe(this, value -> {
            Log.d("winCond","int: "+value);
            SharedPreferences sharedPreferences = getSharedPreferences("winCondition", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putInt("winCondition", value);
            myEdit.apply();
        });
        loadMainMenuFrag();
    }

    private void loadMainMenuFrag(){
        MainMenuFragment mainMenuFragment = new MainMenuFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameMenu, mainMenuFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}