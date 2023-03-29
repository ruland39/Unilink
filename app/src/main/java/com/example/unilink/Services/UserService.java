package com.example.unilink.Services;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.unilink.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;

@GlideModule
public class UserService extends AppGlideModule {
    private static String TAG = "UserService";
    private final FirebaseAuth mAuth;
    private final FirebaseStorage storage;

    public UserService() {
        storage = FirebaseStorage.getInstance("gs://unilink-d6206.appspot.com");
        mAuth = FirebaseAuth.getInstance();
    }

    public void setImage2View(Context ctx, ImageView imageView, String imageName, ImageType type){
        StorageReference imageRef = storage.getReference().getRoot().child(type.toString()).child(imageName);
        Glide.with(ctx)
                .load(imageRef)
                .placeholder(R.drawable.defaultprofilepicturesmall)
                .into(imageView);
    }

    public void setImage2View(Context ctx, ImageView imageView, String url){
        StorageReference imageRef = storage.getReference(url);
        Glide.with(ctx)
                .load(imageRef)
                .placeholder(R.drawable.defaultprofilepicturesmall)
                .into(imageView);
    }

    public void uploadImage(Uri localImageFile,
                            String remoteFileName,
                            ImageType type,
                            UploadCallback callback) {
        StorageReference folderReference = storage.getReference().getRoot().child(type.toString());
        StorageReference newImageReference = folderReference.child(remoteFileName);
        UploadTask uploadTask = newImageReference.putFile(localImageFile);
        // Setting a listener to ensure the uploading of task
        uploadTask.addOnFailureListener(exception->{
            // Unsuccessful upload
            Log.e(TAG, "Unsuccessful image upload for " + type);
            throw new RuntimeException("Error Uploading Image");
        });
        uploadTask.addOnSuccessListener(taskSnapshot -> {
           String uploadedURL = taskSnapshot.getMetadata().getReference().getPath();
           callback.onCallback(uploadedURL);
        });
    }

    public interface UploadCallback {
        void onCallback(String returnedImageURL);
    }

    public void getDrawableFromImage(Context ctx, String imageURL){
        Glide.with(ctx)
                .load(imageURL)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public enum ImageType {
        ProfilePicture("profile-pictures"), ProfileBanner("profile-banners");
        private final String folderName;
        private ImageType(String folderName){
            this.folderName = folderName;
        }
        @NonNull
        @Override
        public String toString(){
            return folderName;
        }
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        // Register FirebaseImageLoader to handle StorageReference
        registry.append(StorageReference.class, InputStream.class,
                new FirebaseImageLoader.Factory());
    }
}
