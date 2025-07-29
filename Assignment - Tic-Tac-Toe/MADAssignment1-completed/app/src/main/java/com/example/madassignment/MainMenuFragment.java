package com.example.madassignment;

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
import android.widget.Toast;

public class MainMenuFragment extends Fragment {

    public MainMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);///testing1/4
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
        Button pvpButton = rootView.findViewById(R.id.buttonPVP);
        Button pvcButton = rootView.findViewById(R.id.buttonPVC);
        Button createProfileButton = rootView.findViewById(R.id.buttonProfile);
        Button leaderboardButton = rootView.findViewById(R.id.buttonLeaderboard);

        pvpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setGameAgainst(true);
                if(viewModel.getProfileCount() < 2){
                    Log.d("profilez","1ig"+viewModel.getProfileCount());
                    loadProfileFrag();
                }else {
                    loadSelectPlayer2Frag();
                }
            }
        });

        pvcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setGameAgainst(false);
                if(viewModel.getProfileCount() == 0){
                    Log.d("profilez","1ig"+viewModel.getProfileCount());
                    loadProfileFrag();
                }else {
                    loadSelectPlayer1FragOnPVC();
                }
            }
        });

        createProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewModel.getProfileCount() < 5) {
                    loadProfileFrag();
                } else {
                    Toast.makeText(getContext(), "Maximum account number reached!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewModel.getProfileCount() == 0){
                    Toast.makeText(requireActivity(), "No accounts created!", Toast.LENGTH_SHORT).show();
                }else {
                    loadSelectPlayer1FragOnLeaderboard();
                }
            }
        });

        return rootView;
    }

    private void loadSelectPlayer1FragOnPVC(){
        SelectPlayer1Fragment selectPlayer1Fragment = new SelectPlayer1Fragment();

        Bundle args = new Bundle();
        args.putString("load_fragment", "game");
        selectPlayer1Fragment.setArguments(args);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameMenu, selectPlayer1Fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loadSelectPlayer1FragOnLeaderboard(){
        SelectPlayer1Fragment selectPlayer1Fragment = new SelectPlayer1Fragment();

        Bundle args = new Bundle();
        args.putString("load_fragment", "leaderboard");
        selectPlayer1Fragment.setArguments(args);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameMenu, selectPlayer1Fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loadSelectPlayer2Frag(){
        SelectPlayer2Fragment selectPlayer2Fragment = new SelectPlayer2Fragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameMenu, selectPlayer2Fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loadProfileFrag(){
        ProfileFragment profileFragment = new ProfileFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameMenu, profileFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void loadLeaderboardFrag(){
        LeaderboardFragment leaderboardFragment = new LeaderboardFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameMenu, leaderboardFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}