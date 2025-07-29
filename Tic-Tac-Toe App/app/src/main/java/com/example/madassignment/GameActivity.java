package com.example.madassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class GameActivity extends AppCompatActivity {
    private SharedViewModel viewModel;
    private int gameMode;
    //private int winCondition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        viewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        gameMode = viewModel.getGameMode().getValue();
        if(gameMode == 3){
            ThreeFragment threeFragment = new ThreeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.FrameBoard, threeFragment);
            fragmentTransaction.commit();
        }else if(gameMode == 4){
            FourFragment fourFragment = new FourFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.FrameBoard, fourFragment);
            fragmentTransaction.commit();
        }else{
            FiveFragment fiveFragment = new FiveFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.FrameBoard, fiveFragment);
            fragmentTransaction.commit();
        }

        loadPlayer1Frag();
        loadPlayer2Frag();
        loadSettingFrag();
    }
    private void loadSettingFrag(){
        SettingFragment settingFragment = new SettingFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameSettings, settingFragment);
        fragmentTransaction.commit();
    }
    private void loadPlayer1Frag(){
        PlayerFragment playerFragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("player", 1);
        playerFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.framePlayer1, playerFragment);
        fragmentTransaction.commit();
    }
    private void loadPlayer2Frag(){
        PlayerFragment playerFragment = new PlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("player", 2);
        playerFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.framePlayer2, playerFragment);
        fragmentTransaction.commit();
    }
}