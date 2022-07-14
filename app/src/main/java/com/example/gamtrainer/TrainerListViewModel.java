package com.example.gamtrainer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TrainerListViewModel extends ViewModel {
    private TrainerRepository trainerRepository = TrainerRepository.get();

    LiveData<List<Trainer>> trainerListLiveData = trainerRepository.getTrainers();

    public void addTrainer(Trainer trainer){
        trainerRepository.saveTrainer(trainer);
    }
}