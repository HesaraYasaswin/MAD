package com.example.madassignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

public class ModeFragment extends Fragment {

    private SharedViewModel viewModel;

    public ModeFragment() {
        // Required empty public constructor
    }
    private int mode = 3;
    private int winCondition = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_mode, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        Button[] modeButton = new Button[3];
        Button[] winButton = new Button[3];
        Button start = rootView.findViewById(R.id.buttonStart);

        for (int i = 0; i < 3; i++) {
            String modButtonID = "button" + (i+3) + "x" + (i+3);
            String winButtonID = "buttonWin" + (i+3);
            int modeResID = getResources().getIdentifier(modButtonID, "id", getActivity().getPackageName());
            int winResID = getResources().getIdentifier(winButtonID, "id", getActivity().getPackageName());
            modeButton[i] = rootView.findViewById(modeResID);
            winButton[i] = rootView.findViewById(winResID);
        }
        winButton[1].setEnabled(false);
        winButton[2].setEnabled(false);

        for(int i = 0; i < 3; i++){
            final int fi = i+3;
            modeButton[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setMode(fi);
                    //setWinCondition(fi);
                    if(mode == 3) {
                        winCondition = 3;
                        winButton[0].setEnabled(true);
                        winButton[1].setEnabled(false);
                        winButton[2].setEnabled(false);
                        winButton[0].setAlpha(1.0f);
                        winButton[1].setAlpha(0.5f);
                        winButton[2].setAlpha(0.5f);
                    }else if(mode == 4) {
                        winCondition = 4;
                        winButton[0].setEnabled(true);
                        winButton[1].setEnabled(true);
                        winButton[2].setEnabled(false);
                        winButton[0].setAlpha(0.5f);
                        winButton[1].setAlpha(1.0f);
                        winButton[2].setAlpha(0.5f);
                    }else {
                        winCondition = 5;
                        winButton[0].setEnabled(true);
                        winButton[1].setEnabled(true);
                        winButton[2].setEnabled(true);
                        winButton[0].setAlpha(0.5f);
                        winButton[1].setAlpha(0.5f);
                        winButton[2].setAlpha(1.0f);
                    }
                }
            });

            winButton[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setWinCondition(fi);
                    Log.d("winfool", "winCond" + winCondition);
                    if(winCondition == 3) {
                        winButton[0].setAlpha(1.0f);
                        winButton[1].setAlpha(0.5f);
                        winButton[2].setAlpha(0.5f);
                    }else if(winCondition == 4) {
                        winButton[0].setAlpha(0.5f);
                        winButton[1].setAlpha(1.0f);
                        winButton[2].setAlpha(0.5f);
                    }else {
                        winButton[0].setAlpha(0.5f);
                        winButton[1].setAlpha(0.5f);
                        winButton[2].setAlpha(1.0f);
                    }
                }
            });
        }

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(getContext(), GameActivity.class);
                    viewModel.setGameMode(mode);
                    viewModel.setWinCondition(winCondition);
                    startActivity(intent);
            }
        });

        return rootView;
    }
    private void setMode(int value) {
        this.mode = value;
    }
    private void setWinCondition(int value) {
        this.winCondition = value;
    }
}