package com.example.unilink.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;

import java.util.ArrayList;

public class ProfileRowAdapter extends RecyclerView.Adapter<ProfileRowAdapter.ViewHolder> {
    private ArrayList<UnilinkUser> mDataset;
    public ProfileRowAdapter() {
        mDataset = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mDataset.isEmpty())
            holder.getText().setText("A Unilink User");
        else
            holder.getText().setText(mDataset.get(position).getFullName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void addUser(UnilinkUser userToBeAdded,int position) {
        this.mDataset.add(position,userToBeAdded);
        this.notifyItemInserted(position);
    }

    public void removeUser(UnilinkUser userToBeRemoved) {
        int index = mDataset.indexOf(userToBeRemoved);
        mDataset.remove(userToBeRemoved);
        notifyItemRemoved(index);
    }

    public void clearData(){
        this.mDataset.clear();
        this.notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.profile_row_cardview);
        }

        public TextView getText() {
            return (TextView) cardView.getChildAt(1);
        }

    }
}
