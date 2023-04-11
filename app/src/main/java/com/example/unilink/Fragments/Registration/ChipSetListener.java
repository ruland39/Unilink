package com.example.unilink.Fragments.Registration;

import com.google.android.material.chip.Chip;

import java.util.Set;

public interface ChipSetListener {
    void onChipAdded(Chip c);
    void onChipRemoved();
    void onChipsAdded(Set<Chip> c);
}
