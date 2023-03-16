package com.example.unilink;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addProfileBannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addProfileBannerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int GALLERY_REQUEST_CODE = 1000;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addProfileBannerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addProfileBannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addProfileBannerFragment newInstance(String param1, String param2) {
        addProfileBannerFragment fragment = new addProfileBannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_profile_banner, container, false);

        ImageButton addProfileBanner = view.findViewById(R.id.addprofilepicturebutton);

        addProfileBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfileBanner();
            }
        });

        return view;
    }

    // Add Profile Banner
    public void addProfileBanner(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);

    }


    // Set Profile Banner
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(resultCode == RESULT_OK){
//            if(requestCode == GALLERY_REQUEST_CODE){
//                temp.setImageURI(data.getData());
//            }
//        }
//    }

    // Set Profile Banner to Database

}