package com.example.gamtrainer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.gamtrainer.database.TrainerDao;
import com.example.gamtrainer.database.TrainerDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private static final String TAG = "Repository";
    private static Repository instance;
    private TrainerDao trainerDao;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private File filesDir;
    private Context context;

    private List<Contact> contacts = new ArrayList<>();

    private Repository(Context context){
        TrainerDatabase db = TrainerDatabase.getInstance(context);
        trainerDao = db.trainerDao();
        filesDir = context.getApplicationContext().getFilesDir();
        this.context = context;
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


    public void addTrainer(Trainer trainer) {
        executor.execute(() -> trainerDao.addTrainer(trainer));
    }

    public void updateTrainer(Trainer trainer){
        executor.execute(() -> trainerDao.updateTrainer(trainer));
    }

    public List<Contact> getContacts() {

        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");

        if ((cursor != null ? cursor.getCount() : 0) > 0) {
            while (cursor.moveToNext()) {

                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                @SuppressLint("Range") String phoneNo = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                @SuppressLint("Range") String photoUri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

                Contact contact = new Contact();
                contact.setName(name);
                contact.setPhoneNumber(phoneNo);
                contact.setPhotoUri(photoUri);
                Log.i(TAG, contact.toString());
                contacts.add(contact);

            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return contacts;
    }
    public static void initialize(Context context){
        if(instance == null){
            instance = new Repository(context);
        }
    }

    public static Repository get(){
        if(instance == null){
            throw new IllegalStateException("TrainerRepository must be initialized!");
        }
        return instance;
    }
}
