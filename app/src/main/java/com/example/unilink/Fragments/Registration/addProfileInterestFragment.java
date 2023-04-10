package com.example.unilink.Fragments.Registration;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unilink.Models.UnilinkAccount;
import com.example.unilink.R;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addProfileInterestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addProfileInterestFragment extends Fragment {
    private UnilinkAccount uAcc;
    private ProfileSetupListener listener;
    private List<Enum> chosenInterests = new ArrayList<>();

    public addProfileInterestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment addProfileInterest.
     */
    public static addProfileInterestFragment newInstance(UnilinkAccount uAcc) {
        addProfileInterestFragment fragment = new addProfileInterestFragment();
        Bundle args = new Bundle();
        args.putParcelable("Account",uAcc);
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
        if (getArguments() != null) {
            uAcc = getArguments().getParcelable("Account");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_profile_interest, container, false);

        ChipGroup musicChipGroup = view.findViewById(R.id.musicchipgroup);
        ChipGroup moviesChipGroup = view.findViewById(R.id.movieschipgroup);
        ChipGroup sportsChipGroup = view.findViewById(R.id.sportschipgroup);
        ChipGroup foodChipGroup = view.findViewById(R.id.foodchipgroup);
        ChipGroup booksChipGroup = view.findViewById(R.id.bookschipgroup);
        ChipGroup gamingChipGroup = view.findViewById(R.id.gamingchipgroup);

        musicChipGroup.setOnCheckedStateChangeListener(((group, checkedIds) -> {

        }));

        moviesChipGroup.setOnCheckedStateChangeListener((((group, checkedIds) -> {

        })));

        sportsChipGroup.setOnCheckedStateChangeListener((((group, checkedIds) -> {

        })));

        foodChipGroup.setOnCheckedStateChangeListener((((group, checkedIds) -> {

        })));

        booksChipGroup.setOnCheckedStateChangeListener((((group, checkedIds) -> {

        })));

        gamingChipGroup.setOnCheckedStateChangeListener((((group, checkedIds) -> {

        })));

        List<Enum> musicInterests = parseChosenInterests(musicChipGroup.getCheckedChipIds());

        return view;
    }

    /**
     * A Method to parse the chosen chip Ids into it's specific
     * Interest Enum, per it's category.
     * @param chosenChips
     * @return A List of Enumerators to be added into the user's list
     */
    private List<Enum> parseChosenInterests(List<Integer> chosenChips) {

        return null;
    }
}