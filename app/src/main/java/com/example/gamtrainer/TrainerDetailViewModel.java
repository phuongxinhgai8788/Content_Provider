package com.example.gamtrainer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.UUID;

public class TrainerDetailViewModel extends ViewModel {

    private Repository trainerRepository = Repository.get();
    private MutableLiveData<UUID> trainerIdLiveData = new MutableLiveData<>();

    public LiveData<Trainer> trainerLiveData = Transformations.switchMap(trainerIdLiveData, trainerID -> trainerRepository.getTrainer(trainerID));

    public void loadTrainer(UUID trainerId){
        trainerIdLiveData.setValue(trainerId);
    }

    public File getPhotoFile(Trainer trainer){
        return trainerRepository.getPhotoFile(trainer);
    }

    public void updateTrainer(Trainer trainer){
        trainerRepository.updateTrainer(trainer);
    }
}
