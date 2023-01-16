package com.example.unilink.Activities.FeaturePage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.unilink.Activities.LoginorregisterActivity;
import com.example.unilink.R;

public class FpPagerAdapter extends RecyclerView.Adapter<FpPagerAdapter.ViewHolder> {
	private int[] images = {R.drawable.connected_amico, R.drawable.conversation_rafiki, R.drawable.connected_cuate};
	private int[] texts = {R.string.discover, R.string.chat, R.string.connect};
	private Context _ctx;
	public FpPagerAdapter(Context ctx) {
		_ctx = ctx;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(_ctx).inflate(R.layout.fragment_feature_page, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.images.setImageResource(images[position]);
		holder.text.setText(texts[position]);
	}

	@Override
	public int getItemCount() {
		return images.length;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		TextView text;
		ImageView images;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			images = itemView.findViewById(R.id.FpImage);
			text = itemView.findViewById(R.id.FpTitleText);
		}
	}

}
