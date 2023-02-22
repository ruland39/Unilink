package com.example.unilink.Fragments;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
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

import androidx.annotation.RequiresPermission;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.unilink.Activities.BLE.MonitoringActivity;
import com.example.unilink.Models.BluetoothButton;
import com.example.unilink.R;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements MonitorNotifier {

    private static final int DATASET_COUNT = 10;

    private RecyclerView mRecyclerView;
    private ProfileRowAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // TODO: Replace mDataset below with the meaningful data taken using Bluetooth
    private String[] mDataset;

    private PulsatorLayout mPulsator;

    private BluetoothButton mBtBtn;

    private BluetoothAdapter btAdapter;

    private ShimmerFrameLayout shimmerFrameLayout;

    private final Region wildcardRegion = new Region("wildcardRegion",
            Identifier.parse("2f234454-cf6d-4a0f-adf2-f4911ba9ffa5"), Identifier.parse("1"),Identifier.parse("1"));

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
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Toast.makeText(getContext(), "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            return v;
        }

        // Shimmer
        shimmerFrameLayout = v.findViewById(R.id.shimmer_layout);
        shimmerFrameLayout.setShimmer(new Shimmer.AlphaHighlightBuilder().setAutoStart(false).build());

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
            else if (mBtBtn.isConnected()) {
                mBtBtn.setDiscovering();
                mPulsator.start();
                shimmerFrameLayout.startShimmer();

                // Start the monitoring activity while it looks for a person
                BeaconManager beaconManager = BeaconManager.getInstanceForApplication(getContext());
                startMonitor(beaconManager);
            }
            else if (mBtBtn.isDiscovering()) {
                mBtBtn.setConnected();
                mPulsator.stop();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
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

    private void startMonitor(BeaconManager beaconManager) {
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));

        beaconManager.setDebug(false);
        beaconManager.addMonitorNotifier(this);
        beaconManager.startMonitoring(wildcardRegion);
    }

    //region Bt-Perms
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

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private void EnableBt() {
        if (!btAdapter.isEnabled()) {
            Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(btIntent);
        }
    }
    //endregion
    //region MonitorNotifier overriders
    @Override
    public void didEnterRegion(Region region) {
        // For now, end the search if it finds one person.
        // Stop Monitoring
        System.out.println("YOUR MOTHER I FOUND");
        getActivity().runOnUiThread(() -> {
            Toast.makeText(getActivity(), "Entered a beacon region!", Toast.LENGTH_LONG).show();
            mBtBtn.setConnected();
            mPulsator.stop();
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            // Stop Monitoring
            BeaconManager beaconManager = BeaconManager.getInstanceForApplication(getContext());
            beaconManager.stopMonitoring(wildcardRegion);
        });
    }

    @Override
    public void didExitRegion(Region region) {

    }

    @Override
    public void didDetermineStateForRegion(int state, Region region){}
        //endregion
}