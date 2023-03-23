package com.example.unilink;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addProfileInterest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addProfileInterest extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addProfileInterest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addProfileInterest.
     */
    // TODO: Rename and change types and number of parameters
    public static addProfileInterest newInstance(String param1, String param2) {
        addProfileInterest fragment = new addProfileInterest();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
            for (int chipId : checkedIds) {
                Chip chip = group.findViewById(chipId);
                if (chip != null){
                    Toast.makeText(this.getActivity(), "Choice:" + chip.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        }));

        moviesChipGroup.setOnCheckedStateChangeListener((((group, checkedIds) -> {
            for (int chipId : checkedIds) {
                Chip chip = group.findViewById(chipId);
                if (chip != null){
                    Toast.makeText(this.getActivity(), "Choice:" + chip.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        })));

        sportsChipGroup.setOnCheckedStateChangeListener((((group, checkedIds) -> {
            for (int chipId : checkedIds) {
                Chip chip = group.findViewById(chipId);
                if (chip != null){
                    Toast.makeText(this.getActivity(), "Choice:" + chip.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        })));

        foodChipGroup.setOnCheckedStateChangeListener((((group, checkedIds) -> {
            for (int chipId : checkedIds) {
                Chip chip = group.findViewById(chipId);
                if (chip != null){
                    Toast.makeText(this.getActivity(), "Choice:" + chip.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        })));

        booksChipGroup.setOnCheckedStateChangeListener((((group, checkedIds) -> {
            for (int chipId : checkedIds) {
                Chip chip = group.findViewById(chipId);
                if (chip != null){
                    Toast.makeText(this.getActivity(), "Choice:" + chip.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        })));

        gamingChipGroup.setOnCheckedStateChangeListener((((group, checkedIds) -> {
            for (int chipId : checkedIds) {
                Chip chip = group.findViewById(chipId);
                if (chip != null){
                    Toast.makeText(this.getActivity(), "Choice:" + chip.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        })));

        List<Integer> checkChipsIdList = new ArrayList<>();
        checkChipsIdList.addAll(musicChipGroup.getCheckedChipIds());
        checkChipsIdList.addAll(moviesChipGroup.getCheckedChipIds());
        checkChipsIdList.addAll(foodChipGroup.getCheckedChipIds());
        checkChipsIdList.addAll(booksChipGroup.getCheckedChipIds());
        checkChipsIdList.addAll(gamingChipGroup.getCheckedChipIds());



    //https://www.youtube.com/watch?v=GtsmzUBbeKE

        return view;
    }

    public void isButtonEnabled(){

    }
}