package com.example.basicremotecall;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.FirebaseApp;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SearchResponseViewModel sViewModel;
    ImageViewModel imageViewModel;
    ErrorViewModel errorViewModel;
    Button searchButton;
    ProgressBar progressBar;
    EditText searchKey;
    RecyclerView recyclerView;
    ImageAdapter imageAdapter;
    Button toggleViewButton;

    private String endpoint;
    private boolean isDoubleColumn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        sViewModel = new ViewModelProvider(this).get(SearchResponseViewModel.class);
        imageViewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        errorViewModel = new ViewModelProvider(this).get(ErrorViewModel.class);

        progressBar = findViewById(R.id.progressBar);
        searchKey = findViewById(R.id.inputSearch);
        recyclerView = findViewById(R.id.recyclerView);
        searchButton = findViewById(R.id.searchButton);
        toggleViewButton = findViewById(R.id.toggleViewButton);

        progressBar.setVisibility(View.INVISIBLE);

        // Pass the FragmentManager to ImageAdapter
        imageAdapter = new ImageAdapter(this, getSupportFragmentManager());

        recyclerView.setAdapter(imageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Single-column by default

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchValues = searchKey.getText().toString();  // Get the search input
                APISearchThread searchThread = new APISearchThread(searchValues, MainActivity.this, sViewModel);  // Create and start a search thread

                closeKeyboard();
                progressBar.setVisibility(View.VISIBLE);
                searchThread.start();
            }
        });


        toggleViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toggle between single and double column
                isDoubleColumn = !isDoubleColumn;

                if (isDoubleColumn) {
                    recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2)); // Double-column view
                } else {
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this)); // Single-column view
                }
            }
        });

        // Observe changes in the search response data
        sViewModel.response.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Search Complete", Toast.LENGTH_LONG).show();
                // Set the endpoint here
                endpoint = s;

                // Start the ImageRetrievalThread
                ImageRetrievalThread imageRetrievalThread = new ImageRetrievalThread(MainActivity.this, sViewModel, imageViewModel, errorViewModel);
                progressBar.setVisibility(View.VISIBLE);
                imageRetrievalThread.execute(endpoint);  // Execute the image retrieval thread
            }
        });

        // Observe changes in the image data
        imageViewModel.images.observe(this, new Observer<List<Bitmap>>() {
            @Override
            public void onChanged(List<Bitmap> imageList) {
                progressBar.setVisibility(View.INVISIBLE);
                // Hide the progress bar
                imageAdapter.setImages(imageList);
                // Set images in the adapter
            }
        });

        // Observe changes in the error code
        errorViewModel.errorCode.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                progressBar.setVisibility(View.INVISIBLE);
            }  // Hide the progress bar
        });


    }

    // A method to close the keyboard
    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchKey.getWindowToken(), 0);
    }


}
