package com.example.unilink.Fragments.Registration;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.unilink.Activities.FeaturePage.LoadingDialogBar;
import com.example.unilink.Models.UnilinkAccount;
import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;
import com.example.unilink.Services.UserService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addProfilePictureFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addProfilePictureFragment extends Fragment  {

    private static final String TAG = "AddProfilePictureFragment";
    private UnilinkAccount uAcc;
    private UnilinkUser uUser;
    ImageView pfp;
    private UserService userService;
    ProfileSetupListener listener;

    public addProfilePictureFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AddProfilePictureFragment.
     */
    public static addProfilePictureFragment newInstance(UnilinkAccount uAcc) {
        addProfilePictureFragment fragment = new addProfilePictureFragment();
        Bundle args = new Bundle();
        args.putParcelable("Account", uAcc);
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
            uAcc = (UnilinkAccount) getArguments().getParcelable("Account");
            userService = new UserService();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_profile_picture, container, false);
        pfp = view.findViewById(R.id.addprofilepictureimg);
        ImageButton addPFPBtn = view.findViewById(R.id.addprofilepicturebutton);
        addPFPBtn.setOnClickListener(v -> {
            chooseImageActivity.launch("image/*");
        });

        return view;
    }

    ActivityResultLauncher<String> chooseImageActivity = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri == null) return;
                LoadingDialogBar loadingDialogBar = new LoadingDialogBar(requireContext());
                loadingDialogBar.showDialog("Uploading");
                userService.uploadImage(uri,
                        uAcc.getUid() + uri.getLastPathSegment(),
                        UserService.ImageType.ProfilePicture,
                        uploadedUrl -> {
                            loadingDialogBar.hideDialog();
                            userService.setImage2View(requireContext(), pfp, uploadedUrl);
                            listener.AddedProfileImage(uploadedUrl);
                        });
            }
    );


}
