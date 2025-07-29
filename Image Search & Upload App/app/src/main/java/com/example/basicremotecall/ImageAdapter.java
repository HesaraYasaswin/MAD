package com.example.basicremotecall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private List<Bitmap> images;
    private int selectedPosition = -1;
    private FragmentManager fragmentManager;

    public ImageAdapter(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager; // Store the FragmentManager
        images = new ArrayList<>();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);  // Create a new view holder by inflating the image item layout
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Bitmap image = images.get(position);
        holder.imageView.setImageBitmap(image);

        // Check if the item should be highlighted as selected
        if (holder.getAdapterPosition() == selectedPosition) {
            // Highlight the selected item
            holder.itemView.setBackgroundResource(R.drawable.selected_item_background);
        } else {
            // Clear the selection
            holder.itemView.setBackgroundResource(0);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the image click here
                selectedPosition = position;
                notifyDataSetChanged(); // Notify that the data has changed to apply the selection effect

                //  pass the selected image to the UploadFragment here
                showUploadFragment(image);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }  // Return the number of items in the dataset

    public void setImages(List<Bitmap> imageList) {  // Set the images to be displayed in the RecyclerView
        images.clear();
        images.addAll(imageList);
        notifyDataSetChanged(); // Notify that the data has changed
    }


    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageItem);
        }
    }

    private void showUploadFragment(Bitmap selectedImage) {
        UploadFragment uploadFragment = new UploadFragment();  // Show the UploadFragment and pass the selected image as an argument
        Bundle args = new Bundle();
        args.putParcelable("selectedImage", selectedImage);
        uploadFragment.setArguments(args);

        FragmentTransaction transaction = fragmentManager.beginTransaction();  // Replace the current fragment with the UploadFragment
        transaction.replace(R.id.fragment_container, uploadFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
