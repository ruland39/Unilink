package com.example.unilink.Dialogs;

import androidx.fragment.app.DialogFragment;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.os.Bundle;

public class BluetoothHomeScreenDialog extends DialogFragment {

    // Interface to be used from the host
    public interface BtHomeScreenDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    BtHomeScreenDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (BtHomeScreenDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement BtHomeScreenDialog");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Unilink requires Bluetooth Permissions to function. Would you like Unilink to connect you with like-minded people around you?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    listener.onDialogPositiveClick(BluetoothHomeScreenDialog.this);
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    listener.onDialogNegativeClick(BluetoothHomeScreenDialog.this);
                }
            })
            .setTitle("Allow Bluetooth");

        return builder.create();
    }

}