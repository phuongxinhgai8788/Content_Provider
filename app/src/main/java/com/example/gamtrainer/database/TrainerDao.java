package com.example.gamtrainer.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;

import com.example.gamtrainer.Trainer;

import java.util.List;
import java.util.UUID;

@Dao
public interface TrainerDao {

    @Query("SELECT * FROM trainer")
    LiveData<List<Trainer>> getTrainers();

    @Query("SELECT * FROM trainer WHERE trainerId=(:id)")
    LiveData<Trainer> getTrainer(UUID id);

    @Update
    void saveTrainer(Trainer trainer);
}
