package com.example.madassignment2;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ContactListFragment())
                    .commit();
        }
    }

    public void onContactSavedOrUpdated() {
        ContactListFragment contactListFragment = (ContactListFragment)  // Reload the contact list fragment
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (contactListFragment != null) {
            contactListFragment.loadContacts();
        }
    }

    public void onContactImported() {
        onContactSavedOrUpdated();  // Reload the contact list fragment after importing a contact
    }
}

