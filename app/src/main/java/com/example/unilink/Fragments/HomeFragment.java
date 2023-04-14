package com.example.unilink.Fragments;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.unilink.Models.BluetoothButton;
import com.example.unilink.Models.UnilinkAccount;
import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;
import com.example.unilink.Services.AccountService;
import com.example.unilink.Services.UserService;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private AccountService accountService;
    private UserService userService;
    private ProfileRowAdapter mAdapter;
    private BluetoothButton mBtBtn;
    private ShimmerFrameLayout shimmerFrameLayout;
    private PulsatorLayout mPulsator;
    private RecyclerView mRecyclerView;

    private final Region wildcardRegion = new Region("wildcardRegion",
            null,null,null);
    private BeaconManager beaconManager = null;
    private static Map<String, Map.Entry<UnilinkAccount, UnilinkUser>> usersInRange = new HashMap<>();
    private static final String TAG = "HomeFragment";
    private View _rootView;

    private UnilinkAccount uAcc;
    private UnilinkUser uUser;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(UnilinkAccount uAcc, UnilinkUser uUser) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putParcelable("Account", uAcc);
        args.putParcelable("User", uUser);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountService = new AccountService();
        userService = new UserService();

        beaconManager = BeaconManager.getInstanceForApplication(requireContext());
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(getString(R.string.beaconlayout)));
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
                        accountService.getAccountByUId(uid.toString(), foundAcc -> {
                            if (foundAcc == null)
                                return;
                            userService.getUserByUid(uid.toString(), foundUser -> {
                                if (foundUser == null)
                                    return;
                                usersInRange.put(uid.toString(), new AbstractMap.SimpleEntry<>(foundAcc, foundUser));
                                if (isAdded()) { // check if fragment is added to an activity
                                    requireActivity().runOnUiThread(()->{
                                        disableShimmer();
                                        mAdapter.addUser(foundAcc,foundUser, 0);
                                    });
                                }
                            });
                        });
                    }
                    else {
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
                    if(isAdded()){
                        requireActivity().runOnUiThread(()->{
                            Map.Entry<UnilinkAccount, UnilinkUser> user = usersInRange.get(userId);
                            mAdapter.removeUser(user.getKey(),user.getValue());
                        });
                        usersInRange.remove(userId);
                        Log.d(TAG, "Unilink User no longer in range! Removed Address: " + userId);
                    }
                }
                Log.d(TAG,"End of ranging cycle");
            }
        });
        accountService = new AccountService();
    }

    @Override
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Toast.makeText(getContext(), "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            return _rootView;
        }
        if (getArguments() != null) {
            uAcc = getArguments().getParcelable("Account");
            uUser = getArguments().getParcelable("User");
        }
        else
            Log.d(TAG, "No Arguments sent to Home Fragment!");

        if (_rootView == null) {
            _rootView = inflater.inflate(R.layout.fragment_home, container, false);

            // Shimmer
            shimmerFrameLayout = _rootView.findViewById(R.id.shimmer_layout);
            shimmerFrameLayout.setShimmer(new Shimmer.AlphaHighlightBuilder().setAutoStart(false).build());

            // Initiating a pulsator
            mPulsator = _rootView.findViewById(R.id.pulsator);

            // Setting up the Bluetooth Button
            mBtBtn = _rootView.findViewById(R.id.bluetoothbtn);
            // Checking if Bluetooth is enabled
            if (btAdapter.isEnabled())
                mBtBtn.setConnected();
            else
                mBtBtn.setOff();
            mBtBtn.setOnClickListener(view -> {
                // If off, simply Enable Bluetooth and Receiver does the rest
                if (mBtBtn.isOff())
                    EnableBt(btAdapter);
                else if (mBtBtn.isConnected()){
                    mBtBtn.setDiscovering();
                    mPulsator.start();
                    enableShimmer();
                    // Start the monitoring activity while it looks for a person
                    startRange();
                }
                else if (mBtBtn.isDiscovering()) {
                    mBtBtn.setConnected();
                    mPulsator.stop();
                    disableShimmer();
                    // Stop Ranging
                    stopRange();
                }
            });

            // Setting the profile picture in home fragment
            ImageView profilePic = _rootView.findViewById(R.id.pfpholder);
            userService.setImage2View(requireContext(), profilePic, uUser.getPfpURL());

            // Calling the RecyclerView
            mRecyclerView = _rootView.findViewById(R.id.home_recyclerview);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.scrollToPosition(0);

            mAdapter = new ProfileRowAdapter(uAcc);
            mRecyclerView.setAdapter(mAdapter);
        } else {

        }

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(mBtUpdateReceiver, filter);

        return _rootView;
    }

    private void startRange() {
        // Resetting current found users
        usersInRange.clear();
        mAdapter.clearData();

        beaconManager.setForegroundScanPeriod(5000l);
        beaconManager.startRangingBeacons(wildcardRegion);
    }

    private void stopRange(){
        beaconManager.stopRangingBeacons(wildcardRegion);
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
    @Override
    public void onDestroyView() {
        if (_rootView.getParent() != null) {
            ((ViewGroup)_rootView.getParent()).removeView(_rootView);
        }
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        stopRange();
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
    private void EnableBt(BluetoothAdapter btAdapter) {
        if (!btAdapter.isEnabled()) {
            Intent btIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(btIntent);
        }
    }
    //endregion


}