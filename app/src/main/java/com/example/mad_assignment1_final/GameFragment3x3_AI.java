package com.example.mad_assignment1_final;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameFragment3x3_AI extends Fragment implements View.OnClickListener {

    List<ImageView> tileList;
    boolean AI_mode = true;
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
    int[] gridPositions = {0, 0, 0, 0, 0 ,0, 0, 0, 0}; //0 represents an empty tile, 1 is an x, 2 is an o

    ImageView tile0, tile1, tile2, tile3, tile4, tile5, tile6, tile7, tile8;

    int numberOfMovesPlayed = 0;
    int gameEnded = 0;  // 0 for not ended, 1 for ended


    GameResultData gameResultDataViewModel;

    private int remainingMovesX = 9; // Remaining moves for X
    private int remainingMovesO = 9; // Remaining moves for O
    private Toast remainingMovesToast;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.game_fragment3x3, container, false);

        gameResultDataViewModel = new ViewModelProvider(getActivity())
                .get(GameResultData.class);

        // Check if we are recreating from saved instance state
     //   if (savedInstanceState != null) {
      //      gridPositions = gameResultDataViewModel.getGridPositionsData();
      //      x_turn = gameResultDataViewModel.getXTurnData();
      //      numberOfMovesPlayed = gameResultDataViewModel.getNumberOfMovesPlayedData();
      //      gameEnded= gameResultDataViewModel.getGameEnded();
      //  }
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
                // Open the InGameMenuFragment when settings button is clicked
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                GameFragment3x3_AI gameFragment = (GameFragment3x3_AI) getActivity().getSupportFragmentManager().findFragmentByTag("GAME_FRAGMENT_TAG");
                if (gameFragment != null) {
                    transaction.hide(gameFragment);
                }
                InGameMenuFragment inmenuFragment = new InGameMenuFragment();
                inmenuFragment.setFragmentToResumeTag("GAME_FRAGMENT_TAG_3x3_AI");
                transaction.add(R.id.f_container, inmenuFragment);
                transaction.addToBackStack(null);
                transaction.commit();

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

        if(gameResultDataViewModel.getGameEnded() == 0)
        {
            startGame();
        }

        return rootView;
    }

    @Override
    public void onClick(View view)
    {

        if (!playerCanMove) {
            return; // If it's not the player's turn, exit the function
        }
        for(ImageView t : tileList)
        {
            if(view.getId() == t.getId() && gridPositions[tileList.indexOf(t)] == 0)
            {
                gridPositions[tileList.indexOf(t)] = x_turn ? 1 : 2;
                t.setImageResource(x_turn ? R.drawable.background_x : R.drawable.background_o);
                t.setOnClickListener(null);
                decrementRemainingMoves();
                displayRemainingMoves();

            }
        }

        numberOfMovesPlayed++;

        gameEnded = checkWin();

        if(gameEnded==1)
        {
            endTheGame();
        }
        else
        {
            switchTurns();
            makeAIMove();
        }

        //gameResultDataViewModel.setGridPositionsData(gridPositions);
       // gameResultDataViewModel.setXTurnData(x_turn);
       // gameResultDataViewModel.setNumberOfMovesPlayedData(numberOfMovesPlayed);

    }

    private void decrementRemainingMoves() {
        if (x_turn) {
            remainingMovesX--;
        } else {
            remainingMovesO--;
        }
    }

    private void displayRemainingMoves() {
        int remainingMoves = x_turn ? remainingMovesX : remainingMovesO;
        String message = (x_turn ? "X" : "O") + "'s Remaining Moves: " + remainingMoves;
        if (remainingMovesToast != null) {
            remainingMovesToast.cancel();
        }
        remainingMovesToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        remainingMovesToast.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putIntArray("gridPositions", gridPositions);
        outState.putBoolean("x_turn", x_turn);
        outState.putInt("numberOfMovesPlayed", numberOfMovesPlayed);
        outState.putInt("gameEnded", gameEnded);  // Saving gameEnded as an int
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            gridPositions = savedInstanceState.getIntArray("gridPositions");
            x_turn = savedInstanceState.getBoolean("x_turn");
            numberOfMovesPlayed = savedInstanceState.getInt("numberOfMovesPlayed");
            gameEnded = savedInstanceState.getInt("gameEnded");  // Retrieving gameEnded as an int
        }
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
            } else if(numberOfMovesPlayed == 9) {
                gameHasEnded = 1;
            }
        }
        return gameHasEnded;
    }


    private void switchTurns()
    {
        x_turn = !x_turn;
       // gameResultDataViewModel.setXTurnData(x_turn);
    }

    private void endTheGame()
    {
        for(ImageView tile : tileList)
        {
            tile.setOnClickListener(null);
        }

        if(x_turn && numberOfMovesPlayed != 9)
        {
            gameResultDataViewModel.setGameEndText("X's win!");
        }
        else if(!x_turn && numberOfMovesPlayed != 9)
        {
            gameResultDataViewModel.setGameEndText("O's win!");
        }
        else
        {
            gameResultDataViewModel.setIsDraw(true);
            gameResultDataViewModel.setGameEndText("It's a draw!");
        }

        gameResultDataViewModel.setGameEnded(1);
       // gameResultDataViewModel.setNumberOfMovesPlayedData(numberOfMovesPlayed);
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
                        displayGameProgress(numberOfMovesPlayed, remainingMovesX, remainingMovesO);

                    }
                    else {
                        playerCanMove = true; // Re-enable player moves even if no moves are possible
                    }
                }
            }, 2000); // Delay of 2 seconds (2000 milliseconds)
        } else {
            playerCanMove = true; // If not in AI mode, re-enable immediately
        }
    }


    private void displayGameProgress(int movesPlayed, int remainingMovesX, int remainingMovesO) {
        String message = "Moves Played: " + movesPlayed + "\n";
        message += "X's Remaining Moves: " + remainingMovesX + "\n";
        message += "O's Remaining Moves: " + remainingMovesO;

        if (remainingMovesToast != null) {
            remainingMovesToast.cancel();
        }

        remainingMovesToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        remainingMovesToast.show();
    }


}

