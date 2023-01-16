package com.example.unilink.Activities.FeaturePage;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.unilink.Activities.LoginorregisterActivity;
import com.example.unilink.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FeaturePageActivity extends AppCompatActivity {
	private ViewPager2 viewPager;
	private Button getStartedButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feature_page);

		viewPager = findViewById(R.id.fpViewPager);
		FpPagerAdapter adapter = new FpPagerAdapter(this);
		viewPager.setAdapter(adapter);
		viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
			// triggered when you select a new page
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				if (position == 2)
					enableStartBtn();
				else
					disableStartBtn();
			}
		});

		getStartedButton = findViewById(R.id.getstartedbutton);
		getStartedButton.setOnClickListener(v -> OpenLoRPage());

		disableStartBtn();
		TabLayout tab = findViewById(R.id.fpDots);
		new TabLayoutMediator(tab, viewPager, ((tab1, position) -> {})).attach();
		// Copy pasted code below
		LinearLayout tabStrip = ((LinearLayout)tab.getChildAt(0));
		for(int i = 0; i < tabStrip.getChildCount(); i++) {
			tabStrip.getChildAt(i).setOnTouchListener((v, event) -> true);
		}
	}
	@Override
	public void onBackPressed() {
		if(viewPager.getCurrentItem() == 0)
			super.onBackPressed();
		else
			viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
	}

	void OpenLoRPage(){
		Intent intent = new Intent(this, LoginorregisterActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // clean all previous activities
		startActivity(intent);
		finish();
	}

	void enableStartBtn() {
		getStartedButton.setAlpha(0f);
		getStartedButton.setVisibility(View.VISIBLE);
		getStartedButton.animate()
				.alpha(1)
				.setDuration(400)
				.setListener(null);
		getStartedButton.setEnabled(true);
	}

	void disableStartBtn() {
		getStartedButton.setEnabled(false);
		getStartedButton.setAlpha(1);
		getStartedButton.animate()
				.alpha(0f)
				.setDuration(200)
				.setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						getStartedButton.setVisibility(View.INVISIBLE);
					}
				});
	}

}

