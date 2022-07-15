package com.example.gamtrainer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ContactViewModel extends ViewModel {
   private Repository repository = Repository.get();

   public List<Contact>getContacts(){
      return repository.getContacts();
   }

}
