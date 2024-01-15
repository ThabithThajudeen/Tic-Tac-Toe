package com.example.mad_assignment1_final;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileEdit#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileEdit extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileEdit() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileEdit.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileEdit newInstance(String param1, String param2) {
        ProfileEdit fragment = new ProfileEdit();
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
        View rootView = inflater.inflate(R.layout.fragment_porfile_edit, container, false);
        ImageView avatarImage = rootView.findViewById(R.id.avatarImage);
        EditText nameText = rootView.findViewById(R.id.nameText);
        Button switchAvatarButton = rootView.findViewById(R.id.editAvatarButton);
        Button saveProfileChanges = rootView.findViewById(R.id.saveChangesButton);
        GameResultData mainActivityDataViewModel = new ViewModelProvider(getActivity()).get(GameResultData.class);
        avatarImage.setImageDrawable(mainActivityDataViewModel.getEditPlayer().getAvatarImage());
        mainActivityDataViewModel.editPlayer.observe(getViewLifecycleOwner(), new Observer<Profile>() {
            @Override
            public void onChanged(Profile profile) {
                nameText.setText(mainActivityDataViewModel.getEditPlayer().getName());
            }
        });
        saveProfileChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivityDataViewModel.getEditPlayer().setName(nameText.getText().toString());
                mainActivityDataViewModel.getEditPlayer().setAvatarImage(avatarImage.getDrawable());
                switchFragment(new ProfileView());
            }
        });
        switchAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                avatarImage.setImageDrawable(mainActivityDataViewModel.getAvatarImages().get(mainActivityDataViewModel.getEditPlayer().getAvatarArrayNumber() % 6));
                mainActivityDataViewModel.getEditPlayer().setAvatarArrayNumber((mainActivityDataViewModel.getEditPlayer().getAvatarArrayNumber() + 1) % 6);
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