package com.example.mad_assignment1_final;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.mad_assignment1_final.R;

public class InGameMenuFragment extends Fragment {


    private Button btnThemes;
    private Button btnCondition3;
    private Button btnCondition4;
    private Button btnCondition5;
    private Button btnMainMenu;
    private Button btnResume;
    private String fragmentToResumeTag = "";  // Default to empty
    GameResultData gameResultDataViewModel;

    GameTimerShared gameTimerShared;
    public void setFragmentToResumeTag(String tag) {
        this.fragmentToResumeTag = tag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_ingame, container, false);

        gameResultDataViewModel = new ViewModelProvider(getActivity())
                .get(GameResultData.class);
        gameTimerShared = new ViewModelProvider(getActivity()).get(GameTimerShared.class);


        btnThemes = view.findViewById(R.id.btnThemes);

        btnThemes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to ThemesFragment
                //switchFragment(new ThemesFragment());
            }
        });
        btnCondition3 = view.findViewById(R.id.btnCondition3);

        btnCondition3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameResultDataViewModel.setRequiredMoves(3);

                if (getParentFragmentManager() != null) {
                    Fragment gameFragment = getParentFragmentManager().findFragmentByTag(fragmentToResumeTag);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    if (gameFragment != null) {
                        transaction.show(gameFragment);
                    }
                    getParentFragmentManager().popBackStack();

                    gameTimerShared.startTimer();
                }
            }
        });

        btnCondition4 = view.findViewById(R.id.btnCondition4);

        btnCondition4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameResultDataViewModel.setRequiredMoves(4);

                if (getParentFragmentManager() != null) {
                    Fragment gameFragment = getParentFragmentManager().findFragmentByTag(fragmentToResumeTag);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    if (gameFragment != null) {
                        transaction.show(gameFragment);
                    }
                    getParentFragmentManager().popBackStack();

                    gameTimerShared.startTimer();
                }
            }
        });
        btnCondition5 = view.findViewById(R.id.btnCondition5);

        btnCondition5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameResultDataViewModel.setRequiredMoves(5);

                if (getParentFragmentManager() != null) {
                    Fragment gameFragment = getParentFragmentManager().findFragmentByTag(fragmentToResumeTag);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    if (gameFragment != null) {
                        transaction.show(gameFragment);
                    }
                    getParentFragmentManager().popBackStack();

                    gameTimerShared.startTimer();
                }
            }
        });

        btnMainMenu = view.findViewById(R.id.btnMainMenu);

        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to MainMenuFragment
                gameTimerShared.resetTimer();
                switchFragment(new MenuFragment());
            }
        });


        btnResume = view.findViewById(R.id.btnResume);

        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager() != null) {
                    Fragment gameFragment = getParentFragmentManager().findFragmentByTag(fragmentToResumeTag);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    if (gameFragment != null) {
                        transaction.show(gameFragment);
                    }
                    getParentFragmentManager().popBackStack();

                    gameTimerShared.startTimer();
                }
            }
        });




        return view;
    }

    public void switchFragment(Fragment fragment) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.loadFragment(R.id.f_container,fragment);
        }
    }


}
