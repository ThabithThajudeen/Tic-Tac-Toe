package com.example.mad_assignment1_final;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class GameEndScreen extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.game_end_screen_fragment, container, false);

        GameResultData gameResultDataViewModel = new ViewModelProvider(getActivity())
                .get(GameResultData.class);

        Button restartButton = rootView.findViewById(R.id.restartButton);
        TextView resultText = rootView.findViewById(R.id.endingText);

        resultText.setText(gameResultDataViewModel.getGameEndText());

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the gameEnded to 0
                gameResultDataViewModel.setGameEnded(0);

                String gameMode = getArguments().getString("gameMode", "normal");

                if (gameResultDataViewModel.getIsAI())
                {
                    if (gameMode.equals("3x3")) {
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.f_container, new GameFragment3x3_AI(),"GAME_FRAGMENT_TAG")
                                .commit();
                    } else if (gameMode.equals("4x4")) {
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.f_container, new GameFragment4x4_AI(),"GAME_FRAGMENT_TAG")
                                .commit();
                    } else if (gameMode.equals("5x5")) {
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.f_container, new GameFragment5x5_AI(),"GAME_FRAGMENT_TAG")
                                .commit();

                    }
                }
                else
                {
                    if (gameMode.equals("3x3")) {
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.f_container, new GameFragment3x3(),"GAME_FRAGMENT_TAG")
                            .commit();
                    }
                    else if (gameMode.equals("4x4")) {
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.f_container, new GameFragment4x4(),"GAME_FRAGMENT_TAG")
                                .commit();
                    }
                    else if (gameMode.equals("5x5")) {
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.f_container, new GameFragment5x5(),"GAME_FRAGMENT_TAG")
                                .commit();

                }
                }

            }
        });



        return rootView;
    }
}