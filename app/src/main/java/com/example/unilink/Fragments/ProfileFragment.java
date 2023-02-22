package com.example.unilink.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;

import java.sql.SQLOutput;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "hello world";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private UnilinkUser user;
    private static final String user_key = "user";
    private static final int GALLERY_REQUEST_CODE = 1000;

    ImageView profilePicture, profileBanner, temp;



    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Passed in Unilinkuser from the Activity
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(UnilinkUser user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(user_key, user);
        // args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);




        }

        // Bundle bundle = getArguments();
        // if(bundle != null) {
        //     user =  bundle.getParcelable("user");
        //     Log.d()
        // } else
        //     Toast.makeText(getActivity(), "Unable to parce User", Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        ImageButton threedotsbutton = view.findViewById(R.id.threedotsbutton);

        // Images
        profilePicture = view.findViewById(R.id.defaultprofilepicture);
        profileBanner = view.findViewById(R.id.profilebanner);

        // TextView

        threedotsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v,R.menu.profileoptions);
            }
        });


        user = (UnilinkUser) getArguments().getSerializable(user_key);
        if (user == null) {
            Toast.makeText(getActivity(), "Unable to parse User", Toast.LENGTH_SHORT).show();
            return view;
        }
        final TextView fullname = (TextView) view.findViewById(R.id.defaultusername);
        fullname.setText(user.getFullName());

        return view;
    }

    private void showMenu(View v, @MenuRes int menuRes) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        popup.setOnMenuItemClickListener(menuItem -> {

            switch (menuItem.getItemId()){

                case R.id.editprofilepicture:
                    Toast.makeText(getActivity(), "Choose Profile Picture", Toast.LENGTH_SHORT).show();
                    pickImage(profilePicture);

                    return true;

                case R.id.editprofilebanner:
                    Toast.makeText(getActivity(), "Choose Profile Banner (preferably wide image)", Toast.LENGTH_SHORT).show();
                    pickImage(profileBanner);
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

    public ImageView pickImage(ImageView i){
        temp = i;
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
        return temp;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == GALLERY_REQUEST_CODE){
                temp.setImageURI(data.getData());
            }
        }
    }

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