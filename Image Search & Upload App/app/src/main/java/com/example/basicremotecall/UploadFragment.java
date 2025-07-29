package com.example.basicremotecall;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UploadFragment extends Fragment {

    private ImageView uploadedImage;
    private Button uploadButton;
    private Bitmap selectedImage;
    private Button backButton;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase in the fragment (This is where you initialize Firebase)
        FirebaseApp.initializeApp(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upload_fragment, container, false);


        uploadedImage = view.findViewById(R.id.uploadedImage);
        backButton = view.findViewById(R.id.backButton);
        uploadButton = view.findViewById(R.id.uploadButton);


        // Check if an image was passed as an argument
        if (getArguments() != null) {
            selectedImage = getArguments().getParcelable("selectedImage");
            uploadedImage.setImageBitmap(selectedImage);
        }

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Upload the selected image to Firebase Storage
                uploadImageToFirebaseStorage();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

    private void uploadImageToFirebaseStorage()
    {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading Image...");
        progressDialog.show();

        // Generate a unique filename
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now) + ".jpg"; // Add ".jpg" to the filename
        storageReference = FirebaseStorage.getInstance().getReference("images/" + fileName);

        // Convert the Bitmap to a byte array for uploading
        byte[] imageData = convertBitmapToByteArray(selectedImage);


        UploadTask uploadTask = storageReference.putBytes(imageData);  // Upload the image data to Firebase Storage


        uploadTask.addOnSuccessListener(taskSnapshot -> {  // Monitor the upload task and handle success or failure
            // Image uploaded successfully
            progressDialog.dismiss();


            Toast.makeText(getContext(), "Image uploaded to Firebase Storage", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            // Handle the upload failure
            progressDialog.dismiss();

            // You can display an error message to the user
            Toast.makeText(getContext(), "Image upload failed. Please try again.", Toast.LENGTH_SHORT).show();
        });
    }


    // Helper method to convert Bitmap to a byte array
    private byte[] convertBitmapToByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); // Use JPEG format
        return stream.toByteArray();
    }
}
