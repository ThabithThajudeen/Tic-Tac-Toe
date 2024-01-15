package com.example.mad_assignment1_final;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    //  ImageView imageView;
    TextView nameView,nameView2,nameView3,nameView4,nameView5,nameView6;
    Button button;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        // imageView = itemView.findViewById(R.id.imageview);
        nameView = itemView.findViewById(R.id.name);
        nameView2 = itemView.findViewById(R.id.wins);
        nameView3 = itemView.findViewById(R.id.losses);
        nameView4 = itemView.findViewById(R.id.Draws);
        nameView5 = itemView.findViewById(R.id.totalGames);
        nameView6 = itemView.findViewById(R.id.Percentage);
        button = itemView.findViewById(R.id.button);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Switch to MainMenuFragment
                switchFragment(new MenuFragment());
            }
        });


        //emailView = itemView.findViewById(R.id.email);

    }

    public void switchFragment(Fragment fragment) {
        if (itemView.getContext() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) itemView.getContext();
            mainActivity.loadFragment(R.id.f_container,fragment);
        } else {
            Toast.makeText(itemView.getContext(), "Not an instance of MainActivity", Toast.LENGTH_SHORT).show();
        }
    }


}
