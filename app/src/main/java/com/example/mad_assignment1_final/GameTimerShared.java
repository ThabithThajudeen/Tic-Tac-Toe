package com.example.mad_assignment1_final;

import android.os.CountDownTimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Locale;

public class GameTimerShared extends ViewModel {
    private static final long START_TIME = 16000; //in milliseconds

    private CountDownTimer cdt;
    private MutableLiveData<String> timeDisplay = new MutableLiveData<>();
    private MutableLiveData<Boolean> isTimerRunning = new MutableLiveData<>();

    private MutableLiveData<Void> turnSwitchedEvent = new MutableLiveData<>();

    private long timeLeft = START_TIME; //in milliseconds

    // Expose an event for fragments to observe
    public LiveData<Void> getTurnSwitchedEvent() {
        return turnSwitchedEvent;
    }
    public LiveData<String> getTimeDisplay() {
        return timeDisplay;
    }

    public LiveData<Boolean> getIsTimerRunning() {
        return isTimerRunning;
    }

    public void startTimer() {
        if (cdt != null) {
            cdt.cancel();  // Ensure we're canceling any existing timer
        }

        cdt = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long timeLeftToFinish) {
                timeLeft = timeLeftToFinish;
                updateText();
            }

            @Override
            public void onFinish() {
                turnSwitchedEvent.setValue(null); // Trigger the event
                resetTimer();
                startTimer();  // Restart the timer
            }
        }.start();
        isTimerRunning.setValue(true);
    }

    public void pauseTimer() {
        if (cdt != null) {
            cdt.cancel();
            cdt = null;
            isTimerRunning.setValue(false);
        }
    }

    public void resetTimer() {
        timeLeft = START_TIME;
        updateText();
    }

    private void updateText() {
        int seconds = (int) (timeLeft / 1000) % 60;
        String timeFormat = String.format(Locale.getDefault(), "%02d", seconds);
        timeDisplay.setValue(timeFormat);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (cdt != null) {
            cdt.cancel();
            cdt = null;
        }
    }
}
