package com.example.unilink.Fragments;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;
import com.example.unilink.UnilinkApplication;
import com.onesignal.OneSignal;

import org.json.JSONArray;
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
    private final ArrayList<UnilinkUser> mDataset;
    private static final String FriendRequestTemplateId = "9540d0b1-ab93-44c5-9140-7fdf11d9e5e6";
    private final UnilinkUser currentUAcc;
    Animation handWaveAnimation;
    public ProfileRowAdapter(UnilinkUser uAcc){
        this.currentUAcc = uAcc;
        mDataset = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_row, parent, false);

        handWaveAnimation = AnimationUtils.loadAnimation(parent.getContext(), R.anim.hand_wave);

        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (mDataset.isEmpty())
            holder.getText().setText("A Unilink User");
        else{
            UnilinkUser targetuAcc = mDataset.get(position);
            holder.getText().setText(targetuAcc.getFullName());
            holder.getWaveBtn().setOnClickListener(v -> {
                holder.getWaveBtn().startAnimation(handWaveAnimation);
                UnilinkApplication.getExecutor().execute(()->{
                    OkHttpClient client = new OkHttpClient();
                    try {
                        OneSignal.setExternalUserId(currentUAcc.getUid());
                        setTargetTag(client, targetuAcc);
                        postNotification(client, targetuAcc);
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            });
        }
    }

    private void setTargetTag(OkHttpClient client, UnilinkUser uAcc) throws JSONException, IOException {
        JSONObject editTagJSON = new JSONObject();
        editTagJSON.accumulate("tags", new JSONObject("{'sender_username':'"+currentUAcc.getFullName()+"'}"));

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(editTagJSON.toString(),mediaType);
        Request request = new Request.Builder()
                .url("https://onesignal.com/api/v1/apps/"+UnilinkApplication.getOnesignalAppId()+"/users/"+uAcc.getUid())
                .put(body)
                .addHeader("accept", "text/plain")
                .addHeader("Content-Type", "application/json")
                .build();

        System.out.println("EditTagRequest: " + request + "\nJSON:" + editTagJSON.toString());
        Response response = client.newCall(request).execute();
        Log.d("RowAdapter", "Got a response! HttpCode: " + response.code());
    }

    private void postNotification(OkHttpClient client, UnilinkUser uAcc) throws JSONException, IOException {
        // Create JSON Body
        JSONObject notifJSON = new JSONObject();

        JSONArray target_uids = new JSONArray();
        target_uids.put(uAcc.getUid());
        notifJSON.accumulate("app_id", UnilinkApplication.getOnesignalAppId());
        notifJSON.accumulate("include_external_user_ids", target_uids);
        notifJSON.accumulate("template_id", FriendRequestTemplateId);
        // Create Http Request
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create( notifJSON.toString(),mediaType);
        Request request = new Request.Builder()
                .url("https://onesignal.com/api/v1/notifications")
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Basic "+UnilinkApplication.getOnesignalApiKey())
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .build();

        // Send request
        Response response = client.newCall(request).execute();
        Log.d("RowAdapter", "Got a response! HttpCode: " + response.code());
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

    @SuppressLint("NotifyDataSetChanged")
    public void clearData(){
        this.mDataset.clear();
        this.notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final CardView cardView;
        private final ImageButton waveBtn;
        private final TextView username;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.profile_row_cardview);
            waveBtn = (ImageButton) itemView.findViewById(R.id.waveorconnectbtn);
            username = (TextView) itemView.findViewById(R.id.defaultusername);
        }

        public TextView getText() {
            return (TextView) username;
        }
        public ImageButton getWaveBtn(){return (ImageButton) waveBtn;}

    }
}
