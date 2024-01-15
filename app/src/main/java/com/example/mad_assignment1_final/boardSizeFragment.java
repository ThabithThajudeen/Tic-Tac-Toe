package com.example.mad_assignment1_final;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
public class boardSizeFragment extends Fragment {
    private GameResultData viewModel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board_size, container, false);
        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(GameResultData.class);

        Button btn3x3 = view.findViewById(R.id.btn3x3);
        Button btn4x4 = view.findViewById(R.id.btn4x4);
        Button btn5x5 = view.findViewById(R.id.btn5x5);

        btn3x3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(viewModel.getIsAI())
                {
                    loadFragment(new GameFragment3x3_AI());
                }
                else
                {
                    loadFragment(new GameFragment3x3());
                }

            }
        });

        btn4x4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.getIsAI())
                {
                    loadFragment(new GameFragment4x4_AI());
                }
                else
                {
                    loadFragment(new GameFragment4x4());
                }
            }
        });

        btn5x5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewModel.getIsAI())
                {
                    loadFragment(new GameFragment5x5_AI());
                }
                else
                {
                    loadFragment(new GameFragment5x5());
                }

            }
        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.f_container, fragment, "GAME_FRAGMENT_TAG");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
