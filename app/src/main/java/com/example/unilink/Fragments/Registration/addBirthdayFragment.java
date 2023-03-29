package com.example.unilink.Fragments.Registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.unilink.Models.UnilinkAccount;
import com.example.unilink.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addBirthdayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addBirthdayFragment extends Fragment {

    private static final String TAG = "AddBirthdayFragment";
    private UnilinkAccount uAcc;
    public addBirthdayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment addBirthdayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addBirthdayFragment newInstance(UnilinkAccount uAcc) {
        addBirthdayFragment fragment = new addBirthdayFragment();
        Bundle args = new Bundle();
        args.putParcelable("Account", uAcc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            uAcc = getArguments().getParcelable("Account");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_birthday, container, false);
        DatePicker dp = view.findViewById(R.id.datepicker);
        dp.setMaxDate(new Date().getTime());
        // I think we need to change it to a text view and use a material date picker
        return view;
    }

//    public void pickDate(){
//        MaterialDatePicker.Builder.datePicker().setTitleText("Select date of birth").build().show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
//    }

    // Set Birthday to Database

}