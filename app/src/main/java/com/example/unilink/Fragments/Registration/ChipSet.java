package com.example.unilink.Fragments.Registration;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.chip.Chip;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChipSet implements Set<Chip> {
    private Set<Chip> chipSet;
    private ChipSetListener chipSetListener;

    public ChipSet(){
        chipSet = new HashSet<>();
    }
    public void setChipSetListener(ChipSetListener chipSetListener){
        this.chipSetListener = chipSetListener;
    }

    @Override
    public int size() {
        return chipSet.size();
    }

    @Override
    public boolean isEmpty() {
        return chipSet.isEmpty();
    }

    @Override
    public boolean contains(@Nullable Object o) {
        return chipSet.contains(o);
    }

    @NonNull
    @Override
    public Iterator<Chip> iterator() {
        return chipSet.iterator();
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return chipSet.toArray();
    }

    @NonNull
    @Override
    public <T> T[] toArray(@NonNull T[] ts) {
        return chipSet.toArray(ts);
    }

    @Override
    public boolean add(Chip chip) {
        if(chipSetListener != null)
            chipSetListener.onChipAdded(chip);
        return chipSet.add(chip);
    }
    public boolean add(@NonNull View v, Integer integer){
        Chip c = v.findViewById(integer);
        if(chipSetListener != null)
            chipSetListener.onChipAdded(c);
        return chipSet.add(c);
    }

    @Override
    public boolean remove(@Nullable Object o) {
        if (chipSetListener != null)
            chipSetListener.onChipRemoved();
        return chipSet.remove(o);
    }
    public boolean remove(@NonNull View v, Integer integer){
        Chip c = v.findViewById(integer);
        if (chipSetListener != null)
            chipSetListener.onChipRemoved();
        return chipSet.remove(c);
    }

    @Override
    public boolean containsAll(@NonNull Collection<?> collection) {
        return chipSet.containsAll(collection);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends Chip> collection) {
        return chipSet.addAll(collection);
    }
    public boolean addAll(@NonNull View v, @NonNull Collection<? extends Integer> collection, @NonNull Class category) {
        Set<Chip> temp = new HashSet<>();
        for(Integer i : collection){
            Chip c = v.findViewById(i);
            temp.add(c);
        }
        if (chipSetListener != null)
            chipSetListener.onChipsAdded(chipSet);
        return chipSet.addAll(temp);
    }

    @Override
    public boolean retainAll(@NonNull Collection<?> collection) {
        return chipSet.retainAll(collection);
    }

    @Override
    public boolean removeAll(@NonNull Collection<?> collection) {
        return chipSet.removeAll(collection);
    }

    @Override
    public void clear() {
        chipSet.clear();
    }
}
