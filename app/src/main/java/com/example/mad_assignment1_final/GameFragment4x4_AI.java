package com.example.mad_assignment1_final;

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

public class GameFragment4x4_AI extends Fragment implements View.OnClickListener{

    List<ImageView> tileList;

    boolean x_turn = true;
    boolean AI_mode = true;
    private boolean playerCanMove = true;
    int[][] winningCombinations3x3 = {{0, 1, 2}, {1, 2, 3}, {4, 5, 6}, {5, 6, 7},
            {8, 9, 10}, {9, 10, 11}, {12, 13, 14}, {13, 14, 15},
            {0, 4, 8}, {4, 8, 12}, {1, 5, 9}, {5, 9, 13},
            {2, 6, 10}, {6, 10, 14}, {3, 7, 11}, {7, 11, 15},
            {0, 5, 10}, {1, 6, 11}, {2, 6, 9}, {3, 10, 13},
            {5, 10, 15}, {4, 9, 14}, {6, 9, 12}, {7, 10, 13}};
    int[][] winningCombinations4x4 = {{0, 1, 2, 3},
            {4, 5, 6, 7},
            {8, 9, 10, 11},
            {12, 13, 14, 15},
            {0, 5, 10, 15},
            {3, 6, 9, 12},
            {0, 4, 8, 12},
            {1, 5, 9, 13},
            {2, 6, 10, 14},
            {3, 7, 11, 15}};
    int[] gridPositions = {0, 0, 0, 0, 0 ,0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; //0 represents an empty tile, 1 is an x, 2 is an o

    ImageView tile0, tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10, tile11, tile12
            , tile13, tile14, tile15;

    int numberOfMovesPlayed = 0;
    int xPlayerMoves =0;
    int oPlayerMoves=0;
    boolean gameEnded = false;

    int requiredMoves = 4;

    GameResultData gameResultDataViewModel;
    private TextView timerText;
    private int remainingMovesX = 9;
    private int remainingMovesO = 9;

    GameTimerShared gameTimerShared;

    private Toast remainingMovesToast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.game_fragment4x4, container, false);

        gameResultDataViewModel = new ViewModelProvider(getActivity())
                .get(GameResultData.class);

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

        // Observe gameEnded LiveData
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
                GameFragment4x4_AI gameFragment = (GameFragment4x4_AI) getActivity().getSupportFragmentManager().findFragmentByTag("GAME_FRAGMENT_TAG");
                if (gameFragment != null) {
                    transaction.hide(gameFragment);
                }
                InGameMenuFragment inmenuFragment = new InGameMenuFragment();
                inmenuFragment.setFragmentToResumeTag("GAME_FRAGMENT_TAG_4x4_AI");
                transaction.add(R.id.f_container, inmenuFragment);
                transaction.addToBackStack(null);
                transaction.commit();

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

        Collections.addAll(tileList, tile0, tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8, tile9, tile10,
                tile11, tile12, tile13, tile14, tile15);

        if(gameResultDataViewModel.getGameEnded() == 0)
        {
            startGame();
        }

        return rootView;
    }
    private void loadGameEndedFragment() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        GameEndScreen gameEndScreen = new GameEndScreen();

        // Use a Bundle to pass data
        Bundle args = new Bundle();
        args.putString("gameMode", "4x4");
        gameEndScreen.setArguments(args);

        transaction.replace(R.id.f_container, gameEndScreen);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void displayGameProgress() {
        int xMoves = xPlayerMoves;
        int oMoves = oPlayerMoves;
        int totalMoves = 16; // Assuming a 4x4 grid

        if (requiredMoves == 3) {
            totalMoves = 9; // Total moves for a 3x3 game
        }

        String message;
        if (x_turn) {
            message = "X's turn. Moves Made: " + xMoves + " / Total Moves: " + totalMoves;
        } else {
            message = "O's turn. Moves Made: " + oMoves + " / Total Moves: " + totalMoves;
        }

        if (remainingMovesToast != null) {
            remainingMovesToast.cancel();
        }

        remainingMovesToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        remainingMovesToast.show();
    }
    @Override
    public void onClick(View view) {
        requiredMoves= gameResultDataViewModel.getRequiredMoves();
        if(requiredMoves >4)
        {requiredMoves=3;}
        if (!playerCanMove) {
            return;
        }
        for (ImageView t : tileList) {
            if (view.getId() == t.getId() && gridPositions[tileList.indexOf(t)] == 0) {
                gridPositions[tileList.indexOf(t)] = x_turn ? 1 : 2;
                t.setImageResource(x_turn ? R.drawable.background_x_resized : R.drawable.background_o_resized);
                t.setOnClickListener(null);
                decrementRemainingMoves(); // Decrement the remaining moves for the current player

            }
        }

        numberOfMovesPlayed++;
        postMove(); // Check the game's status after each move
    }

    private boolean checkWin()
    {
        if (gameEnded) return true;

        boolean gameHasEnded = false;

        if(numberOfMovesPlayed == 16)
        {
            gameHasEnded = true;
        }

        if(requiredMoves == 3)
        {
            for (int[] winningCombination : winningCombinations3x3)
            {
                if (gridPositions[winningCombination[0]] == gridPositions[winningCombination[1]]
                        && gridPositions[winningCombination[1]] == gridPositions[winningCombination[2]]
                        && gridPositions[winningCombination[0]] != 0) {
                    gameHasEnded = true;
                }
            }
        }
        else if(requiredMoves == 4)
        {
            for(int[] winningCombination : winningCombinations4x4)
            {
                if (gridPositions[winningCombination[0]] == gridPositions[winningCombination[1]]
                        && gridPositions[winningCombination[1]] == gridPositions[winningCombination[2]]
                        && gridPositions[winningCombination[2]] == gridPositions[winningCombination[3]]
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

    private void endTheGame()
    {
        gameTimerShared.pauseTimer();
        gameTimerShared.resetTimer();
        for(ImageView tile : tileList)
        {
            tile.setOnClickListener(null);
        }

        if(x_turn && numberOfMovesPlayed != 16)
        {
            gameResultDataViewModel.setGameEndText("X's win!");
        }
        else if(!x_turn && numberOfMovesPlayed != 16)
        {
            gameResultDataViewModel.setGameEndText("O's win!");
        }
        else
        {
            gameResultDataViewModel.setIsDraw(true);
            gameResultDataViewModel.setGameEndText("It's a draw!");
        }

        gameResultDataViewModel.setGameEnded(1);
    }

    private void startGame()
    {
        playerCanMove = true; // player moves
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

        gameEnded = false;

        numberOfMovesPlayed = 0;

        gameResultDataViewModel.setIsDraw(false);

        gameResultDataViewModel.setGameEndText("");

        gameResultDataViewModel.setGameEnded(0);
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

                if (!emptyTiles.isEmpty()) {
                    int randomIndex = (int) (Math.random() * emptyTiles.size());
                    int selectedTile = emptyTiles.get(randomIndex);
                    gridPositions[selectedTile] = 2;
                    tileList.get(selectedTile).setImageResource(R.drawable.background_o);
                    tileList.get(selectedTile).setOnClickListener(null);

                    numberOfMovesPlayed++;
                    postMove(); // Check the game's status after the AI's move
                } else {
                    playerCanMove = true;
                }
            }
        }, 2000);
    }


    private void postMove() {
        gameEnded = checkWin();

        if (gameEnded) {
            endTheGame();
        } else {
            switchTurns();

            if (!x_turn && AI_mode) {
                makeAIMove();
            } else {
                playerCanMove = true;
                displayRemainingMoves();

            }
        }
    }

    private void displayRemainingMoves() {
        String message;
        if (x_turn) {
            message = "X's turn. Moves remaining: " + remainingMovesX;
        } else {
            message = "O's turn. Moves remaining: " + remainingMovesO;
        }

        if (remainingMovesToast != null) {
            remainingMovesToast.cancel();
        }

        remainingMovesToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        remainingMovesToast.show();
    }

    private void decrementRemainingMoves() {
        if (x_turn) {
            remainingMovesX--;
        } else {
            remainingMovesO--;
        }
    }

}