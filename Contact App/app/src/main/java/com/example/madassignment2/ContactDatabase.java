package com.example.madassignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import java.util.ArrayList;
import java.util.List;

public class ContactDatabase extends SQLiteOpenHelper {
    private static final String DATA_NAME = "contact.db";  // the name of the SQLite database file
    private static final int DATA_VER = 2; // version
    private static final String TABLE_CONTACTS = "contacts";  // contact table in the database


    // contact table creation SQL statement
    private static final String TABLE_CREATE =
            "CREATE TABLE contacts (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, phoneNumber TEXT, email TEXT, photoPath TEXT, display_name TEXT);";

    public ContactDatabase(Context context) {
        super(context, DATA_NAME, null, DATA_VER); // initializing the database
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onCreate(SQLiteDatabase db) {  db.execSQL(TABLE_CREATE);    // Create the 'contacts' table in the database
    }


    // CRUD operations (create,read,update,delete)

    // Create operation
    public long insertContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get a writable database
        ContentValues values = new ContentValues();
        values.put("name", contact.getName());
        values.put("phoneNumber", contact.getPhoneNumber());
        values.put("photoPath", contact.getPhotoPath());
        values.put("email", contact.getEmail());
        long id = db.insert("contacts", null, values); // Insert the new contact
        db.close();
        return id;
    }

    // Read operation
    public List<Contact> readContacts() {
        List<Contact> contactList = new ArrayList<>();
        String selectQuery = "SELECT * FROM contacts";  // SQL query to retrieve all contacts
        SQLiteDatabase db = this.getWritableDatabase();  // Get a writable database
        Cursor cursor = db.rawQuery(selectQuery, null);  // Execute the query

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getLong(0));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                contact.setEmail(cursor.getString(3));
                contact.setPhotoPath(cursor.getString(4));
                contactList.add(contact);    // Add the contact to the list
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }

    // Update operation
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", contact.getName());
        values.put("phoneNumber", contact.getPhoneNumber());
        values.put("photoPath", contact.getPhotoPath());
        values.put("email", contact.getEmail());
        int rowsAffected = db.update("contacts", values, "_id = ?", new String[]{String.valueOf(contact.getId())});
        db.close();
        return rowsAffected;
    }

    // Delete operation
    public void deleteContact(long contactId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("contacts", "_id = ?", new String[]{String.valueOf(contactId)}); // deletes the contact by its ID
        db.close();
    }

    // to prevent the insertion of duplicate contacts in the database
    // Before inserting a new contact, this method is used to check if a contact with the same name already exists.
    public boolean contactExists(Contact contact) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;     // Declare a cursor for database query

        try {
            String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " = ?";  // Define the selection criteria based on contact's name
            String[] selectionArgs = { contact.getName() };  // Define the selection arguments with the contact's name

            cursor = db.query(          // Query the database to check if the contact already exists
                    TABLE_CONTACTS,   // Table name to query
                    null,
                    selection,        // Selection criteria (the WHERE clause)
                    selectionArgs,
                    null,
                    null,
                    null
            );
            return cursor != null && cursor.moveToFirst();  // Check if a contact with the same name was found
        } finally {
            if (cursor != null) {
                cursor.close();       // Close cursor to release resources
            }
        }
    }
}