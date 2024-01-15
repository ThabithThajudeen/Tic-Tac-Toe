package com.example.mad_assignment1_final;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameFragment5x5 extends Fragment implements  View.OnClickListener {

    List<ImageView> tileList;
    boolean playerCanMove = true;

    boolean x_turn = true;
    boolean AI_mode = false; // Add this line to denote if AI mode is on
    Random random = new Random();
    int xPlayerMoves =0;
    int oPlayerMoves=0;

    private int lastMove = -1; // Variable to hold the last made move location
    int[][] winningCombinations3x3 = {{0, 1, 2}, {1, 2, 3}, {2, 3, 4}, {5, 6, 7},
            {6, 7, 8}, {7, 8, 9}, {10, 11, 12}, {11, 12, 13},
            {12, 13, 14}, {15, 16, 17}, {16, 17, 18}, {17, 18, 19},
            {20, 21, 22}, {21, 22, 23}, {22, 23, 24}, {0, 5, 10},
            {5, 10, 15}, {10, 15, 20}, {1, 6, 11}, {6, 11, 16},
            {11, 16, 21}, {2, 7, 12}, {7, 12, 17}, {12, 17, 22},
            {3, 8, 13}, {8, 13, 18}, {13, 18, 23}, {4, 9, 14},
            {9, 14, 19}, {14, 19, 24}, {0, 6, 12}, {6, 12, 18},
            {12, 18, 24}, {4, 8, 12}, {8, 12, 16}, {12, 16, 20}};
    int[][] winningCombinations4x4 = {{0, 1, 2, 3}, {1, 2, 3, 4},
            {5, 6, 7, 8}, {6, 7, 8, 9},
            {10, 11, 12, 13}, {11, 12, 13, 14},
            {15, 16, 17, 18}, {16, 17, 18, 19},
            {20, 21, 22, 23}, {21, 22, 23, 24},
            {0, 5, 10, 15}, {5, 10, 15, 20},
            {1, 6, 11, 16}, {6, 11, 16, 21},
            {2, 7, 12, 17}, {7, 12, 17, 22},
            {3, 8, 13, 18}, {8, 13, 18, 23},
            {4, 9, 14, 19}, {9, 14, 19, 24},
            {0, 6, 12, 18}, {6, 12, 18, 24},
            {4, 8, 12, 16}, {8, 12, 16, 20}};

    int[][] winningCombinations5x5 = {{0, 1, 2, 3, 4}, {5, 6, 7, 8, 9},
            {10, 11, 12, 13, 14}, {15, 16, 17, 18, 19},
            {20, 21, 22, 23, 24}, {0, 5, 10, 15, 20},
            {1, 6, 11, 16, 21}, {2, 7, 12, 17, 22},
            {3, 8, 13, 18, 23}, {4, 9, 14, 19, 24},
            {0, 6, 12, 18, 24}, {4, 8, 12, 16, 20}};
    int[] gridPositions = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //0 represents an empty tile, 1 is an x, 2 is an o

    ImageView tile0, tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10, tile11, tile12, tile13, tile14, tile15, tile16, tile17, tile18, tile19, tile20, tile21, tile22, tile23, tile24;

    int numberOfMovesPlayed = 0;
    boolean gameEnded = false;

    int requiredMoves = 3;

    GameResultData gameResultDataViewModel;
    private int remainingMovesX = 12; // Remaining moves for X
    private int remainingMovesO = 12; // Remaining moves for O

    private int maxMovesX = 12;
    private int maxMovesO = 12;

    private Toast remainingMovesToast;

    private TextView timerText;
    GameTimerShared gameTimerShared;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Decide the layout based on orientation
        int layoutId = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                ? R.layout.game_fragment5x5_land
                : R.layout.game_fragment5x5;
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(layoutId, container, false);

        gameResultDataViewModel = new ViewModelProvider(getActivity())
                .get(GameResultData.class);

        // Observe gameEnded LiveData
        gameResultDataViewModel.gameEnded.observe(getViewLifecycleOwner(), gameEnded -> {
            if (gameEnded == 1) {
                loadGameEndedFragment();
            }
        });

        timerText = rootView.findViewById(R.id.timerText);

        gameTimerShared = new ViewModelProvider(getActivity()).get(GameTimerShared.class);
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

        // Initialize settings button and set onClick listener
        Button btnSettings = rootView.findViewById(R.id.btnIn);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameTimerShared.pauseTimer();
                // Open the InGameMenuFragment when settings button is clicked
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                GameFragment5x5 gameFragment = (GameFragment5x5) getActivity().getSupportFragmentManager().findFragmentByTag("GAME_FRAGMENT_TAG");
                if (gameFragment != null) {
                    transaction.hide(gameFragment);
                }
                InGameMenuFragment inmenuFragment = new InGameMenuFragment();
                inmenuFragment.setFragmentToResumeTag("GAME_FRAGMENT_TAG_5x5");
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

        tile0 = rootView.findViewById(R.id.tile0);
        tile1 = rootView.findViewById(R.id.tile1);
        tile2 = rootView.findViewById(R.id.tile2);
        tile3 = rootView.findViewById(R.id.tile3);
        tile4 = rootView.findViewById(R.id.tile4);
        tile5 = rootView.findViewById(R.id.tile5);
        tile6 = rootView.findViewById(R.id.tile6);
        tile7 = rootView.findViewById(R.id.tile7);
        tile8 = rootView.findViewById(R.id.tile8);
        tile9 = rootView.findViewById(R.id.tile9);
        tile10 = rootView.findViewById(R.id.tile10);
        tile11 = rootView.findViewById(R.id.tile11);
        tile12 = rootView.findViewById(R.id.tile12);
        tile13 = rootView.findViewById(R.id.tile13);
        tile14 = rootView.findViewById(R.id.tile14);
        tile15 = rootView.findViewById(R.id.tile15);
        tile16 = rootView.findViewById(R.id.tile16);
        tile17 = rootView.findViewById(R.id.tile17);
        tile18 = rootView.findViewById(R.id.tile18);
        tile19 = rootView.findViewById(R.id.tile19);
        tile20 = rootView.findViewById(R.id.tile20);
        tile21 = rootView.findViewById(R.id.tile21);
        tile22 = rootView.findViewById(R.id.tile22);
        tile23 = rootView.findViewById(R.id.tile23);
        tile24 = rootView.findViewById(R.id.tile24);


        Collections.addAll(tileList, tile0, tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10,
                tile11, tile12, tile13, tile14, tile15, tile16, tile17, tile18, tile19, tile20, tile21, tile22,
                tile23, tile24);


        if (gameResultDataViewModel.getGameEnded() == 0) {
            startGame();
        }

        return rootView;
    }

    private void loadGameEndedFragment() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        GameEndScreen gameEndScreen = new GameEndScreen();

        // Use a Bundle to pass data
        Bundle args = new Bundle();
        args.putString("gameMode", "5x5");
        gameEndScreen.setArguments(args);

        transaction.replace(R.id.f_container, gameEndScreen);
        transaction.addToBackStack(null);
        transaction.commit();
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

    @Override
    public void onClick(View view) {
        requiredMoves = gameResultDataViewModel.getRequiredMoves();
        if (!playerCanMove) {
            return;
        }

        for (ImageView t : tileList) {
            if (view.getId() == t.getId() && gridPositions[tileList.indexOf(t)] == 0) {
                gridPositions[tileList.indexOf(t)] = x_turn ? 1 : 2;
                t.setImageResource(x_turn ? R.drawable.background_x_resized : R.drawable.background_o_resized);
                t.setOnClickListener(null);
                lastMove = tileList.indexOf(t);

                decrementRemainingMoves(); // Decrement the remaining moves for the current player

                displayRemainingMoves();
                numberOfMovesPlayed++;

                gameEnded = checkWin();


                if (gameEnded) {
                    endTheGame();
                } else {
                    switchTurns();
                }
                // after user plays their move, if AI mode is active, AI plays its move
                if (AI_mode && !gameEnded) {
                    makeAIMove();
                }

                displayGameProgress(numberOfMovesPlayed, remainingMovesX, remainingMovesO);

            }
        }
    }

    private void displayGameProgress(int movesPlayed, int xMoves, int oMoves) {
        String message = "Moves Played: " + movesPlayed + "\nX's Moves: " + xMoves + "\nO's Moves: " + oMoves;
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void decrementRemainingMoves() {
        if (x_turn) {
            remainingMovesX--;
        } else {
            remainingMovesO--;
        }
    }

    private boolean checkWin() {
        boolean gameHasEnded = false;
        if (numberOfMovesPlayed == 25) {
            gameHasEnded = true;
        }
        if (requiredMoves == 3) {
            for (int[] winningCombination : winningCombinations3x3) {
                if (gridPositions[winningCombination[0]] == gridPositions[winningCombination[1]]
                        && gridPositions[winningCombination[1]] == gridPositions[winningCombination[2]]
                        && gridPositions[winningCombination[0]] != 0) {
                    gameHasEnded = true;
                }
            }
        } else if (requiredMoves == 4) {
            for (int[] winningCombination : winningCombinations4x4) {
                if (gridPositions[winningCombination[0]] == gridPositions[winningCombination[1]]
                        && gridPositions[winningCombination[1]] == gridPositions[winningCombination[2]]
                        && gridPositions[winningCombination[2]] == gridPositions[winningCombination[3]]
                        && gridPositions[winningCombination[0]] != 0) {
                    gameHasEnded = true;
                }
            }
        } else if (requiredMoves == 5) {
            for (int[] winningCombination : winningCombinations5x5) {
                if (gridPositions[winningCombination[0]] == gridPositions[winningCombination[1]]
                        && gridPositions[winningCombination[1]] == gridPositions[winningCombination[2]]
                        && gridPositions[winningCombination[2]] == gridPositions[winningCombination[3]]
                        && gridPositions[winningCombination[3]] == gridPositions[winningCombination[4]]
                        && gridPositions[winningCombination[0]] != 0) {
                    gameHasEnded = true;
                }
            }
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


    private void endTheGame() {

        gameTimerShared.pauseTimer();
        gameTimerShared.resetTimer();
        playerCanMove = false;
        for (ImageView tile : tileList) {
            tile.setOnClickListener(null);
        }

        if (x_turn && numberOfMovesPlayed != 25) {
            gameResultDataViewModel.setGameEndText("X's win!");
        } else if (!x_turn && numberOfMovesPlayed != 25) {
            gameResultDataViewModel.setGameEndText("O's win!");
        } else {
            gameResultDataViewModel.setIsDraw(true);
            gameResultDataViewModel.setGameEndText("It's a draw!");
        }

        gameResultDataViewModel.setGameEnded(1);
    }

    private void startGame() {
        playerCanMove = true;
        for (ImageView tile : tileList) {
            tile.setImageResource(R.drawable.blankgrid);
            tile.setOnClickListener(this);
        }

        for (int i = 0; i < gridPositions.length; i++) {
            gridPositions[i] = 0;
        }

        x_turn = true;

        gameEnded = false;

        numberOfMovesPlayed = 0;

        gameResultDataViewModel.setIsDraw(false);

        gameResultDataViewModel.setGameEndText("");

        gameResultDataViewModel.setGameEnded(0);
        displayRemainingMoves();
        gameTimerShared.startTimer();

    }

    private void makeAIMove() {

        playerCanMove = false;

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<Integer> emptyTiles = new ArrayList<>();
                for (int i = 0; i < gridPositions.length; i++) {
                    if (gridPositions[i] == 0) {
                        emptyTiles.add(i);
                    }
                }

                if (emptyTiles.size() > 0) {
                    int randomTileIndex = emptyTiles.get(random.nextInt(emptyTiles.size()));
                    ImageView randomTile = tileList.get(randomTileIndex);

                    gridPositions[randomTileIndex] = x_turn ? 1 : 2;
                    randomTile.setImageResource(x_turn ? R.drawable.background_x_resized : R.drawable.background_o_resized);
                    randomTile.setOnClickListener(null);

                    numberOfMovesPlayed++;

                    gameEnded = checkWin();
                    if (gameEnded) {
                        endTheGame();
                    } else {
                        switchTurns();
                    }
                    playerCanMove = true;
                }
            }
        }, 1000); // Delay of 1000 milliseconds (1 second)
    }


    private void displayRemainingMoves() {
        int remainingMoves = x_turn ? remainingMovesX : remainingMovesO;
        String message = "Remaining Moves: " + remainingMoves;
        if (remainingMovesToast != null) {
            remainingMovesToast.cancel();
        }
        remainingMovesToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        remainingMovesToast.show();
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

}