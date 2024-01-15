package com.example.mad_assignment1_final;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameFragment3x3 extends Fragment implements View.OnClickListener {

    List<ImageView> tileList;
    boolean AI_mode = false;
    private boolean playerCanMove = true;

    boolean x_turn = true;
    int[][] winningCombinations = {{0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 4, 8},
            {2, 4, 6},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8}};
    int[] gridPositions = {0, 0, 0, 0, 0, 0, 0, 0, 0}; //0 represents an empty tile, 1 is an x, 2 is an o

    ImageView tile0, tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8;

    int numberOfMovesPlayed = 0;
    int gameEnded = 0;  // 0 for not ended, 1 for ended

    private int maxMovesX = 9;
    private int maxMovesO = 9;

    private int xPlayerMoves = 0; // Initialize move counts for X and O
    private int oPlayerMoves = 0;

    private Toast remainingMovesToast;
    GameResultData gameResultDataViewModel;

    private TextView player1Indicator;
    private TextView timerText;
    private TextView player2Indicator;
    private TextView turnIndicator;
    GameTimerShared gameTimerShared;

    private int lastMove = -1; // Variable to hold the last made move location
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Decide the layout based on orientation
        int layoutId = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                ? R.layout.game_fragment3x3_land
                : R.layout.game_fragment3x3;
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(layoutId, container, false);

        player1Indicator = rootView.findViewById(R.id.player1Name);
        player2Indicator = rootView.findViewById(R.id.player2Name);
        turnIndicator = rootView.findViewById(R.id.currentPlayerTurn);
        timerText = rootView.findViewById(R.id.timerText);

        gameTimerShared = new ViewModelProvider(getActivity()).get(GameTimerShared.class);

        gameResultDataViewModel = new ViewModelProvider(getActivity())
                .get(GameResultData.class);
        // Observe timer's text and update UI accordingly
        gameTimerShared.getTimeDisplay().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                timerText.setText(s);
            }
        });

        // Observe turn switch event
        gameTimerShared.getTurnSwitchedEvent().observe(getViewLifecycleOwner(), new Observer<Void>() {
            @Override
            public void onChanged(Void aVoid) {
                switchTurns();
            }
        });

        gameResultDataViewModel.gameEnded.observe(getViewLifecycleOwner(), gameEnded -> {
            if (gameEnded == 1) {
                loadGameEndedFragment();
            }
        });
        // Initialize settings button and set onClick listener
        Button btnSettings = rootView.findViewById(R.id.btnIn);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameTimerShared.pauseTimer();
                // Open the InGameMenuFragment when settings button is clicked
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                GameFragment3x3 gameFragment = (GameFragment3x3) getActivity().getSupportFragmentManager().findFragmentByTag("GAME_FRAGMENT_TAG");
                if (gameFragment != null) {
                    transaction.hide(gameFragment);
                }
                InGameMenuFragment inmenuFragment = new InGameMenuFragment();
                inmenuFragment.setFragmentToResumeTag("GAME_FRAGMENT_TAG_3x3");
                transaction.add(R.id.f_container, inmenuFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        Button undoButton = rootView.findViewById(R.id.undoButton);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                undoLastMove();
            }
        });

        Button resetButton = rootView.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameTimerShared.pauseTimer();
                gameTimerShared.resetTimer();
                startGame();
                xPlayerMoves=0;
                oPlayerMoves=0;
            }
        });


        tileList = new ArrayList<ImageView>();

        ImageView gridView = rootView.findViewById(R.id.gridView);
        tile0 = rootView.findViewById(R.id.tile0);
        tile1 = rootView.findViewById(R.id.tile1);
        tile2 = rootView.findViewById(R.id.tile2);
        tile3 = rootView.findViewById(R.id.tile3);
        tile4 = rootView.findViewById(R.id.tile4);
        tile5 = rootView.findViewById(R.id.tile5);
        tile6 = rootView.findViewById(R.id.tile6);
        tile7 = rootView.findViewById(R.id.tile7);
        tile8 = rootView.findViewById(R.id.tile8);

        Collections.addAll(tileList, tile0, tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8);

        if (gameResultDataViewModel.getGameEnded() == 0) {
            startGame();
        }

        return rootView;
    }


    @Override
    public void onClick(View view) {

        if (!playerCanMove) {

            return; // If it's not the player's turn, exit the function
        }

        int currentPlayer = x_turn ? 1 : 2;
        int maxMoves = x_turn ? maxMovesX : maxMovesO;


        for (ImageView t : tileList) {
            if (view.getId() == t.getId() && gridPositions[tileList.indexOf(t)] == 0) {
                gridPositions[tileList.indexOf(t)] = x_turn ? 1 : 2;
                t.setImageResource(x_turn ? R.drawable.background_x : R.drawable.background_o);
                t.setOnClickListener(null);

                lastMove = tileList.indexOf(t);

                numberOfMovesPlayed++;

                //showMoveCountToast(numberOfMovesPlayed, maxMoves);

                if (x_turn) {
                    maxMovesX--; // Decrement X's remaining moves
                    maxMovesO--; // Decrement O's remaining moves
                    xPlayerMoves++; // Increment X's move count


                } else {
                    maxMovesO--; // Decrement O's remaining moves
                    maxMovesX--; // Decrement X's remaining moves
                    oPlayerMoves++; // Increment O's move count

                }
                    showRemainingMovesToast(maxMoves,x_turn);
                    break;
            }
        }

        numberOfMovesPlayed++;

        gameEnded = checkWin();

        if (gameEnded == 1 || maxMoves ==0) {
            endTheGame();
        } else {
            switchTurns();
            updatePlayerIndicators();
           playerCanMove = true;
        }
    }

    private void undoLastMove() {
        if (lastMove != -1) {
            ImageView lastMoveTile = tileList.get(lastMove);

            // Clear the move on the grid
            gridPositions[lastMove] = 0;
            lastMoveTile.setImageResource(R.drawable.blankgrid);
            lastMoveTile.setOnClickListener(this);

            // Decrement move counts and update UI as needed
            if (x_turn) {
                maxMovesX++;
                xPlayerMoves--;
            } else {
                maxMovesO++;
                oPlayerMoves--;
            }

            numberOfMovesPlayed--;

            // Clear the last move
            lastMove = -1;

            // Update remaining moves UI
            showRemainingMovesToast(x_turn ? maxMovesX : maxMovesO, x_turn);
            playerCanMove = true;

        }
    }

    private void showMoveCountToast(int xMoves, int oMoves) {
        String message = "X's Moves: " + xMoves + "\nO's Moves: " + oMoves;
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }






    private void showRemainingMovesToast(int remainingMoves,boolean isXTurn) {
        if (remainingMovesToast != null) {
            remainingMovesToast.cancel(); // Cancel previous Toast if it's still showing
        }
        String player = isXTurn ? "X" : "O";
        String message = "Remaining moves: " + remainingMoves;
        remainingMovesToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        remainingMovesToast.show();
    }

    private void loadGameEndedFragment() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        GameEndScreen gameEndScreen = new GameEndScreen();

        // Use a Bundle to pass data
        Bundle args = new Bundle();
        args.putString("gameMode", "3x3");
        gameEndScreen.setArguments(args);

        transaction.replace(R.id.f_container, gameEndScreen);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private int checkWin() {
        int gameHasEnded = 0;

        for (int[] winningCombination : winningCombinations) {
            if (gridPositions[winningCombination[0]] == gridPositions[winningCombination[1]]
                    && gridPositions[winningCombination[1]] == gridPositions[winningCombination[2]]
                    && gridPositions[winningCombination[0]] != 0) {
                gameHasEnded = 1;
                break; // Exit the loop once a winning condition is found
            }
        }

        // If no winning condition is found and all moves are played, it's a draw
        if (gameHasEnded == 0 && numberOfMovesPlayed == 9) {
            gameHasEnded = 1;
        }

        gameTimerShared.pauseTimer();

        return gameHasEnded;
    }



    private void switchTurns()
    {
        x_turn = !x_turn;
        gameTimerShared.resetTimer();
        gameTimerShared.startTimer();
    }


    private void updatePlayerIndicators() {
        if (x_turn) {
            player1Indicator.setText("Your Turn"); // Player 1's turn
            player2Indicator.setText("Opponent's Turn"); // Player 2's turn
        } else {
            player1Indicator.setText("Opponent's Turn"); // Player 1's turn
            player2Indicator.setText("Your Turn"); // Player 2's turn
        }
    }
    private void endTheGame()
    {
        gameTimerShared.pauseTimer();
        gameTimerShared.resetTimer();
        for(ImageView tile : tileList) {
            tile.setOnClickListener(null);
        }

        if(x_turn && numberOfMovesPlayed != 9) {
            gameResultDataViewModel.setGameEndText("X's win!");
            gameResultDataViewModel.incrementPlayerOneWins();
        }
        else if(!x_turn && numberOfMovesPlayed != 9) {
            gameResultDataViewModel.setGameEndText("O's win!");
            gameResultDataViewModel.incrementPlayerTwoWins();
        }
        else {
            gameResultDataViewModel.setIsDraw(true);
            gameResultDataViewModel.setGameEndText("It's a draw!");
            gameResultDataViewModel.incrementDraws(); // Update the draw counters for both players
        }

        gameResultDataViewModel.setGameEnded(1);
    }

    private void startGame()
    {
        playerCanMove = true; // Ensure player can move at the start
        for(ImageView tile : tileList)
        {
            tile.setImageResource(R.drawable.blankgrid);
            tile.setOnClickListener(this);
        }

        for(int i = 0; i < gridPositions.length; i++)
        {
            gridPositions[i] = 0;
        }

        x_turn = true;

        gameEnded = 0;

        numberOfMovesPlayed = 0;

        gameResultDataViewModel.setIsDraw(false);

        gameResultDataViewModel.setGameEndText("");

        gameResultDataViewModel.setGameEnded(0);
        gameTimerShared.startTimer();
    }
    private void makeAIMove() {
        playerCanMove = false; // Disable player moves

        if (AI_mode) {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<Integer> emptyTiles = new ArrayList<>();

                    for (int i = 0; i < gridPositions.length; i++) {
                        if (gridPositions[i] == 0) {
                            emptyTiles.add(i);
                        }
                    }

                    if (!emptyTiles.isEmpty()) {
                        int randomIndex = (int) (Math.random() * emptyTiles.size());
                        int selectedTile = emptyTiles.get(randomIndex);
                        gridPositions[selectedTile] = 2; // O's turn
                        tileList.get(selectedTile).setImageResource(R.drawable.background_o);
                        tileList.get(selectedTile).setOnClickListener(null);

                        numberOfMovesPlayed++;

                        gameEnded = checkWin();

                        if (gameEnded==1) {
                            endTheGame();
                        } else {
                            switchTurns();
                            playerCanMove = true; // Re-enable player moves
                        }
                    }
                }
            }, 2000); // Delay of 1 second (1000 milliseconds)
        }
        playerCanMove=true;
    }



}

