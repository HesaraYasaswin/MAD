package com.example.madassignment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class SettingFragment extends Fragment {

    private SharedViewModel viewModel;

    public SettingFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        Button undoButton = rootView.findViewById(R.id.buttonUndo);
        Button redo = rootView.findViewById(R.id.buttonRedo);

        if(viewModel.getGameAgainst().getValue() == false){
            undoButton.setEnabled(false);
            undoButton.setVisibility(View.INVISIBLE);
        }

        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setTriggerUndo(true);
            }
        });

        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.setTriggerReset(true);
            }
        });



        return rootView;
    }
}
