package com.example.madassignment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.Random;
import java.util.Stack;

public class FiveFragment extends Fragment {

    public FiveFragment() {
        // Required empty public constructor
    }

    private SharedViewModel viewModel;
    ImageButton[][] fiveBoard;
    private static final int ROW_OF_BUTTONS = 5;
    private static final int COL_OF_BUTTONS = 5;
    private boolean turn = true;
    private int winCondition = 5;
    private boolean gameAgainst = true;
    private Random rand = new Random();
    private boolean gameOver = false;
    private Bitmap x;
    private Bitmap o;
    private Stack<Move> moveStack; // Declare a Stack for moves

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_five, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        fiveBoard = new ImageButton[ROW_OF_BUTTONS][COL_OF_BUTTONS];
        if (viewModel.getPlayer2() == null) {
            x = viewModel.getPlayer1().getXMarker();
        } else {
            x = viewModel.getPlayer1().getXMarker();
            o = viewModel.getPlayer2().getOMarker();
        }

        gameAgainst = viewModel.getGameAgainst().getValue();
        winCondition = viewModel.getWinCondition().getValue();

        // Initialize the stack for moves
        moveStack = new Stack<>();

        for (int ir = 0; ir < ROW_OF_BUTTONS; ir++) {
            for (int ic = 0; ic < COL_OF_BUTTONS; ic++) {
                String buttonID = "imageButton" + (ir + 1) + (ic + 1);
                int resID = getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());
                fiveBoard[ir][ic] = rootView.findViewById(resID);
                fiveBoard[ir][ic].setTag("n");
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

                fiveBoard[ir][ic].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("TicTacToe", "button clicked");
                        if (!gameAgainst) {//Against AI
                            fiveBoard[fir][fic].setImageBitmap(x);
                            fiveBoard[fir][fic].setTag("x");
                            fiveBoard[fir][fic].setEnabled(false);
                            Log.d("TicTacToe", "Player moved to position: " + fir + ", " + fic);
                            if (winCondition == 5) {
                                if (checkWinCondition()) {
                                    disableAllButtons();
                                    viewModel.addWin(viewModel.getPlayer1().getName());
                                    gameOver = true;
                                    showEndGamePopup("GAME OVER!", "You WIN!!");
                                } else if (checkDrawCondition()) {
                                    disableAllButtons();
                                    viewModel.addDraw(viewModel.getPlayer1().getName());
                                    gameOver = true;
                                    showEndGamePopup("GAME OVER!", "Game is a DRAW");
                                }
                            } else if (winCondition == 4) {
                                if (checkWinCondition4()) {
                                    disableAllButtons();
                                    viewModel.addWin(viewModel.getPlayer1().getName());
                                    gameOver = true;
                                    showEndGamePopup("GAME OVER!", "You WIN!!");
                                } else if (checkDrawCondition()) {
                                    disableAllButtons();
                                    viewModel.addDraw(viewModel.getPlayer1().getName());
                                    gameOver = true;
                                    showEndGamePopup("GAME OVER!", "Game is a DRAW");
                                }
                            } else if (winCondition == 3) {
                                if (checkWinCondition3()) {
                                    disableAllButtons();
                                    viewModel.addWin(viewModel.getPlayer1().getName());
                                    gameOver = true;
                                    showEndGamePopup("GAME OVER!", "You WIN!!");
                                } else if (checkDrawCondition()) {
                                    disableAllButtons();
                                    viewModel.addDraw(viewModel.getPlayer1().getName());
                                    gameOver = true;
                                    showEndGamePopup("GAME OVER!", "Game is a DRAW");
                                }
                            }
                            if (!gameOver) {
                                turn = false;
                                int x;
                                int y;
                                do {
                                    x = randomNumberGenerator();
                                    y = randomNumberGenerator();
                                }
                                while (!fiveBoard[x][y].getTag().equals("n"));
                                fiveBoard[x][y].setImageResource(R.drawable.o1);
                                Log.d("TicTacToe", "AI moved to position: " + x + ", " + y);
                                fiveBoard[x][y].setTag("y");
                                fiveBoard[x][y].setEnabled(false);

                                if (winCondition == 5 && checkWinCondition()) {
                                    disableAllButtons();
                                    viewModel.addLoss(viewModel.getPlayer1().getName());
                                    showEndGamePopup("GAME OVER!", "Player 2 WINS!!");
                                } else if (winCondition == 4 && checkWinCondition4()) {
                                    disableAllButtons();
                                    viewModel.addLoss(viewModel.getPlayer1().getName());
                                    showEndGamePopup("GAME OVER!", "Player 2 WINS!!");
                                } else if (winCondition == 3 && checkWinCondition3()) {
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
                        } else {//Against Player//////////////////
                            if (turn) {
                                fiveBoard[fir][fic].setImageBitmap(x);
                                fiveBoard[fir][fic].setTag("x");
                                fiveBoard[fir][fic].setEnabled(false);
                                if (winCondition == 5) {
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
                                } else if (winCondition == 4) {
                                    if (checkWinCondition4()) {
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
                                } else if (winCondition == 3) {
                                    if (checkWinCondition3()) {
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
                                }
                                turn = false;
                            } else {
                                fiveBoard[fir][fic].setImageBitmap(o);
                                fiveBoard[fir][fic].setTag("y");
                                fiveBoard[fir][fic].setEnabled(false);
                                if (winCondition == 5) {
                                    if (checkWinCondition()) {
                                        disableAllButtons();
                                        viewModel.addWin(viewModel.getPlayer2().getName());
                                        viewModel.addLoss(viewModel.getPlayer1().getName());
                                        showEndGamePopup("GAME OVER!", "Player 2 WINS!!");
                                    } else if (checkDrawCondition()) {
                                        disableAllButtons();
                                        viewModel.addDraw(viewModel.getPlayer1().getName());
                                        viewModel.addDraw(viewModel.getPlayer2().getName());
                                        showEndGamePopup("GAME OVER!", "Game is a DRAW");
                                    }
                                } else if (winCondition == 4) {
                                    if (checkWinCondition4()) {
                                        disableAllButtons();
                                        viewModel.addWin(viewModel.getPlayer2().getName());
                                        viewModel.addLoss(viewModel.getPlayer1().getName());
                                        showEndGamePopup("GAME OVER!", "Player 2 WINS!!");
                                    } else if (checkDrawCondition()) {
                                        disableAllButtons();
                                        viewModel.addDraw(viewModel.getPlayer1().getName());
                                        viewModel.addDraw(viewModel.getPlayer2().getName());
                                        showEndGamePopup("GAME OVER!", "Game is a DRAW");
                                    }
                                } else if (winCondition == 3) {
                                    if (checkWinCondition3()) {
                                        disableAllButtons();
                                        viewModel.addWin(viewModel.getPlayer2().getName());
                                        viewModel.addLoss(viewModel.getPlayer1().getName());
                                        showEndGamePopup("GAME OVER!", "Player 2 WINS!!");
                                    } else if (checkDrawCondition()) {
                                        disableAllButtons();
                                        viewModel.addDraw(viewModel.getPlayer1().getName());
                                        viewModel.addDraw(viewModel.getPlayer2().getName());
                                        showEndGamePopup("GAME OVER!", "Game is a DRAW");
                                    }
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
                fiveBoard[ir][ic].setEnabled(false);
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
            if (fiveBoard[i][0].getTag().equals(currentPlayer) &&
                    fiveBoard[i][1].getTag().equals(currentPlayer) &&
                    fiveBoard[i][2].getTag().equals(currentPlayer) &&
                    fiveBoard[i][3].getTag().equals(currentPlayer) &&
                    fiveBoard[i][4].getTag().equals(currentPlayer)) {
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < COL_OF_BUTTONS; i++) {
            if (fiveBoard[0][i].getTag().equals(currentPlayer) &&
                    fiveBoard[1][i].getTag().equals(currentPlayer) &&
                    fiveBoard[2][i].getTag().equals(currentPlayer) &&
                    fiveBoard[3][i].getTag().equals(currentPlayer) &&
                    fiveBoard[4][i].getTag().equals(currentPlayer)) {
                return true;
            }
        }

        // Check main diagonal (from top-left to bottom-right)
        if (fiveBoard[0][0].getTag().equals(currentPlayer) &&
                fiveBoard[1][1].getTag().equals(currentPlayer) &&
                fiveBoard[2][2].getTag().equals(currentPlayer) &&
                fiveBoard[3][3].getTag().equals(currentPlayer) &&
                fiveBoard[4][4].getTag().equals(currentPlayer)) {
            return true;
        }

        // Check other diagonal (from top-right to bottom-left)
        if (fiveBoard[0][4].getTag().equals(currentPlayer) &&
                fiveBoard[1][3].getTag().equals(currentPlayer) &&
                fiveBoard[2][2].getTag().equals(currentPlayer) &&
                fiveBoard[3][1].getTag().equals(currentPlayer) &&
                fiveBoard[4][0].getTag().equals(currentPlayer)) {
            return true;
        }

        return false;
    }

    private boolean checkWinCondition4() {
        String currentPlayer;
        if (turn) {
            currentPlayer = "x";
        } else {
            currentPlayer = "y";
        }

        // Check rows
        for (int i = 0; i < ROW_OF_BUTTONS; i++) {
            for (int j = 0; j < 2; j++) {
                if (fiveBoard[i][j].getTag().equals(currentPlayer) &&
                        fiveBoard[i][j + 1].getTag().equals(currentPlayer) &&
                        fiveBoard[i][j + 2].getTag().equals(currentPlayer) &&
                        fiveBoard[i][j + 3].getTag().equals(currentPlayer)) {
                    return true;
                }
            }
        }

        // Check columns
        for (int i = 0; i < COL_OF_BUTTONS; i++) {
            for (int j = 0; j < 2; j++) {
                if (fiveBoard[j][i].getTag().equals(currentPlayer) &&
                        fiveBoard[j + 1][i].getTag().equals(currentPlayer) &&
                        fiveBoard[j + 2][i].getTag().equals(currentPlayer) &&
                        fiveBoard[j + 3][i].getTag().equals(currentPlayer)) {
                    return true;
                }
            }
        }

        // Check main diagonal (from top-left to bottom-right)
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (fiveBoard[i][j].getTag().equals(currentPlayer) &&
                        fiveBoard[i + 1][j + 1].getTag().equals(currentPlayer) &&
                        fiveBoard[i + 2][j + 2].getTag().equals(currentPlayer) &&
                        fiveBoard[i + 3][j + 3].getTag().equals(currentPlayer)) {
                    return true;
                }
            }
        }

        // Check other diagonal (from top-right to bottom-left)
        for (int i = 0; i < 2; i++) {
            for (int j = 3; j < 5; j++) {
                if (fiveBoard[i][j].getTag().equals(currentPlayer) &&
                        fiveBoard[i + 1][j - 1].getTag().equals(currentPlayer) &&
                        fiveBoard[i + 2][j - 2].getTag().equals(currentPlayer) &&
                        fiveBoard[i + 3][j - 3].getTag().equals(currentPlayer)) {
                    return true;
                }
            }
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
            for (int j = 0; j < 3; j++) {
                if (fiveBoard[i][j].getTag().equals(currentPlayer) &&
                        fiveBoard[i][j + 1].getTag().equals(currentPlayer) &&
                        fiveBoard[i][j + 2].getTag().equals(currentPlayer)) {
                    return true;
                }
            }
        }

        // Check columns
        for (int i = 0; i < COL_OF_BUTTONS; i++) {
            for (int j = 0; j < 3; j++) {
                if (fiveBoard[j][i].getTag().equals(currentPlayer) &&
                        fiveBoard[j + 1][i].getTag().equals(currentPlayer) &&
                        fiveBoard[j + 2][i].getTag().equals(currentPlayer)) {
                    return true;
                }
            }
        }

        // Check main diagonal (from top-left to bottom-right)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (fiveBoard[i][j].getTag().equals(currentPlayer) &&
                        fiveBoard[i + 1][j + 1].getTag().equals(currentPlayer) &&
                        fiveBoard[i + 2][j + 2].getTag().equals(currentPlayer)) {
                    return true;
                }
            }
        }

        // Check other diagonal (from top-right to bottom-left)
        for (int i = 0; i < 3; i++) {
            for (int j = 2; j < 5; j++) {
                if (fiveBoard[i][j].getTag().equals(currentPlayer) &&
                        fiveBoard[i + 1][j - 1].getTag().equals(currentPlayer) &&
                        fiveBoard[i + 2][j - 2].getTag().equals(currentPlayer)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkDrawCondition() {
        // Check if all cells are filled
        for (int ir = 0; ir < ROW_OF_BUTTONS; ir++) {
            for (int ic = 0; ic < COL_OF_BUTTONS; ic++) {
                if (fiveBoard[ir][ic].getTag().equals("n")) {
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
                        FiveFragment newFiveFrag = new FiveFragment();
                        Bundle args = new Bundle();
                        args.putInt("WIN_CONDITION", winCondition);
                        args.putBoolean("gameAgainst", gameAgainst);
                        newFiveFrag.setArguments(args);
                        FragmentManager fragmentManager = getParentFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.FrameBoard, newFiveFrag);
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
        return rand.nextInt(5);
    }


    private void undoLastMove() {
        if (!moveStack.isEmpty()) {
            Move lastMove = moveStack.pop();
            int row = lastMove.row;
            int col = lastMove.col;
            fiveBoard[row][col].setImageBitmap(null);
            fiveBoard[row][col].setBackgroundColor(Color.WHITE);
            fiveBoard[row][col].setTag("n");
            fiveBoard[row][col].setEnabled(true);
        }
    }

    private void resetBoard() {
        for (int ir = 0; ir < ROW_OF_BUTTONS; ir++) {
            for (int ic = 0; ic < COL_OF_BUTTONS; ic++) {
                fiveBoard[ir][ic].setImageBitmap(null);
                fiveBoard[ir][ic].setBackgroundColor(Color.WHITE);
                fiveBoard[ir][ic].setTag("n");
                fiveBoard[ir][ic].setEnabled(true);
            }
        }
        moveStack.clear();
        turn = true;
    }


}
