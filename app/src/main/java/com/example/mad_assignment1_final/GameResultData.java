package com.example.mad_assignment1_final;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class GameResultData extends ViewModel {

    private MutableLiveData<Boolean> isDraw;
    private MutableLiveData<Boolean> isAI;
    private MutableLiveData<String> gameEndText;
    public MutableLiveData<Integer> gameEnded;

    public MutableLiveData<Integer> requiredMoves;
    public MutableLiveData<Integer> clickedValue;
    private MutableLiveData<Integer> gameMarker = new MutableLiveData<>(-1);


    private final MutableLiveData<Integer> playerOneWins = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> playerOneLosses = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> playerOneDraws = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> playerTwoWins = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> playerTwoLosses = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> playerTwoDraws = new MutableLiveData<>(0);
    public MutableLiveData<Profile> activePlayer1;
    public MutableLiveData<Profile> activePlayer2;
    public MutableLiveData<Profile> editPlayer;
    public MutableLiveData<Integer> currentFragment;
    public MutableLiveData<ArrayList<Drawable>> avatarImages;
    public MutableLiveData<ArrayList<Profile>> listOfProfiles;
    private List<PlayerData> playerStatistics = new ArrayList<>();

    private final MutableLiveData<Integer> playerOneTotalGames = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> playerTwoTotalGames = new MutableLiveData<>(0);

    public GameResultData() {

        //Harry added Constructor
        currentFragment = new MediatorLiveData<Integer>();
        currentFragment.setValue(1);
        listOfProfiles = new MediatorLiveData<ArrayList<Profile>>();
        listOfProfiles.setValue(null);
        activePlayer1 = new MediatorLiveData<Profile>();
        activePlayer1.setValue(null);
        activePlayer2 = new MediatorLiveData<Profile>();
        activePlayer2.setValue(null);
        editPlayer = new MediatorLiveData<Profile>();
        editPlayer.setValue(null);
        avatarImages = new MediatorLiveData<ArrayList<Drawable>>();
        avatarImages.setValue(null);


        isDraw = new MediatorLiveData<>();
        gameEndText = new MediatorLiveData<>();
        gameEnded = new MediatorLiveData<>();
        requiredMoves = new MediatorLiveData<>();
        isAI = new MediatorLiveData<>();

        clickedValue = new MediatorLiveData<Integer>();


        isAI.setValue(false);
        gameEnded.setValue(0);  // Assuming 0 as the default state, indicating the game is not ended
        requiredMoves.setValue(3);  // Assuming 3 as the default state, indicating the game is not ended
        gameMarker.setValue(-1);



    }

    public int getGameMarker() {
        return gameMarker.getValue();
    }

    public void setGameMarker(int marker) {
        gameMarker.setValue(marker);
    }
    public void incrementPlayerOneWins() {
        playerOneWins.setValue(playerOneWins.getValue() + 1);
        incrementPlayerOneTotalGames();

    }

    public void incrementPlayerTwoWins() {
        playerTwoWins.setValue(playerTwoWins.getValue() + 1);
        incrementPlayerTwoTotalGames();


    }

    public void incrementDraws() {
        playerOneDraws.setValue(playerOneDraws.getValue() + 1);
        playerTwoDraws.setValue(playerTwoDraws.getValue() + 1);
        incrementPlayerOneTotalGames();
        incrementPlayerTwoTotalGames();



    }
    public void incrementPlayerOneTotalGames() {
        playerOneTotalGames.setValue(playerOneWins.getValue() + playerOneLosses.getValue() + playerOneDraws.getValue() + playerTwoWins.getValue()+playerTwoLosses.getValue()+playerTwoDraws.getValue());
    }

    public void incrementPlayerTwoTotalGames() {
        playerTwoTotalGames.setValue( playerOneWins.getValue()+playerOneLosses.getValue()+playerOneDraws.getValue() + playerTwoWins.getValue() + playerTwoLosses.getValue() + playerTwoDraws.getValue());


    }

    public double getPlayerOneWinPercentage() {
        int totalGames = getPlayerOneTotalGames();
        int wins = getPlayerOneWins();
        if (totalGames == 0) {
            return 0.0; // Avoid division by zero
        }
        return ((double) wins / totalGames) * 100.0;
    }

    public double getPlayerTwoWinPercentage() {
        int totalGames = getPlayerTwoTotalGames();
        int wins = getPlayerTwoWins();
        if (totalGames == 0) {
            return 0.0; // Avoid division by zero
        }
        return ((double) wins / totalGames) * 100.0;
    }

    public int getPlayerOneWins() {
        return playerOneWins.getValue();
    }

    public int getPlayerTwoWins() {
        return playerTwoWins.getValue();
    }

    public int getPlayerOneLosses() {
        return playerOneLosses.getValue();
    }

    public int getPlayerTwoLosses() {
        return playerTwoLosses.getValue();
    }

    public int getPlayerOneDraws() {
        return playerOneDraws.getValue();
    }

    public int getPlayerTwoDraws() {
        return playerTwoDraws.getValue();
    }


    public int getPlayerOneTotalGames() {
        return playerOneTotalGames.getValue();
    }

    public int getPlayerTwoTotalGames() {
        return playerTwoTotalGames.getValue();
    }

    public void resetAllStats() {
        playerOneWins.setValue(0);
        playerOneLosses.setValue(0);
        playerOneDraws.setValue(0);
        playerTwoWins.setValue(0);
        playerTwoLosses.setValue(0);
        playerTwoDraws.setValue(0);
    }

    public boolean getIsDraw() {
        return isDraw.getValue();
    }

    public void setIsDraw(boolean value) {
        isDraw.setValue(value);
    }

    // Getter and Setter for isAI
    public boolean getIsAI() {
        return isAI.getValue();
    }

    public void setIsAI(boolean value) {
        isAI.setValue(value);
    }

    // Getter and Setter for gameEndText
    public String getGameEndText() {
        return gameEndText.getValue();
    }

    public void setGameEndText(String value) {
        gameEndText.setValue(value);
    }

    // Getter and Setter for gameEnded
    public int getGameEnded() {
        return gameEnded.getValue();
    }

    public void setGameEnded(int value) {
        gameEnded.setValue(value);
    }

    // Getter and Setter for requiredMoves
    public int getRequiredMoves() {
        return requiredMoves.getValue();
    }

    public void setRequiredMoves(int value) {
        requiredMoves.setValue(value);
    }


    public int getClickedValue(){
        return clickedValue.getValue();
    }
    public void setClickedValue(int value){
        clickedValue.setValue(value);
    }


    //getters
    public Profile getActivePlayer1() {
        return activePlayer1.getValue();
    }
    public Profile getActivePlayer2() { return activePlayer2.getValue(); }
    public Profile getEditPlayer() { return editPlayer.getValue(); }
    public ArrayList<Profile> getListOfProfiles() { return listOfProfiles.getValue(); }
    public int getCurrentFragment() { return currentFragment.getValue(); }
    public ArrayList<Drawable> getAvatarImages() { return avatarImages.getValue(); }



    //setters
    public void setActivePlayer1(Profile pProfile) {
        activePlayer1.setValue(pProfile);
    }

    public void setActivePlayer2(Profile pProfile) {
        activePlayer2.setValue(pProfile);
    }
    public void setEditPlayer(Profile pProfile) {
        editPlayer.setValue(pProfile);
    }
    public void setListOfProfiles(ArrayList<Profile> value) {
        listOfProfiles.setValue(value);
    }
    public void setCurrentFragment(int value) {
        currentFragment.setValue(value);
    }
    public void setAvatarImages(ArrayList<Drawable> value) { avatarImages.setValue(value); }

    public List<PlayerData> getPlayerStatistics() {
        List<PlayerData> playerStatistics = new ArrayList<>();

        // Add player data to the list
        playerStatistics.add(new PlayerData("Player 1", getPlayerOneWins(), getPlayerOneLosses(),getPlayerOneDraws(),getPlayerOneTotalGames(), (int) getPlayerOneWinPercentage()));
        playerStatistics.add(new PlayerData("Player 2", getPlayerTwoWins(), getPlayerTwoLosses(), getPlayerTwoDraws(),getPlayerTwoTotalGames(), (int) getPlayerTwoWinPercentage()));
        // Add more players as needed

        return playerStatistics;
    }


}

