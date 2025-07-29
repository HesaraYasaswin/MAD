package com.example.madassignment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class SelectPlayer2Fragment extends Fragment {

    private SharedViewModel viewModel;
    Profile player1;
    Profile player2;

    public SelectPlayer2Fragment(){
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_select_player2, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        Button[] profile1 = new Button[5];
        Button[] profile2 = new Button[5];
        Button okButton = rootView.findViewById(R.id.buttonOk);
        int count = viewModel.getProfileCount();
        MutableLiveData<ArrayList<Profile>> profileList = viewModel.getProfileList();

        for (int i = 0; i < 5; i++) {
            String p1ButtonID = "buttonPlayer1Profile" + (i+1);
            int p1ResID = getResources().getIdentifier(p1ButtonID, "id", getActivity().getPackageName());
            profile1[i] = rootView.findViewById(p1ResID);
            profile1[i].setAlpha(0.5f);
            profile1[i].setVisibility(View.INVISIBLE);
            profile1[i].setEnabled(false);

            String p2ButtonID = "buttonPlayer2Profile" + (i+1);
            int p2ResID = getResources().getIdentifier(p2ButtonID, "id", getActivity().getPackageName());
            profile2[i] = rootView.findViewById(p2ResID);
            profile2[i].setAlpha(0.5f);
            profile2[i].setVisibility(View.INVISIBLE);
            profile2[i].setEnabled(false);
        }

        // Observe the LiveData
        profileList.observe(getViewLifecycleOwner(), profile -> {
            // Handle changes to the profile list here
            for(int i=0;i<count;i++){
                final int fi = i;
                profile1[i].setEnabled(true);
                profile1[i].setText(profile.get(i).getName());
                profile1[i].setVisibility(View.VISIBLE);
                profile1[i].setAlpha(1.0f);

                profile2[i].setEnabled(true);
                profile2[i].setText(profile.get(i).getName());
                profile2[i].setVisibility(View.VISIBLE);
                profile2[i].setAlpha(1.0f);
            }
        });
        for(int i = 0; i<5;i++){
            final int fi = i;
            profile1[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player1 = viewModel.getProfile(profile1[fi].getText().toString());
                    profile1[fi].setAlpha(1.0f);
                    profile2[fi].setEnabled(false);
                    for (int j = 0; j < 5; j++) {
                        // Skip the button that was clicked
                        if (j != fi) {
                            profile1[j].setAlpha(0.5f);
                        }
                    }
                }
            });

            profile2[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player2 = viewModel.getProfile(profile2[fi].getText().toString());
                    profile2[fi].setAlpha(1.0f);
                    profile1[fi].setEnabled(false);
                    for (int j = 0; j < 5; j++) {
                        // Skip the button that was clicked
                        if (j != fi) {
                            profile2[j].setAlpha(0.5f);
                        }
                    }
                }
            });
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setPlayer1(player1);
                viewModel.setPlayer2(player2);
                Log.d("SelectP2","p1: "+player1.getName());
                Log.d("SelectP2","p2: "+player2.getName());
                loadModeFrag();
            }
        });
        return rootView;
    }

    private void loadModeFrag(){
        ModeFragment modeFragment = new ModeFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameMenu, modeFragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}