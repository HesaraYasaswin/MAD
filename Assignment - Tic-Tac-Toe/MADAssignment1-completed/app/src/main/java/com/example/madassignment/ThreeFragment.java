package com.example.madassignment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Random;
import java.util.Stack;

public class ThreeFragment extends Fragment {

    public ThreeFragment() {
    }

    SharedViewModel viewModel;
    ImageButton[][] threeBoard;
    private static final int ROW_OF_BUTTONS = 3;
    private static final int COL_OF_BUTTONS = 3;
    private boolean turn = true;
    private int winCondition = 3;
    private boolean gameAgainst = true;
    private Random rand = new Random();
    private Bitmap x;
    private Bitmap o;
    private Stack<Move> moveStack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_three, container, false);
        moveStack = new Stack<>();
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        threeBoard = new ImageButton[ROW_OF_BUTTONS][COL_OF_BUTTONS];
        if (viewModel.getPlayer2() == null) {
            x = viewModel.getPlayer1().getXMarker();
        } else {
            x = viewModel.getPlayer1().getXMarker();
            o = viewModel.getPlayer2().getOMarker();
        }

        gameAgainst = viewModel.getGameAgainst().getValue();
        winCondition = viewModel.getWinCondition().getValue();

        for (int ir = 0; ir < ROW_OF_BUTTONS; ir++) {
            for (int ic = 0; ic < COL_OF_BUTTONS; ic++) {
                String buttonID = "imageButton" + (ir + 1) + (ic + 1);
                int resID = getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());
                threeBoard[ir][ic] = rootView.findViewById(resID);
                threeBoard[ir][ic].setTag("n");
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

                threeBoard[ir][ic].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!gameAgainst) {
                            threeBoard[fir][fic].setImageBitmap(x);
                            threeBoard[fir][fic].setTag("x");
                            threeBoard[fir][fic].setEnabled(false);

                            if (checkWinCondition()) {
                                disableAllButtons();
                                viewModel.addWin(viewModel.getPlayer1().getName());
                                showEndGamePopup("GAME OVER!", "You WIN!!");
                            } else if (checkDrawCondition()) {
                                disableAllButtons();
                                showEndGamePopup("GAME OVER!", "Game is a DRAW");
                                viewModel.addDraw(viewModel.getPlayer1().getName());
                            } else {
                                turn = false;
                                int x;
                                int y;
                                do {
                                    x = randomNumberGenerator();
                                    y = randomNumberGenerator();
                                } while (!threeBoard[x][y].getTag().equals("n"));
                                threeBoard[x][y].setImageResource(R.drawable.o1);
                                threeBoard[x][y].setTag("y");
                                threeBoard[x][y].setEnabled(false);

                                if (checkWinCondition()) {
                                    disableAllButtons();
                                    viewModel.addLoss(viewModel.getPlayer1().getName());
                                    showEndGamePopup("GAME OVER!", "Player 2 WINS!!");
                                } else if (checkDrawCondition()) {
                                    disableAllButtons();
                                    viewModel.addDraw(viewModel.getPlayer1().getName());
                                    showEndGamePopup("GAME OVER!", "Game is a DRAW");
                                }
                                turn = true;
                            }
                        } else {
                            if (turn) {
                                threeBoard[fir][fic].setImageBitmap(x);
                                threeBoard[fir][fic].setTag("x");
                                threeBoard[fir][fic].setEnabled(false);
                                if (checkWinCondition()) {
                                    disableAllButtons();
                                    viewModel.addWin(viewModel.getPlayer1().getName());
                                    viewModel.addLoss(viewModel.getPlayer2().getName());
                                    showEndGamePopup("GAME OVER!", "Player 1 WINS!!");
                                } else if (checkDrawCondition()) {
                                    disableAllButtons();
                                    viewModel.addDraw(viewModel.getPlayer1().getName());
                                    viewModel.addDraw(viewModel.getPlayer2().getName());
                                    showEndGamePopup("GAME OVER!", "Game is a DRAW");
                                }
                                turn = false;
                            } else {
                                threeBoard[fir][fic].setImageBitmap(o);
                                threeBoard[fir][fic].setTag("y");
                                threeBoard[fir][fic].setEnabled(false);
                                if (checkWinCondition()) {
                                    disableAllButtons();
                                    viewModel.addLoss(viewModel.getPlayer1().getName());
                                    viewModel.addWin(viewModel.getPlayer2().getName());
                                    showEndGamePopup("GAME OVER!", "Player 2 WINS!!");
                                } else if (checkDrawCondition()) {
                                    disableAllButtons();
                                    viewModel.addDraw(viewModel.getPlayer1().getName());
                                    viewModel.addDraw(viewModel.getPlayer2().getName());
                                    showEndGamePopup("GAME OVER!", "Game is a DRAW");
                                }
                                turn = true;
                            }
                        }

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
                threeBoard[ir][ic].setEnabled(false);
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

        for (int i = 0; i < ROW_OF_BUTTONS; i++) {
            if (threeBoard[i][0].getTag().equals(currentPlayer) &&
                    threeBoard[i][1].getTag().equals(currentPlayer) &&
                    threeBoard[i][2].getTag().equals(currentPlayer)) {
                return true;
            }
        }

        for (int i = 0; i < COL_OF_BUTTONS; i++) {
            if (threeBoard[0][i].getTag().equals(currentPlayer) &&
                    threeBoard[1][i].getTag().equals(currentPlayer) &&
                    threeBoard[2][i].getTag().equals(currentPlayer)) {
                return true;
            }
        }

        if (threeBoard[0][0].getTag().equals(currentPlayer) &&
                threeBoard[1][1].getTag().equals(currentPlayer) &&
                threeBoard[2][2].getTag().equals(currentPlayer)) {
            return true;
        }

        if (threeBoard[0][2].getTag().equals(currentPlayer) &&
                threeBoard[1][1].getTag().equals(currentPlayer) &&
                threeBoard[2][0].getTag().equals(currentPlayer)) {
            return true;
        }

        return false;
    }

    private boolean checkDrawCondition() {
        for (int ir = 0; ir < ROW_OF_BUTTONS; ir++) {
            for (int ic = 0; ic < COL_OF_BUTTONS; ic++) {
                if (threeBoard[ir][ic].getTag().equals("n")) {
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
                        ThreeFragment newThreeFrag = new ThreeFragment();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.FrameBoard, newThreeFrag);
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

    private int randomNumberGenerator() {
        return rand.nextInt(3);
    }

    private void undoLastMove() {
        if (!moveStack.isEmpty()) {
            Move lastMove = moveStack.pop();
            int row = lastMove.row;
            int col = lastMove.col;
            //threeBoard[row][col].setImageResource(android.R.color.transparent);
            threeBoard[row][col].setImageBitmap(null);
            threeBoard[row][col].setBackgroundColor(Color.WHITE);
            threeBoard[row][col].setTag("n");
            threeBoard[row][col].setEnabled(true);
            turn = !turn;
        }
    }

    private void resetBoard() {
        for (int ir = 0; ir < ROW_OF_BUTTONS; ir++) {
            for (int ic = 0; ic < COL_OF_BUTTONS; ic++) {
                //threeBoard[ir][ic].setImageResource(android.R.color.transparent);
                threeBoard[ir][ic].setImageBitmap(null);
                threeBoard[ir][ic].setBackgroundColor(Color.WHITE);
                threeBoard[ir][ic].setTag("n");
                threeBoard[ir][ic].setEnabled(true);
            }
        }
        moveStack.clear();
        turn = true;
    }



}
