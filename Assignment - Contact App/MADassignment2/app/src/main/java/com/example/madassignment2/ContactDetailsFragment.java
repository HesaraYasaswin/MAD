package com.example.madassignment2;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;


public class ContactDetailsFragment extends Fragment {
    private static final int CAMERA_CODE = 100;   // request code for the camera
    private ImageView contactImageView;
    private EditText nameEditText, phoneEditText, emailEditText;
    private Button capturePhotoButton;
    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    private Contact currentContact;
    private boolean isEditing;
    private String photoFile;  //to store the captured photo

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);

        capturePhotoButton = view.findViewById(R.id.capturePhotoButton);
        saveButton = view.findViewById(R.id.saveButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        deleteButton = view.findViewById(R.id.deleteButton);
        nameEditText = view.findViewById(R.id.nameEditText);
        phoneEditText = view.findViewById(R.id.phoneEditText);
        emailEditText = view.findViewById(R.id.emailEditText);
        contactImageView = view.findViewById(R.id.contactImageView);

        photoFile = null;


        Bundle args = getArguments();
        if (args != null) {
            currentContact = (Contact) args.getSerializable("contact");
            if (currentContact != null) {
                // Editing an existing contact
                isEditing = true;
                displayContactData();
            }
        } else {
            // If no arguments are passed so it shows the default image
            contactImageView.setVisibility(View.VISIBLE);
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveUpdateContact();  // Save or update the contact in the database
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();  // Goes back to the previous fragment
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
            }
        });

        capturePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();   // Open the camera to capture a photo
            }
        });


        return view;
    }

    // handles the user's response to the request on camera permisssion
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {  // to get the permission to open the camera
        if (requestCode == CAMERA_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();  // Camera permission granted, open the camera
            } else {
                Toast.makeText(requireContext(), "Camera permission was denied", Toast.LENGTH_SHORT).show(); // Camera permission denied, show a message to the user
            }
        }
    }


    // manges the camera permission and opening when the permission is granted
    private void openCamera() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)  // Check camera permission and request if not granted
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
            return;
        }
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  // Open the camera to capture a photo
        if (cameraIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_CODE);
        }
    }


    // manages the photo capture
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Bundle extras = data.getExtras();  // Photo capture successful, retrieve the image
            if (extras != null) {
                Bitmap photoBitmap = (Bitmap) extras.get("data");
                if (photoBitmap != null) {
                    PhotoStorage(photoBitmap);  // Save the captured photo to storage and display it
                    contactImageView.setImageBitmap(photoBitmap);

                    if (currentContact != null) {  // Update the photo path in the contact
                        currentContact.setPhotoPath(photoFile);
                    }
                }
            }
        }
    }

    // to save the photo to file (phone storage)
    private void PhotoStorage(Bitmap photoBitmap) {  //saves the photo in storage
        String fileName = "contact_" + System.currentTimeMillis() + ".jpg";  // Generate a unique file name for the photo
        File file = new File(requireActivity().getExternalFilesDir(null), fileName);

        // Save the photo to the file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            photoFile = file.getAbsolutePath();  // Store the file path for later use
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayContactData() {  // displays the entered data to the UI
        nameEditText.setText(currentContact.getName());

        if (currentContact.getPhoneNumber() != null) {
            phoneEditText.setText(currentContact.getPhoneNumber());
        }

        emailEditText.setText(currentContact.getEmail());

        if (currentContact.getPhotoPath() != null) {   // Load and display the contact's photo if available
            Bitmap photoBitmap = BitmapFactory.decodeFile(currentContact.getPhotoPath());
            if (photoBitmap != null) {
                contactImageView.setImageBitmap(photoBitmap);
            }
        } else {
            // If the contact's photoPath is null, display the default profile picture
            contactImageView.setImageResource(R.drawable.defualt_profile);
        }
    }
    private void deleteContact() {
        if (currentContact != null) {
            // Delete the contact from the database
            ContactDatabase contactDatabase = new ContactDatabase(requireContext());
            contactDatabase.deleteContact(currentContact.getId());
            contactDatabase.close();
            Toast.makeText(requireContext(), "Contact deleted successfully.", Toast.LENGTH_SHORT).show();

            requireActivity().getSupportFragmentManager().popBackStack();
        }
    }
    private void SaveUpdateContact() {
        // Get the contact details from UI
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        // Check if any required fields are empty
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        ContactDatabase contactDatabase = new ContactDatabase(requireContext());

        if (isEditing) {  // Update existing contact
            currentContact.setName(name);
            currentContact.setPhoneNumber(phone);
            currentContact.setEmail(email);

            if (photoFile != null) {  // Update the photo path if a new photo was captured
                currentContact.setPhotoPath(photoFile);
            }
            // Update the contact in the database
            int rowsUpdated = contactDatabase.updateContact(currentContact);
            if (rowsUpdated > 0) {
                Toast.makeText(requireContext(), "Contact updated successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to update contact.", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Create a new contact
            Contact newContact = new Contact(name, phone, email, photoFile);
            long contactId = contactDatabase.insertContact(newContact);
            if (contactId > 0) {
                Toast.makeText(requireContext(), "Contact saved successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to save contact.", Toast.LENGTH_SHORT).show();
            }
        }
        contactDatabase.close();
        requireActivity().getSupportFragmentManager().popBackStack(); // Goes back to the previous fragment

    }

}