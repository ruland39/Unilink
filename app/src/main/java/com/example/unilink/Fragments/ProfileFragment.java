package com.example.unilink.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unilink.Models.UnilinkAccount;
import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;
import com.example.unilink.Services.UserService;

import java.net.URI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private UnilinkAccount uAcc;
    private UnilinkUser uUser;
    private UserService userService;
    ImageView profilePicture, profileBanner;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Passed in UnilinkUser from the Activity
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(UnilinkAccount user, UnilinkUser uUser) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable("AuthenticatedUser", user);
        args.putParcelable("UnilinkUser", uUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uAcc = (UnilinkAccount) getArguments().getSerializable("AuthenticatedUser");
            uUser = getArguments().getParcelable("UnilinkUser");
        }
        userService = new UserService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (uAcc == null || uUser == null) {
            Toast.makeText(requireActivity(), "Unable to parse User", Toast.LENGTH_SHORT).show();
            return view;
        }

        ImageButton threedotsbutton = view.findViewById(R.id.threedotsbutton);

        // Images
        profilePicture = view.findViewById(R.id.defaultprofilepicture);
        profileBanner = view.findViewById(R.id.profilebanner);

        userService.setImage2View(requireContext(), profilePicture, uUser.getPfpURL());
        userService.setImage2View(requireContext(), profileBanner, uUser.getPfbURL());

        threedotsbutton.setOnClickListener(v -> showMenu(v, R.menu.profileoptions));

        // TextView
        final TextView fullname = (TextView) view.findViewById(R.id.defaultusername);
        fullname.setText(uAcc.getFullName());
        final TextView aboutSection = view.findViewById(R.id.aboutsection);
        aboutSection.setText(uUser.getBio());
        final TextView connectionNumTV = view.findViewById(R.id.connectionnumber);
        String text = uUser.getConnectedUIDs().size() + " Connections";
        connectionNumTV.setText(text);

        return view;
    }

    private void showMenu(View v, @MenuRes int menuRes) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        popup.setOnMenuItemClickListener(menuItem -> {

            switch (menuItem.getItemId()) {

                case R.id.editprofilepicture:
                    Toast.makeText(getActivity(), "Choose Profile Picture", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.editprofilebanner:
                    Toast.makeText(getActivity(), "Choose Profile Banner (preferably wide image)", Toast.LENGTH_SHORT).show();
                    return true;

                case R.id.editprofiledetails:
                    Toast.makeText(getActivity(), "Profile Details", Toast.LENGTH_SHORT).show();
                    return true;

            }
            // Respond to menu item click.
            return true;
        });
        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu popupMenu) {
                // Respond to popup being dismissed.
            }
        });

        popup.show();
    }


    ActivityResultLauncher<String> chooseImageActivity = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {

                }
            }
    );

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i(TAG, "On Restore Instance State");
    }
}