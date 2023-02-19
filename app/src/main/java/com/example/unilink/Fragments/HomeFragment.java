package com.example.unilink.Fragments;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.unilink.Models.BluetoothButton;
import com.example.unilink.R;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int DATASET_COUNT = 10;

    private RecyclerView mRecyclerView;
    private ProfileRowAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // TODO: Replace mDataset below with the meaningful data taken using Bluetooth
    private String[] mDataset;

    private PulsatorLayout mPulsator;

    private BluetoothButton mBtBtn;

    private BluetoothAdapter btAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null){
            Toast.makeText(getContext(), "Bluetooth is not supported on this devices", Toast.LENGTH_SHORT).show();
            return v;
        }
        // Initiating a pulsator
        mPulsator = v.findViewById(R.id.pulsator);

        // Setting up the Bluetooth Button
        mBtBtn = (BluetoothButton) v.findViewById(R.id.bluetoothbtn);
        // Checking if Bluetooth is enabled
        if (btAdapter.isEnabled())
            mBtBtn.setConnected();
        else
            mBtBtn.setOff();
        mBtBtn.setOnClickListener(view -> {    
            // If off, simply Enable Bluetooth and Receiver does the rest
            if (mBtBtn.isOff())
                EnableBt();
            else if (mBtBtn.isConnected()){
                mBtBtn.setDiscovering();
                mPulsator.start();
            }
            else if (mBtBtn.isDiscovering()) {
                mBtBtn.setConnected();
                mPulsator.stop();
            }
        });

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) v.findViewById(R.id.home_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

        mAdapter = new ProfileRowAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(mBtUpdateReceiver, filter);

        return v;
    }

    public BroadcastReceiver mBtUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                if (mBtBtn == null)
                    return;
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                    mBtBtn.setConnected();
                } else if (state == BluetoothAdapter.STATE_OFF) {
                    mPulsator.stop();
                    mBtBtn.setOff();
                }
            }
        }
    };

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
//        getActivity().unregisterReceiver(mBtUpdateReceiver);
    }

    private void initDataset() {
        mDataset = new String[DATASET_COUNT];
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    private void EnableBt() {
        if (!btAdapter.isEnabled()){
            Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(btIntent);
        }
    }
}