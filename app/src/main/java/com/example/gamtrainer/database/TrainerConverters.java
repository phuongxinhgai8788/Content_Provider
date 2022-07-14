package com.example.gamtrainer.database;

import androidx.room.TypeConverter;

import java.util.Date;
import java.util.UUID;

public class TrainerConverters {
    @TypeConverter
    public Long fromDate(Date date){
        return date.getTime();
    }

    @TypeConverter
    public Date toDate(Long milisSinceEpoch){
        return new Date(milisSinceEpoch);
    }

    @TypeConverter
    public UUID toUUID(String uuid){
        return UUID.fromString(uuid);
    }

    @TypeConverter
    public String fromUUID(UUID uuid){
        return uuid.toString();
    }
}

