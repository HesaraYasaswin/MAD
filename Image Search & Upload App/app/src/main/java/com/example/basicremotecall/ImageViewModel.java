package com.example.basicremotecall;

import android.graphics.Bitmap;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ImageViewModel extends ViewModel {
    public MutableLiveData<List<Bitmap>> images;

    public ImageViewModel() {
        images = new MutableLiveData<>();
    }  // Initialize a MutableLiveData to hold a list of Bitmap images

    public List<Bitmap> getImages() {
        return images.getValue();
    } // Get the list of images as a List of Bitmap

    public void setImages(List<Bitmap> imageList) {
        images.postValue(imageList);
    }  // Set the list of images in the MutableLiveData
}
