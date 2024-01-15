package com.example.mad_assignment1_final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class MenuFragment extends Fragment {


    private GameResultData viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(GameResultData.class);

        // Handling the btn1v1 click
        Button btn1v1 = view.findViewById(R.id.btn1v1);
        btn1v1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setIsAI(false);
                switchFragment(new boardSizeFragment());
            }
        });

        // Handling the btnAI click
        Button btnAI = view.findViewById(R.id.btnAI);
        btnAI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setIsAI(true);
                switchFragment(new boardSizeFragment());
            }
        });

        // Handling the btnSettings click
        Button btnSettings = view.findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // switchFragment(new PlaceholderSettingsFragment());
            }
        });

        // Handling the btnProfile click
        Button btnProfile = view.findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchFragment(new ProfileView());
            }
        });

        // Handling the btnLeaderBoard click
        Button btnLeaderBoard = view.findViewById(R.id.btnLeaderBoard);
        btnLeaderBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   switchFragment(new PlaceholderLeaderBoardFragment());
                viewModel.setClickedValue(1);
                switchFragment(new leaderBoardFragment());
            }
        });
        return view;
    }

    public void switchFragment(Fragment fragment) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.loadFragment(R.id.f_container, fragment);
        }
    }
}
