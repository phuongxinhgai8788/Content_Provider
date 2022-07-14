package com.example.gamtrainer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.gamtrainer.Trainer;

@Database(entities = {Trainer.class}, version = 1, exportSchema = true)
@TypeConverters(TrainerConverters.class)
public abstract class TrainerDatabase extends RoomDatabase {

    public abstract TrainerDao trainerDao();

    private static volatile TrainerDatabase INSTANCE;

    public static TrainerDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (TrainerDatabase.class) {
                    INSTANCE = Room.databaseBuilder(context, TrainerDatabase.class, "trainer_database")
                            .allowMainThreadQueries().build();
            }
        }

        return INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }

}
