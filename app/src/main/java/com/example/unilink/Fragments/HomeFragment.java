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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.unilink.Activities.BLE.BeaconWorker;
import com.example.unilink.Models.BluetoothButton;
import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;
import com.example.unilink.Services.UserService;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.checkerframework.checker.units.qual.A;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment{
    // Checking using Map; String holds user id found
    private static Map<String, UnilinkUser> usersInRange = new HashMap<>();
    private UserService userService;
    private RecyclerView mRecyclerView;
    private ProfileRowAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private PulsatorLayout mPulsator;

    private BluetoothButton mBtBtn;

    private BluetoothAdapter btAdapter;

    private ShimmerFrameLayout shimmerFrameLayout;

    private final Region wildcardRegion = new Region("wildcardRegion",
            null,null,null);
    private BeaconManager beaconManager = null;

    private static final String TAG = "HomeFragment";

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
        beaconManager = BeaconManager.getInstanceForApplication(getContext());
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(getString(R.string.beaconlayout)));
        userService = new UserService();
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
            else if (mBtBtn.isConnected()){
                mBtBtn.setDiscovering();
                mPulsator.start();
                enableShimmer();
                // Start the monitoring activity while it looks for a person
                startMonitor();
            }
            else if (mBtBtn.isDiscovering()) {
                mBtBtn.setConnected();
                mPulsator.stop();
                disableShimmer();
                // Stop Ranging
                beaconManager.stopRangingBeacons(wildcardRegion);
            }
        });

        // Calling the RecyclerView
        mRecyclerView = (RecyclerView) v.findViewById(R.id.home_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);

        mAdapter = new ProfileRowAdapter();
        mRecyclerView.setAdapter(mAdapter);

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(mBtUpdateReceiver, filter);

        return v;
    }

    private void startMonitor() {
        // Resetting current found users
        usersInRange.clear();
        mAdapter.clearData();

        beaconManager.setForegroundScanPeriod(5000l);
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                Log.d(TAG, "New Range Cycle; Beacons found: " + beacons.size());
                for (Beacon beacon : beacons) {
                    byte[] bytes = beacon.getId2().toByteArray();
                    ByteBuffer buffer = ByteBuffer.wrap(bytes);
                    long high = buffer.getLong();
                    long low = buffer.getLong();
                    UUID uid = new UUID(high, low);

                    if (!usersInRange.containsKey(uid.toString())){
                        // User is not in the known list (new)
                        Log.d(TAG, "New unilink user found: " + uid);
                        userService.getUserInfoByUId(uid.toString(), foundUser -> {
                            if (foundUser == null)
                                return;
                            usersInRange.put(uid.toString(), foundUser);
                            requireActivity().runOnUiThread(()-> {
                                disableShimmer();
                                mAdapter.addUser(foundUser, 0);
                            });
                        });
                    } else {
                        Log.d(TAG, "Existing unilink user found: " + uid);
                    }
                }

                List<String> usersToRemove = new ArrayList<>();
                for (String userId : usersInRange.keySet()) {
                    boolean found = false;
                    for (Beacon beac : beacons) {
                        byte[] bytes = beac.getId2().toByteArray();
                        ByteBuffer buffer = ByteBuffer.wrap(bytes);
                        long high = buffer.getLong();
                        long low = buffer.getLong();
                        UUID uid = new UUID(high, low);
                        if (uid.toString().equals(userId)){
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        usersToRemove.add(userId);
                    }
                }

                for (String userId : usersToRemove) {
                    requireActivity().runOnUiThread(()->mAdapter.removeUser(usersInRange.get(userId))
                    );
                    usersInRange.remove(userId);
                    Log.d(TAG, "Unilink User no longer in range! Removed Address: " + userId);
                }
                Log.d(TAG,"End of ranging cycle");
            }
        });
        beaconManager.startRangingBeacons(wildcardRegion);
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

    public void onStop() {
        super.onStop();
    }

    private void enableShimmer() {
        mRecyclerView.setVisibility(View.GONE);
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
    }

    private void disableShimmer() {
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private void EnableBt() {
        if (!btAdapter.isEnabled()) {
            Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(btIntent);
        }
    }
    //endregion
}