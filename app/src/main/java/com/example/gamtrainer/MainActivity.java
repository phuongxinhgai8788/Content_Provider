package com.example.gamtrainer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements TrainerListFragment.Callbacks, TrainerFragment.Callbacks{

    public final int REQUEST_CODE = 1;
    private final String TAG = "MainActivity";
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

    @Override
    public void requestPermission() {
        Log.i(TAG, "requestPermission");
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if(permissionCheck == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
            Log.i(TAG, "PERMISSION_DENIED");
        }else {
            openContactListFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE:
                if((grantResults.length>0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    Log.i(TAG, "PERMISSION_GRANTED");
                    openContactListFragment();
                }
                break;
            default:
                break;
        }
    }

    private void openContactListFragment() {
        Log.i(TAG, "openContactListFragment");
        Fragment currentFragment =
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        Fragment fragment = ContactListFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        if (currentFragment == null) {
            fragmentTransaction
                    .add(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }else{
            fragmentTransaction
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

//    private boolean hasPhoneContactsPermission(String permission) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            int hasPermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
//            return hasPermission == PackageManager.PERMISSION_GRANTED;
//        } else {
//            return true;
//        }
//    }
    }
