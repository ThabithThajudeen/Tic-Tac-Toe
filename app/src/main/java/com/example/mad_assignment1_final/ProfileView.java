package com.example.mad_assignment1_final;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileView.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileView newInstance(String param1, String param2) {
        ProfileView fragment = new ProfileView();
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
        int screenOrientation = getResources().getConfiguration().orientation;
        View rootView;
        if (screenOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            rootView = inflater.inflate(R.layout.fragment_profile_view_horizontal, container, false);
        }
        else {
            rootView = inflater.inflate(R.layout.fragment_profile_view, container, false);
        }

        Button returnToMain = rootView.findViewById(R.id.menu);

        ConstraintLayout profile1 = rootView.findViewById(R.id.profile_1);
        ImageView profile1Avatar = rootView.findViewById(R.id.profile_image_1);
        Button profile1Edit = rootView.findViewById(R.id.edit_Profile_1);
        Button profile1Set = rootView.findViewById(R.id.set_active_1);
        TextView profile1Text = rootView.findViewById(R.id.profile_name_1);

        ConstraintLayout profile2 = rootView.findViewById(R.id.profile_2);
        ImageView profile2Avatar = rootView.findViewById(R.id.profile_image_2);
        Button profile2Edit = rootView.findViewById(R.id.edit_Profile_2);
        Button profile2Set = rootView.findViewById(R.id.set_active_2);
        TextView profile2Text = rootView.findViewById(R.id.profile_name_2);

        ConstraintLayout profile3 = rootView.findViewById(R.id.profile_3);
        ImageView profile3Avatar = rootView.findViewById(R.id.profile_image_3);
        Button profile3Edit = rootView.findViewById(R.id.edit_Profile_3);
        Button profile3Set = rootView.findViewById(R.id.set_active_3);
        TextView profile3Text = rootView.findViewById(R.id.profile_name_3);

        ConstraintLayout profile4 = rootView.findViewById(R.id.profile_4);
        ImageView profile4Avatar = rootView.findViewById(R.id.profile_image_4);
        Button profile4Edit = rootView.findViewById(R.id.edit_Profile_4);
        Button profile4Set = rootView.findViewById(R.id.set_active_4);
        TextView profile4Text = rootView.findViewById(R.id.profile_name_4);

        GameResultData mainActivityDataViewModel = new ViewModelProvider(getActivity()).get(GameResultData.class);

        ArrayList<Profile> currProfileList =  mainActivityDataViewModel.getListOfProfiles();

        profile1Text.setText(currProfileList.get(0).getName());
        profile2Text.setText(currProfileList.get(1).getName());
        profile3Text.setText(currProfileList.get(2).getName());
        profile4Text.setText(currProfileList.get(3).getName());
        profile1Avatar.setImageDrawable(mainActivityDataViewModel.getListOfProfiles().get(0).getAvatarImage());
        profile2Avatar.setImageDrawable(mainActivityDataViewModel.getListOfProfiles().get(1).getAvatarImage());
        profile3Avatar.setImageDrawable(mainActivityDataViewModel.getListOfProfiles().get(2).getAvatarImage());
        profile4Avatar.setImageDrawable(mainActivityDataViewModel.getListOfProfiles().get(3).getAvatarImage());


        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment(new MenuFragment());
            }
        });

        profile1Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityDataViewModel.setEditPlayer(mainActivityDataViewModel.getListOfProfiles().get(0));
                switchFragment(new ProfileEdit());
            }
        });
        profile2Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityDataViewModel.setEditPlayer(mainActivityDataViewModel.getListOfProfiles().get(1));
                switchFragment(new ProfileEdit());
            }
        });
        profile3Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityDataViewModel.setEditPlayer(mainActivityDataViewModel.getListOfProfiles().get(2));
                switchFragment(new ProfileEdit());
            }
        });
        profile4Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityDataViewModel.setEditPlayer(mainActivityDataViewModel.getListOfProfiles().get(3));
                switchFragment(new ProfileEdit());
            }
        });
        profile1Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mainActivityDataViewModel.getActivePlayer1().equals(mainActivityDataViewModel.getListOfProfiles().get(0))) {
                    mainActivityDataViewModel.setActivePlayer2(mainActivityDataViewModel.getActivePlayer1());
                    mainActivityDataViewModel.setActivePlayer1(mainActivityDataViewModel.getListOfProfiles().get(0));
                }
            }
        });
        profile2Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mainActivityDataViewModel.getActivePlayer1().equals(mainActivityDataViewModel.getListOfProfiles().get(1))) {
                    mainActivityDataViewModel.setActivePlayer2(mainActivityDataViewModel.getActivePlayer1());
                    mainActivityDataViewModel.setActivePlayer1(mainActivityDataViewModel.getListOfProfiles().get(1));
                }
            }
        });
        profile3Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mainActivityDataViewModel.getActivePlayer1().equals(mainActivityDataViewModel.getListOfProfiles().get(2))) {
                    mainActivityDataViewModel.setActivePlayer2(mainActivityDataViewModel.getActivePlayer1());
                    mainActivityDataViewModel.setActivePlayer1(mainActivityDataViewModel.getListOfProfiles().get(2));
                }
            }
        });
        profile4Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mainActivityDataViewModel.getActivePlayer1().equals(mainActivityDataViewModel.getListOfProfiles().get(3))) {
                    mainActivityDataViewModel.setActivePlayer2(mainActivityDataViewModel.getActivePlayer1());
                    mainActivityDataViewModel.setActivePlayer1(mainActivityDataViewModel.getListOfProfiles().get(3));
                }
            }
        });
        return rootView;
    }
    public void switchFragment(Fragment fragment) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.loadFragment(R.id.f_container, fragment);
        }
    }
}