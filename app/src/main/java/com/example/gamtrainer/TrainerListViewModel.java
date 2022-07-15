package com.example.gamtrainer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.util.List;

public class TrainerListViewModel extends ViewModel {
    private Repository trainerRepository = Repository.get();

    LiveData<List<Trainer>> trainerListLiveData = trainerRepository.getTrainers();

    public void addTrainer(Trainer trainer){
        trainerRepository.addTrainer(trainer);
    }

    public File getPhotoFile(Trainer trainer) {
       return trainerRepository.getPhotoFile(trainer);
    }
}