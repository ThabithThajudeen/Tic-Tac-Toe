package com.example.mad_assignment1_final;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class MarkerSelectionFragment extends Fragment {



    Button x_button;
    Button o_button;

    GameResultData gameResultDataViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.marker_selection, container, false);

        x_button = rootView.findViewById(R.id.x_marker_button);
        o_button = rootView.findViewById(R.id.o_marker_button);

        gameResultDataViewModel = new ViewModelProvider(getActivity())
                .get(GameResultData.class);

        x_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameResultDataViewModel.setGameMarker(0); // marker is X
                gameResultDataViewModel.setGameEnded(0);
            }
        });

        o_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gameResultDataViewModel.setGameMarker(1); // marker is O
                gameResultDataViewModel.setGameEnded(0);
            }
        });

        return rootView;
    }
}
