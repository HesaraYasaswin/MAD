package com.example.madassignment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Random;
import java.util.Stack;

public class FourFragment extends Fragment {

    SharedViewModel viewModel;
    ImageButton[][] fourBoard;
    private static final int ROW_OF_BUTTONS = 4;
    private static final int COL_OF_BUTTONS = 4;
    private boolean turn = true;
    private int winCondition = 4;
    private boolean gameAgainst = true;
    private Random rand = new Random();
    private boolean gameOver = false;
    private Bitmap x;
    private Bitmap o;
    private Stack<Move> moveStack; // Declare a Stack for moves

    public FourFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_four, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        fourBoard = new ImageButton[ROW_OF_BUTTONS][COL_OF_BUTTONS];
        if(viewModel.getPlayer2() == null){
            x = viewModel.getPlayer1().getXMarker();
        }else{
            x = viewModel.getPlayer1().getXMarker();
            o = viewModel.getPlayer2().getOMarker();
        }

        gameAgainst = viewModel.getGameAgainst().getValue();
        winCondition = viewModel.getWinCondition().getValue();

        // Initialize the stack for moves
        moveStack = new Stack<>();

        for (int ir = 0; ir < ROW_OF_BUTTONS; ir++) {
            for (int ic = 0; ic < COL_OF_BUTTONS; ic++) {
                String buttonID = "imageButton" + (ir+1) + (ic+1);
                int resID = getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());
                fourBoard[ir][ic] = rootView.findViewById(resID);
                fourBoard[ir][ic].setTag("n");
            }
        }

        viewModel.getTriggerUndo().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean triggerUndo) {
                if (triggerUndo) {
                    undoLastMove();
                    viewModel.setTriggerUndo(false);
                }
            }
        });

        viewModel.getTriggerReset().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean triggerReset) {
                if (triggerReset) {
                    resetBoard();
                    viewModel.setTriggerReset(false);
                }
            }
        });

        for (int ir = 0; ir < ROW_OF_BUTTONS; ir++) {
            for (int ic = 0; ic < COL_OF_BUTTONS; ic++) {
                final int fir = ir;
                final int fic = ic;

                fourBoard[ir][ic].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TicTacToe", "button clicked");
                        if(!gameAgainst) {//Against AI
                            fourBoard[fir][fic].setImageBitmap(x);
                            fourBoard[fir][fic].setTag("x");
                            fourBoard[fir][fic].setEnabled(false);
                            Log.d("TicTacToe", "Player moved to position: " + fir + ", " + fic);
                            if(winCondition == 4){
                                if(checkWinCondition()){
                                    disableAllButtons();
                                    viewModel.addWin(viewModel.getPlayer1().getName());
                                    gameOver = true;
                                    showEndGamePopup("GAME OVER!","You WIN!!");
                                } else if(checkDrawCondition()){
                                    disableAllButtons();
                                    viewModel.addDraw(viewModel.getPlayer1().getName());
                                    gameOver = true;
                                    showEndGamePopup("GAME OVER!","Game is a DRAW");
                                }
                            } else if(winCondition == 3) {
                                if(checkWinCondition3()){
                                    disableAllButtons();
                                    viewModel.addWin(viewModel.getPlayer1().getName());
                                    gameOver = true;
                                    showEndGamePopup("GAME OVER!","You WIN!!");
                                } else if(checkDrawCondition()){
                                    disableAllButtons();
                                    viewModel.addDraw(viewModel.getPlayer1().getName());
                                    gameOver = true;
                                    showEndGamePopup("GAME OVER!","Game is a DRAW");
                                }
                            }if(!gameOver){
                                turn = false;
                                int x;
                                int y;
                                do {
                                    x = randomNumberGenerator();
                                    y = randomNumberGenerator();
                                }
                                while(!fourBoard[x][y].getTag().equals("n"));
                                fourBoard[x][y].setImageResource(R.drawable.o1);
                                Log.d("TicTacToe", "AI moved to position: " + x + ", " + y);
                                fourBoard[x][y].setTag("y");
                                fourBoard[x][y].setEnabled(false);

                                if(winCondition == 4 && checkWinCondition()){
                                    disableAllButtons();
                                    viewModel.addLoss(viewModel.getPlayer1().getName());
                                    showEndGamePopup("GAME OVER!","Player 2 WINS!!");
                                }else if(winCondition == 3 && checkWinCondition3()){
                                    disableAllButtons();
                                    viewModel.addLoss(viewModel.getPlayer1().getName());
                                    showEndGamePopup("GAME OVER!","Player 2 WINS!!");
                                } else if(checkDrawCondition()){
                                    disableAllButtons();
                                    viewModel.addDraw(viewModel.getPlayer1().getName());
                                    showEndGamePopup("GAME OVER!","Game is a DRAW");
                                }
                                turn = true;
                            }
                        }
                        else {//Against Player//////////////////
                            if(turn) {
                                fourBoard[fir][fic].setImageBitmap(x);
                                fourBoard[fir][fic].setTag("x");
                                fourBoard[fir][fic].setEnabled(false);
                                if(winCondition == 4){
                                    if(checkWinCondition()){
                                        disableAllButtons();
                                        viewModel.addWin(viewModel.getPlayer1().getName());
                                        viewModel.addLoss(viewModel.getPlayer2().getName());
                                        showEndGamePopup("GAME OVER!","Player 1 WINS!!");
                                    } else if(checkDrawCondition()){
                                        disableAllButtons();
                                        viewModel.addDraw(viewModel.getPlayer1().getName());
                                        viewModel.addDraw(viewModel.getPlayer2().getName());
                                        showEndGamePopup("GAME OVER!","Game is a DRAW");
                                    }
                                } else if(winCondition == 3) {
                                    if(checkWinCondition3()){
                                        disableAllButtons();
                                        viewModel.addWin(viewModel.getPlayer1().getName());
                                        viewModel.addLoss(viewModel.getPlayer2().getName());
                                        showEndGamePopup("GAME OVER!","Player 1 WINS!!");
                                    } else if(checkDrawCondition()){
                                        disableAllButtons();
                                        viewModel.addDraw(viewModel.getPlayer1().getName());
                                        viewModel.addDraw(viewModel.getPlayer2().getName());
                                        showEndGamePopup("GAME OVER!","Game is a DRAW");
                                    }
                                }
                                turn = false;
                            }
                            else {
                                fourBoard[fir][fic].setImageBitmap(o);
                                fourBoard[fir][fic].setTag("y");
                                fourBoard[fir][fic].setEnabled(false);
                                if(winCondition == 4){
                                    if(checkWinCondition()){
                                        disableAllButtons();
                                        viewModel.addWin(viewModel.getPlayer2().getName());
                                        viewModel.addLoss(viewModel.getPlayer1().getName());
                                        showEndGamePopup("GAME OVER!","Player 2 WINS!!");
                                    } else if(checkDrawCondition()){
                                        disableAllButtons();
                                        viewModel.addDraw(viewModel.getPlayer1().getName());
                                        viewModel.addDraw(viewModel.getPlayer2().getName());
                                        showEndGamePopup("GAME OVER!","Game is a DRAW");
                                    }
                                } else if(winCondition == 3) {
                                    if(checkWinCondition3()){
                                        disableAllButtons();
                                        viewModel.addWin(viewModel.getPlayer2().getName());
                                        viewModel.addLoss(viewModel.getPlayer1().getName());
                                        showEndGamePopup("GAME OVER!","Player 2 WINS!!");
                                    } else if(checkDrawCondition()){
                                        disableAllButtons();
                                        viewModel.addDraw(viewModel.getPlayer1().getName());
                                        viewModel.addDraw(viewModel.getPlayer2().getName());
                                        showEndGamePopup("GAME OVER!","Game is a DRAW");
                                    }
                                }
                                turn = true;
                            }
                        }

                        // Store the move in the stack for undo
                        moveStack.push(new Move(fir, fic));
                    }
                });
            }
        }

        return rootView;
    }
    private void disableAllButtons() {
        for (int ir = 0; ir < ROW_OF_BUTTONS; ir++) {
            for (int ic = 0; ic < COL_OF_BUTTONS; ic++) {
                fourBoard[ir][ic].setEnabled(false);
            }
        }
    }
    private boolean checkWinCondition() {
        String currentPlayer;
        if (turn) {
            currentPlayer = "x";
        } else {
            currentPlayer = "y";
        }

        // Check rows
        for (int i = 0; i < ROW_OF_BUTTONS; i++) {
            if (fourBoard[i][0].getTag().equals(currentPlayer) &&
                    fourBoard[i][1].getTag().equals(currentPlayer) &&
                    fourBoard[i][2].getTag().equals(currentPlayer) &&
                    fourBoard[i][3].getTag().equals(currentPlayer)) {
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < COL_OF_BUTTONS; i++) {
            if (fourBoard[0][i].getTag().equals(currentPlayer) &&
                    fourBoard[1][i].getTag().equals(currentPlayer) &&
                    fourBoard[2][i].getTag().equals(currentPlayer) &&
                    fourBoard[3][i].getTag().equals(currentPlayer)) {
                return true;
            }
        }

        // Check main diagonal (from top-left to bottom-right)
        if (fourBoard[0][0].getTag().equals(currentPlayer) &&
                fourBoard[1][1].getTag().equals(currentPlayer) &&
                fourBoard[2][2].getTag().equals(currentPlayer) &&
                fourBoard[3][3].getTag().equals(currentPlayer)) {
            return true;
        }

        // Check other diagonal (from top-right to bottom-left)
        if (fourBoard[0][3].getTag().equals(currentPlayer) &&
                fourBoard[1][2].getTag().equals(currentPlayer) &&
                fourBoard[2][1].getTag().equals(currentPlayer) &&
                fourBoard[3][0].getTag().equals(currentPlayer)) {
            return true;
        }

        return false;
    }

    private boolean checkWinCondition3() {
        String currentPlayer;
        if (turn) {
            currentPlayer = "x";
        } else {
            currentPlayer = "y";
        }

        // Check rows
        for (int i = 0; i < ROW_OF_BUTTONS; i++) {
            for (int j = 0; j < 2; j++) { // 2 sets of 3 consecutive marks in a 4x4 row
                if (fourBoard[i][j].getTag().equals(currentPlayer) &&
                        fourBoard[i][j+1].getTag().equals(currentPlayer) &&
                        fourBoard[i][j+2].getTag().equals(currentPlayer)) {
                    return true;
                }
            }
        }

        // Check columns
        for (int i = 0; i < COL_OF_BUTTONS; i++) {
            for (int j = 0; j < 2; j++) { // 2 sets of 3 consecutive marks in a 4x4 column
                if (fourBoard[j][i].getTag().equals(currentPlayer) &&
                        fourBoard[j+1][i].getTag().equals(currentPlayer) &&
                        fourBoard[j+2][i].getTag().equals(currentPlayer)) {
                    return true;
                }
            }
        }

        // Check primary diagonals
        for (int i = 0; i < 2; i++) {
            if (fourBoard[i][i].getTag().equals(currentPlayer) &&
                    fourBoard[i+1][i+1].getTag().equals(currentPlayer) &&
                    fourBoard[i+2][i+2].getTag().equals(currentPlayer)) {
                return true;
            }
        }

        // Check secondary diagonals
        for (int i = 0; i < 2; i++) {
            if (fourBoard[i][3-i].getTag().equals(currentPlayer) &&
                    fourBoard[i+1][2-i].getTag().equals(currentPlayer) &&
                    fourBoard[i+2][1-i].getTag().equals(currentPlayer)) {
                return true;
            }
        }

        // Upper-right diagonal
        if (fourBoard[0][1].getTag().equals(currentPlayer) &&
                fourBoard[1][2].getTag().equals(currentPlayer) &&
                fourBoard[2][3].getTag().equals(currentPlayer)) {
            return true;
        }

        // Lower-left diagonal
        if (fourBoard[1][0].getTag().equals(currentPlayer) &&
                fourBoard[2][1].getTag().equals(currentPlayer) &&
                fourBoard[3][2].getTag().equals(currentPlayer)) {
            return true;
        }

        // Upper-left diagonal
        if (fourBoard[0][2].getTag().equals(currentPlayer) &&
                fourBoard[1][1].getTag().equals(currentPlayer) &&
                fourBoard[2][0].getTag().equals(currentPlayer)) {
            return true;
        }

        // Lower-right diagonal
        if (fourBoard[2][3].getTag().equals(currentPlayer) &&
                fourBoard[3][2].getTag().equals(currentPlayer) &&
                fourBoard[3][1].getTag().equals(currentPlayer)) {
            return true;
        }
        return false;
    }

    private boolean checkDrawCondition() {
        // Check if all cells are filled
        for (int ir = 0; ir < ROW_OF_BUTTONS; ir++) {
            for (int ic = 0; ic < COL_OF_BUTTONS; ic++) {
                if (fourBoard[ir][ic].getTag().equals("n")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showEndGamePopup(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FourFragment newFourFrag = new FourFragment();
                        Bundle args = new Bundle();
                        args.putInt("WIN_CONDITION", winCondition);
                        args.putBoolean("gameAgainst", gameAgainst);
                        newFourFrag.setArguments(args);
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.FrameBoard, newFourFrag);
                        fragmentTransaction.commit();

                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        startActivity(intent);
                    }
                }).setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void undoLastMove() {
        if (!moveStack.isEmpty()) {
            Move lastMove = moveStack.pop();
            int row = lastMove.row;
            int col = lastMove.col;
            fourBoard[row][col].setImageBitmap(null);
            fourBoard[row][col].setBackgroundColor(Color.WHITE);
            fourBoard[row][col].setTag("n");
            fourBoard[row][col].setEnabled(true);
        }
    }

    private void resetBoard() {
        for (int ir = 0; ir < ROW_OF_BUTTONS; ir++) {
            for (int ic = 0; ic < COL_OF_BUTTONS; ic++) {
                fourBoard[ir][ic].setImageBitmap(null);
                fourBoard[ir][ic].setBackgroundColor(Color.WHITE);
                fourBoard[ir][ic].setTag("n");
                fourBoard[ir][ic].setEnabled(true);
            }
        }
        moveStack.clear();
        turn = true;
    }

    private int randomNumberGenerator(){
        return rand.nextInt(4);
    }
}