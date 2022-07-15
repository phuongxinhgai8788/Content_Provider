package com.example.gamtrainer;

import static java.util.Collections.emptyList;

import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class TrainerListFragment extends Fragment {

    private TrainerListViewModel trainerListViewModel;
    private RecyclerView trainerRecyclerView;
    private TrainerAdapter adapter = new TrainerAdapter(emptyList());

    private Context context;
    private Callbacks callback = null;

    public interface Callbacks {
        void onTrainerSelected(UUID trainerId);
    }
    public static TrainerListFragment newInstance() {
        return new TrainerListFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callback = (Callbacks) context;
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.trainer_list_fragment, container, false);
        trainerRecyclerView = view.findViewById(R.id.trainer_recycler_view);
        trainerRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        trainerRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trainerListViewModel = new ViewModelProvider(this).get(TrainerListViewModel.class);

    }

    @Override
    public void onStart() {
        super.onStart();
        trainerListViewModel.trainerListLiveData.observe(getViewLifecycleOwner(), trainers -> {
            if(trainers!=null && trainers.size()>0){
                updateUI(trainers);
                Log.i("TrainerListFragment", "There is trainer in db");
            }
        });
    }

    private void updateUI(List<Trainer> trainers) {
        adapter = new TrainerAdapter(trainers);
        trainerRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_trainer_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(item.getItemId() == R.id.new_trainer){
           Trainer trainer = new Trainer();
           trainerListViewModel.addTrainer(trainer);
           callback.onTrainerSelected(trainer.getId());
           return true;
       } else{
           return super.onOptionsItemSelected(item);
       }
    }

    private class TrainerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Trainer trainer;
        private TextView accountTV, nameTV;
        private ImageView marriedIV, avatarIV;
        private File photoFile;
        private Uri photoUri;

        public TrainerHolder(@NonNull View itemView) {
            super(itemView);
            accountTV = itemView.findViewById(R.id.account_tv);
            nameTV = itemView.findViewById(R.id.name_tv);
            marriedIV = itemView.findViewById(R.id.married_iv);
            avatarIV = itemView.findViewById(R.id.avatar_iv);
            itemView.setOnClickListener(TrainerHolder.this);
        }

        @Override
        public void onClick(View view) {
            callback.onTrainerSelected(trainer.getId());
        }

        public void bind(Trainer trainer){
            this.trainer = trainer;
            accountTV.setText(trainer.getAccount());
            nameTV.setText(trainer.getName());

            if(!trainer.getGetMarried()) {
                marriedIV.setImageDrawable(null);
            }
//            }else{
//                marriedIV.setVisibility(View.GONE);
//            }
            photoFile = trainerListViewModel.getPhotoFile(trainer);
            photoUri = FileProvider.getUriForFile(requireActivity(), "com.example.gamtrainer.fileprovider", photoFile);
            updatePhotoView();
        }

        private void updatePhotoView() {
            if(photoFile.exists()) {
                Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), requireActivity());
                avatarIV.setImageBitmap(bitmap);
            }
//            }else{
//                avatarIV.setImageDrawable(null);
//            }
        }

    }

    private class TrainerAdapter extends RecyclerView.Adapter<TrainerHolder>{

        private List<Trainer> trainers;

        public TrainerAdapter (List<Trainer> trainers){
            this.trainers = trainers;
        }
        @NonNull
        @Override
        public TrainerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(R.layout.list_item_trainer, parent, false);
            return new TrainerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TrainerHolder holder, int position) {
            Trainer trainer = trainers.get(position);
            holder.bind(trainer);
        }

        @Override
        public int getItemCount() {
            return trainers.size();
        }
    }
}