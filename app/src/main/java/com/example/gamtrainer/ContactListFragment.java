package com.example.gamtrainer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactListFragment extends Fragment {

    private RecyclerView contactListRecyclerView;
    private ContactViewModel contactViewModel;
    private Context context;
    private List<Contact> contactList = new ArrayList<>();
    private final String TAG = "ContactListFragment";

    public ContactListFragment() {
        // Required empty public constructor
    }

       public static ContactListFragment newInstance() {
        ContactListFragment fragment = new ContactListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context  = context;
        Log.i(TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.i(TAG, "onCreate");
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_list2, container, false);
        contactListRecyclerView = view.findViewById(R.id.contact_list_container);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contactViewModel = ViewModelProviders.of(this).get(ContactViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        contactList = contactViewModel.getContacts();
        ContactListAdapter adapter = new ContactListAdapter();
        contactListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        contactListRecyclerView.setAdapter(adapter);
    }

    private class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private Contact contact;
        private TextView phoneTV, nameTV;
        private ImageView avatarIV;
//        private File photoFile;
//        private Uri photoUri;


        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            phoneTV = itemView.findViewById(R.id.contact_number);
            nameTV = itemView.findViewById(R.id.contact_name);
            avatarIV = itemView.findViewById(R.id.contact_avatar);
            itemView.setOnClickListener(ContactHolder.this);
        }

        @Override
        public void onClick(View view) {

        }

        public void bind(Contact contact){
            this.contact = contact;
            phoneTV.setText(contact.getPhoneNumber());
            nameTV.setText(contact.getName());
        }
    }
    private class ContactListAdapter extends RecyclerView.Adapter<ContactHolder>{
        @NonNull
        @Override
        public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = getLayoutInflater().from(context);
            View view = layoutInflater.inflate(R.layout.contact_item, parent, false);
            return new ContactHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
            Contact contact = contactList.get(position);
            holder.bind(contact);
        }

        @Override
        public int getItemCount() {
            return contactList.size();
        }
    }
}