package com.example.unilink.Fragments.Registration;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.unilink.Models.Interests.Category;
import com.example.unilink.Models.Interests.Interest;
import com.example.unilink.Models.UnilinkAccount;
import com.example.unilink.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addProfileInterestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addProfileInterestFragment extends Fragment {
    private UnilinkAccount uAcc;
    private ProfileSetupListener listener;
    private ChipSet allChips;
    private ChipSet checkedIdsSet;
    public static Set<Interest> chosenInterest = new HashSet<>();
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
        allChips = new ChipSet();
        checkedIdsSet = new ChipSet();
        ChipGroup musicChipGroup = view.findViewById(R.id.musicchipgroup);
        ChipGroup moviesChipGroup = view.findViewById(R.id.movieschipgroup);
        ChipGroup sportsChipGroup = view.findViewById(R.id.sportschipgroup);
        ChipGroup foodChipGroup = view.findViewById(R.id.foodchipgroup);
        ChipGroup booksChipGroup = view.findViewById(R.id.bookschipgroup);
        ChipGroup gamingChipGroup = view.findViewById(R.id.gamingchipgroup);

        addChipsToAll(musicChipGroup, Category.CategoryName.MUSIC);
        addChipsToAll(moviesChipGroup, Category.CategoryName.MOVIES);
        addChipsToAll(sportsChipGroup, Category.CategoryName.SPORTS);
        addChipsToAll(foodChipGroup, Category.CategoryName.FOOD);
        addChipsToAll(booksChipGroup, Category.CategoryName.BOOKS);
        addChipsToAll(gamingChipGroup, Category.CategoryName.GAMING);

        checkedIdsSet.setChipSetListener(new ChipSetListener() {
            @Override
            public void onChipAdded(Chip c) {
                if(checkedIdsSet.size() >= 5){
                    disableNonCheckedChips();
                }
                if (checkedIdsSet.size() >= 3) {
                    listener.AddedInterest();
                }
                chosenInterest = new HashSet<>(parseChosenInterests(checkedIdsSet));
            }
            @Override
            public void onChipRemoved() {
                if(checkedIdsSet.size() <= 6){
                    enableChips();
                }
            }
            @Override
            public void onChipsAdded(Set<Chip> c) {
                if (checkedIdsSet.size() >= 5) {
                    disableNonCheckedChips();
                }
            }
        });
        musicChipGroup.setOnCheckedStateChangeListener((new InterestChipCheckedStateListener()));
        moviesChipGroup.setOnCheckedStateChangeListener(((new InterestChipCheckedStateListener())));
        sportsChipGroup.setOnCheckedStateChangeListener(((new InterestChipCheckedStateListener())));
        foodChipGroup.setOnCheckedStateChangeListener(((new InterestChipCheckedStateListener())));
        booksChipGroup.setOnCheckedStateChangeListener(((new InterestChipCheckedStateListener())));
        gamingChipGroup.setOnCheckedStateChangeListener(((new InterestChipCheckedStateListener())));
        return view;
    }

    private class InterestChipCheckedStateListener implements ChipGroup.OnCheckedStateChangeListener{

        @Override
        public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
            for (int i = 0; i < group.getChildCount(); i++) {
                Chip c = (Chip) group.getChildAt(i);
                if (!c.isChecked()) {
                    checkedIdsSet.remove(c);
                } else {
                    checkedIdsSet.add(c);
                }
            }
        }
    }

    private void addChipsToAll(ChipGroup chipGroup, Category.CategoryName categoryName) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            allChips.add(chip);
            chip.setTag(new Category(0, categoryName));
        }
    }

    /**
     * A Method to parse the chosen chip Ids into it's specific
     * Interest Enum, per it's category.
     * @param chosenChips
     * @return A List of Enumerators to be added into the user's list
     */
    private List<Interest> parseChosenInterests(ChipSet chosenChips){
        List<Interest> interests = new ArrayList<>();
        for (Chip c : chosenChips) {
            String interestName = c.getText().toString();
            Category category = (Category) c.getTag();
            interests.add(new Interest(interestName, category));
        }
        return interests;
    }

    private void enableChips(){
        for(Chip c : allChips){
            c.setEnabled(true);
        }
    }

    private void disableNonCheckedChips(){
       for (Chip c: allChips){
           if(!c.isChecked()){
               c.setEnabled(false);
           }
       }
    }
}