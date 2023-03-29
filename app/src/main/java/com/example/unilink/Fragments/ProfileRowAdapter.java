package com.example.unilink.Fragments;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (mDataset.isEmpty())
            holder.getText().setText("A Unilink User");
        else{
            holder.getText().setText(mDataset.get(position).getFullName());
            holder.getWaveBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject notifJSON = new JSONObject();
                    try {
                        notifJSON.accumulate("contents", "{'en':'Test Message'}");
                        notifJSON.accumulate("include_external_user_ids", new String[]{mDataset.get(position).getUid()});
                        OneSignal.postNotification(notifJSON, new OneSignal.PostNotificationResponseHandler() {
                                    @Override
                                    public void onSuccess(JSONObject response) {
                                        Log.i("OneSignalExample", "postNotification Success: " + response.toString());
                                    }

                                    @Override
                                    public void onFailure(JSONObject response) {
                                        Log.e("OneSignalExample", "postNotification Failure: " + response.toString());
                                    }
                                });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
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
        private final ImageButton waveBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.profile_row_cardview);
            waveBtn = (ImageButton) itemView.findViewById(R.id.waveorconnectbtn);
        }

        public TextView getText() {
            return (TextView) cardView.getChildAt(1);
        }
        public ImageButton getWaveBtn(){return (ImageButton) waveBtn;}

    }
}
