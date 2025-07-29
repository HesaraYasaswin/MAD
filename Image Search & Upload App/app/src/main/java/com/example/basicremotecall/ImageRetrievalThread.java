package com.example.basicremotecall;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class ImageRetrievalThread extends AsyncTask<String, Void, List<Bitmap>> {
    private RemoteUtilities remoteUtilities;
    private SearchResponseViewModel sViewModel;
    private ImageViewModel imageViewModel;
    private ErrorViewModel errorViewModel;
    private Activity uiActivity;

    public ImageRetrievalThread(Activity uiActivity, SearchResponseViewModel viewModel, ImageViewModel imageViewModel, ErrorViewModel errorViewModel) {
        remoteUtilities = RemoteUtilities.getInstance(uiActivity);
        this.sViewModel = viewModel;
        this.imageViewModel = imageViewModel;
        this.errorViewModel = errorViewModel;
        this.uiActivity = uiActivity;
    }

    @Override
    protected List<Bitmap> doInBackground(String... endpoints) {
        if (endpoints != null && endpoints.length > 0) {
            String endpoint = endpoints[0];
            return getImagesFromEndpoint(endpoint);
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Bitmap> images) {
        if (images == null) {
            uiActivity.runOnUiThread(new Runnable() {   // Handle case when no images are found
                @Override
                public void run() {
                    Toast.makeText(uiActivity, "No image found", Toast.LENGTH_LONG).show();
                    errorViewModel.setErrorCode(errorViewModel.getErrorCode() + 1);
                }
            });
        } else {
            uiActivity.runOnUiThread(new Runnable() {  // Handle case when images are found
                @Override
                public void run() {
                    if (images.isEmpty()) {
                        Toast.makeText(uiActivity, "No images to display", Toast.LENGTH_LONG).show();
                    } else {
                        imageViewModel.setImages(images);  // Set the retrieved images in the view model
                    }
                }
            });
        }
    }

    // Retrieve images from the provided JSON data
    private List<Bitmap> getImagesFromEndpoint(String data) {
        List<Bitmap> images = new ArrayList<>();

        try {
            if (data.startsWith("{") && data.endsWith("}")) {
                JSONObject jBase = new JSONObject(data);  // Parse the JSON response

                if (jBase.has("hits")) {
                    JSONArray jHits = jBase.getJSONArray("hits");
                    int numImagesToLoad = Math.min(jHits.length(), 15); // Limit to 15 images

                    Log.d("ImageRetrievalThread", "Number of images to load: " + numImagesToLoad);

                    for (int i = 0; i < numImagesToLoad; i++) {
                        JSONObject jHitsItem = jHits.getJSONObject(i);
                        String imageUrl = jHitsItem.optString("largeImageURL");

                        if (!imageUrl.isEmpty()) {
                            Bitmap image = getImageFromUrl(imageUrl); // Load and add the image to the list
                            if (image != null) {
                                images.add(image);
                            } else {
                                Log.e("ImageRetrievalThread", "Failed to load image from URL: " + imageUrl);
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ImageRetrievalThread", "JSON parsing error: " + e.getMessage());
        }

        Log.d("ImageRetrievalThread", "Number of images retrieved: " + images.size());

        return images;
    }

    // Retrieve a Bitmap image from a URL
    private Bitmap getImageFromUrl(String imageUrl) {
        Bitmap image = null;
        Uri.Builder url = Uri.parse(imageUrl).buildUpon();
        String urlString = url.build().toString();
        HttpURLConnection connection = remoteUtilities.openConnection(urlString);
        if (connection != null) {
            if (remoteUtilities.isConnectionOkay(connection) == true) {
                image = getBitmapFromConnection(connection);  // Download and decode the image from the connection
                connection.disconnect();
            }
        }
        return image;
    }

    // Get a Bitmap from an open connection
    private Bitmap getBitmapFromConnection(HttpURLConnection conn) {
        Bitmap data = null;
        try {
            InputStream inputStream = conn.getInputStream();  // Get an input stream from the connection and decode it into a Bitmap
            data = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}

