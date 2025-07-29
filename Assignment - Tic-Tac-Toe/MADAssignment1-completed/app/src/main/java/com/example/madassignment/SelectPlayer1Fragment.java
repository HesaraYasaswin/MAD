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
public class SelectPlayer1Fragment extends Fragment {

    private SharedViewModel viewModel;
    private Profile player;
    private String loadFrag;

    public SelectPlayer1Fragment(){
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_select_player1, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        Bundle args = getArguments();
        if (args != null) {
            loadFrag = args.getString("load_fragment");
        }

        Button[] profiles = new Button[5];
        int count = viewModel.getProfileCount();
        MutableLiveData<ArrayList<Profile>> profileList = viewModel.getProfileList();

        for (int i = 0; i < 5; i++) {
            String buttonID = "buttonPlayer1Profile" + (i+1);
            int resID = getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());
            profiles[i] = rootView.findViewById(resID);
            profiles[i].setAlpha(0.5f);
            profiles[i].setVisibility(View.INVISIBLE);
            profiles[i].setEnabled(false);
        }
        // Observe the LiveData
        profileList.observe(getViewLifecycleOwner(), profile -> {
            // Handle changes to the profile list here
            for(int i=0;i<count;i++){
                final int fi = i;
                profiles[i].setEnabled(true);
                profiles[i].setText(profile.get(i).getName());
                profiles[i].setVisibility(View.VISIBLE);
                profiles[i].setAlpha(1.0f);
            }
        });
        for(int i = 0; i<5;i++){
            final int fi = i;
            profiles[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    player = viewModel.getProfile(profiles[fi].getText().toString());
                    profiles[fi].setAlpha(1.0f);
                    for (int j = 0; j < 5; j++) {
                        // Skip the button that was clicked
                        if (j != fi) {
                            profiles[j].setAlpha(0.5f);
                        }
                    }
                    viewModel.setPlayer1(player);
                    Log.d("SelectP1","p1: "+player.getName());
                    if(loadFrag.equals("game")){
                        loadModeFrag();
                    }else if(loadFrag.equals("leaderboard")){
                        loadLeaderboardFrag();
                    }
                }
            });
        }
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

    private void loadLeaderboardFrag(){
        LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameMenu, leaderboardFragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}