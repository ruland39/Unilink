package com.example.unilink.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilink.Activities.OthersProfileActivity;
import com.example.unilink.Models.Interests.Interest;
import com.example.unilink.Models.UnilinkAccount;
import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;
import com.example.unilink.Services.UserService;
import com.example.unilink.UnilinkApplication;
import com.example.unilink.othersProfileActivity;
import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileRowAdapter extends RecyclerView.Adapter<ProfileRowAdapter.ViewHolder> {
    private final ArrayList<Map.Entry<UnilinkAccount, UnilinkUser>> mDataset;
    private static final String FriendRequestTemplateId = "9540d0b1-ab93-44c5-9140-7fdf11d9e5e6";
    Animation handWaveAnimation;
    private final UnilinkAccount currentUAcc;
    public ProfileRowAdapter(UnilinkAccount uAcc){
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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (mDataset.isEmpty())
            holder.getText().setText("A Unilink User");
        else{
            Map.Entry<UnilinkAccount, UnilinkUser> target = mDataset.get(position);
            UnilinkAccount targetuAcc = target.getKey();
            UnilinkUser targetuUser = target.getValue();
            holder.getText().setText(targetuAcc.getFullName());
            holder.setUserInfo(targetuUser);
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
            holder.getImgBtn().setOnClickListener(view->{
                Toast.makeText(view.getContext(), "Profile Clicked!", Toast.LENGTH_SHORT).show();
                Log.d("RowAdapter", "Clicked on " + targetuUser.getUserID());
                Intent intent = new Intent(view.getContext(), OthersProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction("RETRIEVE_WAVER_PROFILE");
                intent.putExtra("SENDER_USERID", targetuUser.getUserID());
                intent.putExtra("RECEIVER_USERID", currentUAcc.getUid());
                view.getContext().startActivity(intent);
            });
        }
    }

    private void setTargetTag(OkHttpClient client, UnilinkAccount uAcc) throws JSONException, IOException {
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

    private void postNotification(OkHttpClient client, UnilinkAccount uAcc) throws JSONException, IOException {
        // Create JSON Body
        JSONObject notifJSON = new JSONObject();

        JSONArray target_uids = new JSONArray();
        target_uids.put(uAcc.getUid());
        notifJSON.accumulate("app_id", UnilinkApplication.getOnesignalAppId());
        notifJSON.accumulate("include_external_user_ids", target_uids);
        notifJSON.accumulate("template_id", FriendRequestTemplateId);
        JSONObject dataJSON = new JSONObject();
        dataJSON.put("cuid", currentUAcc.getUid());
        dataJSON.put("tuid", uAcc.getUid());
        notifJSON.accumulate("data", dataJSON);
        Log.d("RowAdapter", notifJSON.toString());
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

    public void addUser(UnilinkAccount accToBeAdded, UnilinkUser userToBeAdded, int position) {
        this.mDataset.add(position, new AbstractMap.SimpleEntry<>(accToBeAdded, userToBeAdded));
        this.notifyItemInserted(position);
    }

    public void removeUser(UnilinkAccount accToBeRemoved, UnilinkUser userToBeRemoved){
        Map.Entry<UnilinkAccount, UnilinkUser> target = new AbstractMap.SimpleEntry<>(accToBeRemoved, userToBeRemoved);
        int index = mDataset.indexOf(target);
        mDataset.remove(target);
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
        private final ImageButton profilePictureButton;
        private final ImageView profilePicture;
        private final LinearLayoutCompat interestRow;
        private UserService userService;
        private UnilinkUser userInfo;
        private UnilinkAccount userAccount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.profile_row_cardview);
            waveBtn = (ImageButton) itemView.findViewById(R.id.waveorconnectbtn);
            username = (TextView) itemView.findViewById(R.id.defaultusername);
            profilePictureButton = (ImageButton) itemView.findViewById(R.id.profilepicbutton);

//            profilePictureButton.setOnClickListener(v->{
//                Toast.makeText(v.getContext(), "Profile Clicked!", Toast.LENGTH_SHORT).show();
//                Log.d("RowAdapter", "Clicked on " + userInfo.getUserID());
//                Intent intent = new Intent(v.getContext(), OthersProfileActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("SENDER_USERID", userInfo.getUserID());
//                intent.putExtra("RECEIVER_USERID", userAccount.getUid());
//                v.getContext().startActivity(intent);
//            });

            profilePicture = (ImageView) itemView.findViewById(R.id.profilepicimg);
            interestRow = (LinearLayoutCompat) itemView.findViewById(R.id.interestlinearlayout);

            userService = new UserService();
        }

        public TextView getText() {
            return (TextView) username;
        }
        public ImageButton getWaveBtn(){return (ImageButton) waveBtn;}
        public ImageButton getImgBtn() {return (ImageButton) profilePictureButton;}
        public void setAccountUser (UnilinkAccount targetuAcc, UnilinkUser targetuUser){
            userAccount = targetuAcc;
            userInfo = targetuUser;
        }

        public void setUserInfo(UnilinkUser targetuUser) {
            userService.setImage2View(itemView.getContext(), profilePicture, targetuUser.getPfpURL());

            Interest[] interests = new Interest[targetuUser.getTop3HighestInterest().size()];
            targetuUser.getTop3HighestInterest().toArray(interests);
            for (int i = 0; i < interestRow.getChildCount(); i++){
                CardView cardView = (CardView) interestRow.getChildAt(i);
                TextView textView = (TextView) cardView.getChildAt(0);
                textView.setText(interests[i].getName());
            }
        }
    }
}
