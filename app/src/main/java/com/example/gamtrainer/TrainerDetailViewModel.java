package com.example.gamtrainer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class TrainerDetailViewModel extends ViewModel {

    private TrainerRepository trainerRepository = TrainerRepository.get();
    private MutableLiveData<UUID> trainerIdLiveData = new MutableLiveData<>();

    public LiveData<Trainer> trainerLiveData = Transformations.switchMap(trainerIdLiveData, trainerID -> trainerRepository.getTrainer(trainerID));

    public void loadTrainer(UUID trainerId){
        trainerIdLiveData.setValue(trainerId);
    }

    public LiveData<List<Trainer>> loadTrainers(){
        return trainerRepository.getTrainers();
    }

    public File getPhotoFile(Trainer trainer){
        return trainerRepository.getPhotoFile(trainer);
    }

    public void saveTrainer(Trainer trainer){
        trainerRepository.saveTrainer(trainer);
    }
}
