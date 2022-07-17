package com.example.gamtrainer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.FileUtils;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class TrainerFragment extends Fragment {

    private final String TAG = "TrainerFragment";
    private static final String ARG_TRAINER_ID = "trainer_id";
    private final int REQUEST_CONTACT = 0;
    private final int REQUEST_PHOTO = 1;


    // TODO: Rename and change types of parameters
    private Trainer trainer;
    private File photoFile;
    private Uri photoUri;
    private EditText nameET, accountET, phoneET;
    private CheckBox marriedCheckBox;
    private Button callBtn, sendEmailBtn;
    private ImageButton takePhotoImageBtn;
    private ImageView avatarIV;
    private TrainerDetailViewModel trainerDetailViewModel;
    private Callbacks callbacks;

    private UUID trainerUUID;

    interface Callbacks{
        void requestPermission();
    }
    public TrainerFragment() {
        // Required empty public constructor
    }

    public static TrainerFragment newInstance(UUID param1) {
        TrainerFragment fragment = new TrainerFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TRAINER_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            trainerUUID = (UUID) getArguments().getSerializable(ARG_TRAINER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_trainer, container, false);
        nameET = view.findViewById(R.id.name_edit_text);
        accountET = view.findViewById(R.id.account_edit_text);
        phoneET = view.findViewById(R.id.phone_edit_text);
        avatarIV = view.findViewById(R.id.avatar);
        takePhotoImageBtn = view.findViewById(R.id.open_camera_btn);
        callBtn = view.findViewById(R.id.call_btn);
        sendEmailBtn = view.findViewById(R.id.send_message_btn);
        marriedCheckBox = view.findViewById(R.id.married_checkbox);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trainerDetailViewModel = ViewModelProviders.of(this).get(TrainerDetailViewModel.class);
        trainerDetailViewModel.loadTrainer(trainerUUID);
        trainerDetailViewModel.trainerLiveData.observe(
                getViewLifecycleOwner(), trainer ->{
                    if(trainer!=null){
                        this.trainer = trainer;
                        photoFile = trainerDetailViewModel.getPhotoFile(trainer);
                        photoUri = FileProvider.getUriForFile(requireActivity(),
                                "com.example.gamtrainer.fileprovider", photoFile);
                        updateUI();
                    }else{
                        this.trainer = new Trainer();
                    }
                }
        );
    }

    private void updateUI() {
        accountET.setText(trainer.getAccount());
        nameET.setText(trainer.getName());
        phoneET.setText(trainer.getPhoneNumber());
        marriedCheckBox.setChecked(trainer.getGetMarried());
        marriedCheckBox.jumpDrawablesToCurrentState();

        updatePhotoView();
    }

    private void updatePhotoView() {
        if(photoFile.exists()){
            Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), requireActivity());
            Log.i(TAG, photoFile.getPath());
            avatarIV.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        TextWatcher accountWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                trainer.setAccount(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        accountET.addTextChangedListener(accountWatcher);

        TextWatcher nameWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                trainer.setName(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        nameET.addTextChangedListener(nameWatcher);

        TextWatcher phoneWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                trainer.setPhoneNumber(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        phoneET.addTextChangedListener(phoneWatcher);

        marriedCheckBox.setOnCheckedChangeListener((compoundButton, isChecked) -> trainer.setGetMarried(isChecked));

        sendEmailBtn.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(Intent.EXTRA_TEXT, "SOME TEXT")
                    .putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
            Intent chooserIntent = Intent.createChooser(intent, "SEND EMAIL TO");
            startActivity(chooserIntent);
        });

        callBtn.setOnClickListener( v -> {
            callbacks.requestPermission();
        });


        PackageManager packageManager = requireActivity().getPackageManager();

        Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        ResolveInfo resolvedActivity = packageManager.resolveActivity(captureImageIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        if( resolvedActivity == null) {
            takePhotoImageBtn.setEnabled(false);
        }
        takePhotoImageBtn.setOnClickListener( v -> {
           captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

           List<ResolveInfo> cameraActivities = packageManager.queryIntentActivities(captureImageIntent,
                   PackageManager.MATCH_DEFAULT_ONLY);

           for(ResolveInfo cameraActivity: cameraActivities){
               requireActivity().grantUriPermission(
                       cameraActivity.activityInfo.packageName,
                       photoUri,
                       Intent.FLAG_GRANT_WRITE_URI_PERMISSION
               );
           }
           startActivityForResult(captureImageIntent, REQUEST_PHOTO);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }else if(requestCode == REQUEST_PHOTO){
            requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        trainerDetailViewModel.updateTrainer(trainer);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // Revoke photo permissions if the user leaves without taking a photo
        requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }
}