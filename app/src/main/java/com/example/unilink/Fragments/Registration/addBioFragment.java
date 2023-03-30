package com.example.unilink.Fragments.Registration;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.unilink.Models.UnilinkAccount;
import com.example.unilink.R;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addBioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addBioFragment extends Fragment {

    private ProfileSetupListener listener;
    public addBioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment addBioFragment.
     */

    public static addBioFragment newInstance() {
        addBioFragment fragment = new addBioFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context ctx){
        super.onAttach(ctx);
        try {
            listener = (ProfileSetupListener) ctx;
        } catch (ClassCastException e) {
            throw new ClassCastException(ctx.toString() + " must implement ProfileSetupListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_bio, container, false);
        TextInputLayout bioInput = view.findViewById(R.id.textField);
        bioInput.setOnFocusChangeListener((view1, b) -> {
            if (bioInput.hasFocus())
                listener.AddedBio(bioInput.getEditText().getText().toString());
        });
        return view;
    }

}