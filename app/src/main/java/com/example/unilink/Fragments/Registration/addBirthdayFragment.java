package com.example.unilink.Fragments.Registration;


import static android.os.Build.VERSION_CODES.N;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.unilink.Models.UnilinkAccount;
import com.example.unilink.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addBirthdayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addBirthdayFragment extends Fragment {

    private static final String TAG = "AddBirthdayFragment";
    private UnilinkAccount uAcc;
    ProfileSetupListener listener;
    public addBirthdayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment addBirthdayFragment.
     */
    public static addBirthdayFragment newInstance(UnilinkAccount uAcc) {
        addBirthdayFragment fragment = new addBirthdayFragment();
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
            uAcc = getArguments().getParcelable("Account");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_birthday, container, false);
        EditText dateText = view.findViewById(R.id.birthdayedittext);
        dateText.setOnClickListener(v->{
            CalendarConstraints.Builder calConst = new CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointBackward.now());

            MaterialDatePicker dp = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Choose your birthday!")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setCalendarConstraints(calConst.build())
                    .build();

            dp.addOnPositiveButtonClickListener(selection -> {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                cal.setTimeInMillis((Long) selection);
                dateText.setText(cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR));
                listener.AddedBirthdate(cal.getTime());
            });
            dp.show(getParentFragmentManager(),"BIRTHDAY_PICKER");

        });
        return view;
    }
}