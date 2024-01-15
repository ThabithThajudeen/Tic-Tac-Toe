package com.example.mad_assignment1_final;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link leaderBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class leaderBoardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // private ArrayList<Item> newsArrayList;
    private ArrayList<PlayerData> playerDataArrayList;
    private GameResultData gameResultData; // Declare the variable


    public leaderBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment leaderBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static leaderBoardFragment newInstance(String param1, String param2) {
        leaderBoardFragment fragment = new leaderBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_leader_board, container, false);

        // Initialize RecyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity())); // Set a layout manager (e.g., LinearLayoutManager)

        // Create and set the adapter
        MyAdapter adapter = new MyAdapter(getActivity(), getLeaderboardData()); // Replace with your actual data source
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private List<PlayerData> getLeaderboardData() {
        List<PlayerData> leaderboardData = new ArrayList<>();

        // Get player statistics from your GameResultData instance
        GameResultData gameResultData = new ViewModelProvider(requireActivity()).get(GameResultData.class);
        List<PlayerData> playerStatistics = gameResultData.getPlayerStatistics();

        // Iterate through player statistics and create leaderboard items
        for (PlayerData player : playerStatistics) {
            String playerName = player.getPlayerName();
            int wins = player.getWins();
            int losses = player.getLosses();
            int draws = player.getDraws();
            int total = player.getTotalGames();
            double percentage = player.getWinPercentage();

            // Create a PlayerData object with player data
            PlayerData leaderboardItem = new PlayerData(playerName, wins, losses, draws,total, (int) percentage);

            // Add the item to the leaderboard data list
            leaderboardData.add(leaderboardItem);
        }

        return leaderboardData;
    }

}

