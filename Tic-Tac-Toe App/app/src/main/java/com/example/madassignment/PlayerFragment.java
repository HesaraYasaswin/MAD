package com.example.madassignment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerFragment extends Fragment {

    private SharedViewModel viewModel;

    public PlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        TextView nameView = rootView.findViewById(R.id.textViewPlayerName);
        TextView timerView = rootView.findViewById(R.id.textViewTimer);
        ImageView avatarView = rootView.findViewById(R.id.imageViewPlayerAvatar);

        timerView.setVisibility(View.INVISIBLE);
        int player = 0;

        Bundle args = getArguments();
        if (args != null) {
            player = args.getInt("player");
        }

        if(viewModel.getGameAgainst().getValue() == true){
            if(player == 1){
                nameView.setText(viewModel.getPlayer1().getName());
                avatarView.setImageBitmap(viewModel.getPlayer1().getAvatar());
            } else if(player == 2){
                nameView.setText(viewModel.getPlayer2().getName());
                avatarView.setImageBitmap(viewModel.getPlayer2().getAvatar());
            }
        } else{
            if(player == 1){
                nameView.setText(viewModel.getPlayer1().getName());
                avatarView.setImageBitmap(viewModel.getPlayer1().getAvatar());
            } else if(player == 2){
                nameView.setText("AI");
                avatarView.setImageResource(R.drawable.ai);
            }
        }

        return rootView;
    }
}