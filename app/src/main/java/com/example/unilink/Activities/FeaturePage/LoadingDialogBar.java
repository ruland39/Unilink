package com.example.unilink.Activities.FeaturePage;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.example.unilink.R;

public class LoadingDialogBar {
    Context context;
    Dialog dialog;

    public LoadingDialogBar(Context context) {
        this.context = context;
    }

    public void showDialog(String title){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView loadingTextView = dialog.findViewById(R.id.loading_textview);
        loadingTextView.setText(title);
        dialog.create();
        dialog.show();

    }

    public void hideDialog(){
        dialog.dismiss();
    }

}
