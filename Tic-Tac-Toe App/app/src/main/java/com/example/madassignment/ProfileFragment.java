package com.example.madassignment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }
    private SharedViewModel viewModel;
    ImageView[] profileAvatar;
    ImageButton[] xMark;
    ImageButton[] oMark;
    private String name;
    private Bitmap avatar;
    private Bitmap xMarker;
    private Bitmap oMarker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        EditText profileName = rootView.findViewById(R.id.editTextProfileName);
        ImageView showAvatar = rootView.findViewById(R.id.imageViewShowAvatar);
        Button createProfile = rootView.findViewById(R.id.buttonCreateProfile);
        profileAvatar = new ImageView[6];
        xMark = new ImageButton[5];
        oMark = new ImageButton[5];
        for (int i = 0; i < 6; i++) {
            String buttonID = "imageViewAvatar" + (i+1);
            int resID = getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());
            profileAvatar[i] = rootView.findViewById(resID);
        }
        for (int i = 0; i < 5; i++) {
            String buttonID = "imageButtonX" + (i+1);
            int resID = getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());
            xMark[i] = rootView.findViewById(resID);
        }
        for (int i = 0; i < 5; i++) {
            String buttonID = "imageButtonO" + (i+1);
            int resID = getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());
            oMark[i] = rootView.findViewById(resID);
        }

        for(int i = 0; i < 6; i++){
            int fi = i;
            profileAvatar[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable drawable = profileAvatar[fi].getDrawable();
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    showAvatar.setImageDrawable(drawable);
                }
            });
        }

        for(int i = 0; i < 5; i++){
            int fi = i;
            xMark[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable drawable = xMark[fi].getDrawable();
                    xMark[fi].setAlpha(1.0f);
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    xMarker = bitmapDrawable.getBitmap();//put to viewModel
                    for (int j = 0; j < 5; j++) {
                        // Skip the button that was clicked
                        if (j != fi) {
                            xMark[j].setAlpha(0.5f);
                        }
                    }
                }
            });
            oMark[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Drawable drawable = oMark[fi].getDrawable();
                    oMark[fi].setAlpha(1.0f);
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    oMarker = bitmapDrawable.getBitmap();//put to viewModel
                    for (int j = 0; j < 5; j++) {
                        // Skip the button that was clicked
                        if (j != fi) {
                            oMark[j].setAlpha(0.5f);
                        }
                    }
                }
            });
        }
        createProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = profileName.getText().toString();

                Drawable drawable = showAvatar.getDrawable();
                // Check if any variable is null or empty
                if (name.trim().isEmpty() || drawable==null/*avatar == null*/ || xMarker == null || oMarker == null) {
                    Toast.makeText(requireActivity(), "Please complete all profile details!", Toast.LENGTH_SHORT).show();
                    return;
                }
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                avatar = bitmapDrawable.getBitmap();

                Profile profile = new Profile();
                profile.setAvatar(avatar);
                profile.setName(name);
                profile.setXMarker(xMarker);
                profile.setOMarker(oMarker);

                viewModel.addProfile(profile);

                MainMenuFragment mainMenuFragment = new MainMenuFragment();
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.FrameMenu, mainMenuFragment);
                fragmentTransaction.commit();
            }
        });
        return rootView;
    }
}