package com.example.gamtrainer;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.gamtrainer.database.TrainerDao;
import com.example.gamtrainer.database.TrainerDatabase;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TrainerRepository {
    private static TrainerRepository instance;
    private TrainerDao trainerDao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private File filesDir;

    private TrainerRepository(Context context){
        TrainerDatabase db = TrainerDatabase.getInstance(context);
        trainerDao = db.trainerDao();
        filesDir = context.getApplicationContext().getFilesDir();
    }

    public LiveData<List<Trainer>> getTrainers(){
        LiveData<List<Trainer>> trainers = null;
        if(trainerDao !=null){
            trainers = trainerDao.getTrainers();
        }
        return trainers;
    }

    public LiveData<Trainer> getTrainer(UUID uuid){
        LiveData<Trainer> trainer = null;
        if(trainerDao != null){
            trainer = trainerDao.getTrainer(uuid);
        }
        return trainer;
    }

    public File getPhotoFile(Trainer trainer){
        File file = new File(filesDir, trainer.getPhotoFileName());
        return file;
    }

    public static void initialize(Context context){
        if(instance == null){
            instance = new TrainerRepository(context);
        }
    }

    public static TrainerRepository get(){
        if(instance == null){
            throw new IllegalStateException("TrainerRepository must be initialized!");
        }
        return instance;
    }

    public void saveTrainer(Trainer trainer) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                trainerDao.saveTrainer(trainer);
            }
        });
    }
}
