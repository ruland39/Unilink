package com.example.unilink.Fragments.Registration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.unilink.Activities.FeaturePage.LoadingDialogBar;
import com.example.unilink.Models.UnilinkAccount;
import com.example.unilink.R;
import com.example.unilink.Services.UserService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addProfileBannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addProfileBannerFragment extends Fragment {
    private static final int GALLERY_REQUEST_CODE = 1000;
    private static final String TAG = "AddProfileBannerFragment";
    private UnilinkAccount uAcc;

    ImageView banner;
    private UserService userService;
    ProfileSetupListener listener;

    public addProfileBannerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment addProfileBannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addProfileBannerFragment newInstance(UnilinkAccount uAcc) {
        addProfileBannerFragment fragment = new addProfileBannerFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_profile_banner, container, false);
        ImageButton addBtn = view.findViewById(R.id.addprofilebannerbutton);
        banner = view.findViewById(R.id.addprofilebannerimg);
        addBtn.setOnClickListener(v -> {
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
                        UserService.ImageType.ProfileBanner,
                        uploadedUrl -> {
                            loadingDialogBar.hideDialog();
                            userService.setImage2View(requireContext(), banner, uploadedUrl);
                            listener.AddedProfileBanner(uploadedUrl);
                        });
            }
    );
}