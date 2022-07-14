package com.example.gamtrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements TrainerListFragment.Callbacks{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment currentFragment =
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        if (currentFragment == null) {
            Fragment fragment = TrainerListFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onTrainerSelected(UUID trainerId) {
        Fragment fragment = TrainerFragment.newInstance(trainerId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}