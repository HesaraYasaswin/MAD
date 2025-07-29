package com.example.madassignment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LeaderboardFragment extends Fragment {

    private SharedViewModel viewModel;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        ImageView avatar = rootView.findViewById(R.id.imageViewAvatar);
        Button okButton = rootView.findViewById(R.id.buttonOK);
        TextView name = rootView.findViewById(R.id.textViewName);
        TextView winCount = rootView.findViewById(R.id.textViewWinCount);
        TextView drawCount = rootView.findViewById(R.id.textViewDrawCount);
        TextView lossCount = rootView.findViewById(R.id.textViewLossCount);
        TextView winPercentage = rootView.findViewById(R.id.textViewWinPercentage);

        if(viewModel.getPlayer1() != null){
            Log.d("leader","after if");

            Profile player = viewModel.getPlayer1();
            String playerName = player.getName();
            int playerWins = player.getWin();
            int playerDraws = player.getDraw();
            int playerLosses = player.getLoss();
            double playerWinPercentage = 0;
            int totalGames = playerWins + playerDraws + playerLosses;
            if (totalGames != 0) {
                playerWinPercentage = ((double) playerWins / totalGames) * 100;
            }

            name.setText(playerName);
            winCount.setText("WINS   : "+playerWins);
            drawCount.setText("DRAWS  : "+playerDraws);
            lossCount.setText("LOSSES : "+playerLosses);
            winPercentage.setText("WIN %  : "+String.format("%.2f", playerWinPercentage));
            avatar.setImageBitmap(player.getAvatar());

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), MenuActivity.class);
                    startActivity(intent);
                }
            });
        }

        return rootView;
    }
}