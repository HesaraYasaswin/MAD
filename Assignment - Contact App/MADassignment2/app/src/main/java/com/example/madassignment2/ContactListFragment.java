package com.example.madassignment2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ContactListFragment extends Fragment {
    private ArrayList<Contact> contactsList;
    private ContactListAdapter contactListAdapter;
    private RecyclerView recyclerView;
    private ContactDatabase contactDatabase;
    private static final int IMPORT_CODE = 101; // request code for importing
    private static final int PERMISSIONS_CODE = 102; // request code for contact permissions
    private int selectedPosition = RecyclerView.NO_POSITION;
    private Button addContactButton;
    private Button deleteContactButton;
    private Button importButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);

        contactDatabase = new ContactDatabase(requireContext());

        recyclerView = view.findViewById(R.id.recyclerViewContacts);
        contactsList = new ArrayList<>();
        contactListAdapter = new ContactListAdapter(getActivity(), contactsList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());  // Set the layout manager for the RecyclerView
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(contactListAdapter);  // Set the adapter for the RecyclerView


        contactListAdapter.setOnItemClickListener(new ContactListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contact contact) {
                int position = contactsList.indexOf(contact);
                if (position != RecyclerView.NO_POSITION) {
                    openContactDetailsFragment(contactsList.get(position));  // Pass the selected contact to the ContactDetailsFragment
                }
            }
        });

        // to populate UI
        loadContacts();


        addContactButton = view.findViewById(R.id.addContactButton);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactDetailsFragment contactDetailsFragment = new ContactDetailsFragment();  // Open ContactDetailsFragment for adding a new contact
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, contactDetailsFragment);
                transaction.addToBackStack(null); // This allows you to navigate back to the ContactListFragment
                transaction.commit();
            }
        });

        deleteContactButton = view.findViewById(R.id.deleteContactButton);
        deleteContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSelectedContact();
            }
        });

        importButton = view.findViewById(R.id.importButton);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(requireContext(),   // Check for permissions before opening the contact picker
                        Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    openContactdefaultapp();
                } else {
                    ActivityCompat.requestPermissions(requireActivity(),  // Request permission to read contacts
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSIONS_CODE);
                }
            }
        });


        return view;
    }


    public void loadContacts() {
        Set<Contact> uniqueContacts = new HashSet<>();  // Load contacts from the database and update
        uniqueContacts.addAll(contactDatabase.readContacts());
        contactsList.clear();
        contactsList.addAll(uniqueContacts);
        contactListAdapter.notifyDataSetChanged();   // Notify the adapter that the data has changed
    }



    // allows the user to select contacts from the device's default contact list and import the contact to this app
    private void openContactdefaultapp() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, IMPORT_CODE);
    }

    // handles the user's response to the request to access the contacts
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {  // Permission granted, open the contact picker
                openContactdefaultapp();
            } else {
                Toast.makeText(requireContext(), "Permission denied to read contacts", Toast.LENGTH_SHORT).show();  // Permission denied, show a message to the user
            }
        }
    }

    // Handles contact import and database interaction upon user selection
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMPORT_CODE && resultCode == Activity.RESULT_OK) {
            Uri contactUri = data.getData();  // Handle the imported contact data here
            Contact importedContact = importContact(contactUri);

            if (importedContact != null) {
                boolean contactExists = contactDatabase.contactExists(importedContact);
                if (!contactExists) {
                    long contactId = contactDatabase.insertContact(importedContact);  // Insert the imported contact into the database

                    if (contactId > 0) {
                        Toast.makeText(requireContext(), "Contact imported successfully.", Toast.LENGTH_SHORT).show();
                        ((MainActivity) requireActivity()).onContactImported();  // Notify MainActivity that a contact has been imported
                    } else {
                        Toast.makeText(requireContext(), "Failed to import contact.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "Contact already exists.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Failed to import contact.", Toast.LENGTH_SHORT).show();
            }

        }
    }

    // Converts contact details from a URI to a Contact object for usage
    private Contact importContact(Uri contactUri) {
        Contact importedContact = new Contact();

        try {
            // Query the contact details using the contactUri
            ContentResolver contentResolver = requireActivity().getContentResolver();
            Cursor cursor = contentResolver.query(contactUri, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve contact name
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(nameIndex);
                importedContact.setName(name);

                // this check if the contact has phone numbers and retrieve the first one
                @SuppressLint("Range") int hasPhone = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                if (hasPhone > 0) {
                    // Query the phone numbers for the contact
                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[] { String.valueOf(contactUri.getLastPathSegment()) },
                            null
                    );
                    if (phoneCursor != null && phoneCursor.moveToFirst()) {
                        int phoneIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        String phoneNumber = phoneCursor.getString(phoneIndex);
                        importedContact.setPhoneNumber(phoneNumber);
                        phoneCursor.close();
                    }
                }

                // Retrieve contact email if available
                int emailIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS);
                if (emailIndex >= 0) {
                    String email = cursor.getString(emailIndex);
                    importedContact.setEmail(email);
                }
            }

            if (cursor != null) {
                cursor.close();
            }

            // to Check if the contact already exists in the database
            if (contactDatabase.contactExists(importedContact)) {
                Toast.makeText(requireContext(), "Contact already exists.", Toast.LENGTH_SHORT).show();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Failed to import contact.", Toast.LENGTH_SHORT).show();
            return null;
        }
        return importedContact;
    }


    private void deleteSelectedContact() {
        if (selectedPosition != RecyclerView.NO_POSITION) {  // Check if a contact is selected for deletion
            Contact contactToDelete = contactsList.get(selectedPosition);
            contactDatabase.deleteContact(contactToDelete.getId());
            contactsList.remove(selectedPosition);
            contactListAdapter.notifyDataSetChanged();
            selectedPosition = RecyclerView.NO_POSITION;  // Clear the selected position in the recycler view

            Toast.makeText(requireContext(), "Contact deleted successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "Please select a contact to delete.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openContactDetailsFragment(Contact contact) {
        ContactDetailsFragment contactDetailsFragment = new ContactDetailsFragment();

        Bundle args = new Bundle();  // Pass the selected contact as an argument
        args.putSerializable("contact", contact);
        contactDetailsFragment.setArguments(args);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, contactDetailsFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}




