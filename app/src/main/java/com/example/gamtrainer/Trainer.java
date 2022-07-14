package com.example.gamtrainer;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(tableName = "trainer")
public class Trainer {

    @ColumnInfo(name = "trainerId")
    @PrimaryKey
    @NonNull
    private UUID id = UUID.randomUUID();

    @ColumnInfo(name = "name")
    private String name="";

    @ColumnInfo(name = "account")
    private String account="";

    @ColumnInfo(name = "married")
    private Boolean getMarried = false;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber = "";

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Boolean getGetMarried() {
        return getMarried;
    }

    public void setGetMarried(Boolean getMarried) {
        this.getMarried = getMarried;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoFileName(){
        return "IMG_"+id+".jpg";
    }
}
