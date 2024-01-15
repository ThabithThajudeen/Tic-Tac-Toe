package com.example.mad_assignment1_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    GameFragment3x3 gFragment = new GameFragment3x3();
    GameEndScreen gEndFragment = new GameEndScreen();
    GameFragment4x4 gFragment4x4 = new GameFragment4x4();
    GameFragment5x5 gFragment5x5 = new GameFragment5x5();
    MenuFragment menuFragment = new MenuFragment();
    leaderBoardFragment LBF = new leaderBoardFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Drawable avatar1 = getResources().getDrawable(R.drawable.ic_android_blue_24dp);
        Drawable avatar2 = getResources().getDrawable(R.drawable.ic_android_cyan_24dp);
        Drawable avatar3 = getResources().getDrawable(R.drawable.ic_android_green_24dp);
        Drawable avatar4 = getResources().getDrawable(R.drawable.ic_android_purple_24dp);
        Drawable avatar5 = getResources().getDrawable(R.drawable.ic_android_red_24dp);
        Drawable avatar6 = getResources().getDrawable(R.drawable.ic_android_yellow_24dp);

        ArrayList<Drawable> avatarImages = new ArrayList<>();
        avatarImages.add(avatar1);
        avatarImages.add(avatar2);
        avatarImages.add(avatar3);
        avatarImages.add(avatar4);
        avatarImages.add(avatar5);
        avatarImages.add(avatar6);

        ArrayList<Profile> profileList = new ArrayList<Profile>();

        Profile profile1 = new Profile();
        Profile profile2 = new Profile();
        Profile profile3 = new Profile();
        Profile profile4 = new Profile();

        profile1.setAvatarImage(avatarImages.get(0));
        profile1.setAvatarArrayNumber(0);
        profile2.setAvatarImage(avatarImages.get(1));
        profile2.setAvatarArrayNumber(1);
        profile3.setAvatarImage(avatarImages.get(2));
        profile3.setAvatarArrayNumber(2);
        profile4.setAvatarImage(avatarImages.get(3));
        profile4.setAvatarArrayNumber(3);

        profileList.add(profile1);
        profileList.add(profile2);
        profileList.add(profile3);
        profileList.add(profile4);

        GameResultData mainActivityDataViewModel = new ViewModelProvider(this).get(GameResultData.class);
        mainActivityDataViewModel.setListOfProfiles(profileList);
        mainActivityDataViewModel.setActivePlayer1(profileList.get(0));
        mainActivityDataViewModel.setActivePlayer2(profileList.get(1));
        mainActivityDataViewModel.setAvatarImages(avatarImages);

        loadFragment(R.id.f_container, menuFragment);
    }
    public void loadFragment(int container, Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(container);

        if(frag == null) {
            fm.beginTransaction().add(container, fragment).commit();
        } else {
            fm.beginTransaction().replace(container, fragment).commit();
        }
    }
}