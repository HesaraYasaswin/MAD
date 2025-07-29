package com.example.madassignment2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private OnItemClickListener onItemClickListener;
    private Context context;
    private ArrayList<Contact> contactsList;



    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Contact contact);  // Interface for item click event handling
    }

    public ContactListAdapter(Context context, ArrayList<Contact> contactsList) {
        this.context = context;
        this.contactsList = contactsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(view);  // Inflates the layout for a list item and creates a new ViewHolder
    }

    // for updating the content of each item in the recyclerview as the user scrolls
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contactsList.get(position); // Get the current contact

        holder.nameTextView.setText(contact.getName());    // Bind data to UI elements
        holder.phoneTextView.setText(contact.getPhoneNumber());

        if (holder.profileImageView != null) {   // Check if the ImageView is null
            if (contact.getPhotoPath() != null) {
                Bitmap photoBitmap = BitmapFactory.decodeFile(contact.getPhotoPath());  // Load and set the image if it's not null
                if (photoBitmap != null) {
                    holder.profileImageView.setImageBitmap(photoBitmap);
                    holder.profileImageView.setVisibility(View.VISIBLE);
                }
            } else {
                holder.profileImageView.setImageResource(R.drawable.defualt_profile);  // Set a default image if the photo path is null
                holder.profileImageView.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public int getItemCount() {return contactsList.size();}    // Return the number of items in the list



    // interaction between the user and the individual contact items displayed in the RecyclerView , it triggers the appropriate action when an item clicked.
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profileImageView;
        TextView nameTextView;
        TextView phoneTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemClickListener != null) {
                        onItemClickListener.onItemClick(contactsList.get(position));
                    }
                }
            });
        }
    }

}



