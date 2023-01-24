package com.example.unilink.Fragments;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.unilink.Models.BluetoothButton;
import com.example.unilink.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private BluetoothButton BtBtn;
    private BluetoothAdapter BtAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Set up the bluetooth button, depending on enabled or disabled bluetooth
        BtBtn = view.findViewById(R.id.bluetoothbtn);
        Context ctx = view.getContext();
        BluetoothManager BtMgr = (BluetoothManager) ctx.getSystemService(ctx.BLUETOOTH_SERVICE);
        BtAdapter = BtMgr.getAdapter();
        // TODO:A check for Monitoring activity needs to be added here
        // Setting the button click
        if (BtAdapter.isEnabled())
            BtBtn.setConnected();
        else
            BtBtn.setOff();

        BtBtn.setOnClickListener(view1 -> {
            if (BtBtn.isConnected() && BtAdapter.isEnabled()) enableDiscover();
            else if (BtBtn.isDiscovering()) BtBtn.setConnected();
            else if (BtBtn.isOff() && !BtAdapter.isEnabled()) enableBluetooth();
        });

        this.requireActivity().registerReceiver(DisabledBtReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        return view;
    }

    private void enableBluetooth() {
        Intent startBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivity(startBt);
        BtBtn.setConnected();
    }

    @Deprecated
    private void disableBluetooth(Context ctx) {
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.requireActivity(), new String[]{
                    Manifest.permission.BLUETOOTH_CONNECT
            },1);
        }
        System.out.println("lol");
        BtAdapter.disable();
        BtBtn.setOff();
    }

    private void enableDiscover() {
        BtBtn.setDiscovering();
    }

    private final BroadcastReceiver DisabledBtReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                switch (btAdapter.getState()) {
                    case BluetoothAdapter.STATE_TURNING_OFF:
                    case BluetoothAdapter.STATE_OFF:
                        BtBtn.setOff(); break;
                    case BluetoothAdapter.STATE_ON:
                        BtBtn.setConnected(); break;
                }

            }
        }
    };
}