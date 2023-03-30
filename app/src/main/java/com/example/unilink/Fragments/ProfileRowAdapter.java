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

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Handler;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;

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
            UnilinkUser uAcc = mDataset.get(position);
            holder.getText().setText(uAcc.getFullName());
            holder.getWaveBtn().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject notifJSON = new JSONObject();

                    try {
                        notifJSON.accumulate("include_external_user_ids", new String[]{uAcc.getUid()});
                        notifJSON.accumulate("template_id", "9540d0b1-ab93-44c5-9140-7fdf11d9e5e6");
                        OneSignal.sendTag("username", uAcc.getFullName());
                        OneSignal.setExternalUserId(uAcc.getUid());

                        new Thread(() -> {
                            OkHttpClient client = new OkHttpClient();

                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create( notifJSON.toString(),mediaType);
                            Request request = new Request.Builder()
                                    .url("https://onesignal.com/api/v1/notifications")
                                    .post(body)
                                    .addHeader("accept", "application/json")
                                    .addHeader("Authorization", "Basic NjU2Yzk0NTQtNjI2OC00NzQ5LTk2OTEtMDhlMTk5MmM0ZDNi")
                                    .addHeader("content-type", "application/json")
                                    .build();

                            try {
                                Response response = client.newCall(request).execute();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }).run();

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
