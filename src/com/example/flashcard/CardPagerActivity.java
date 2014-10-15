package com.example.flashcard;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class CardPagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private String mFileName;
	private FlashCardDatabase mCardDatabase;
	private static final String TAG = "CARDPAGER"; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);
		
		mFileName = (String) getIntent()
				.getSerializableExtra(CardsChooserListFragment.EXTRA_FILENAME);
		Log.d(TAG, "filename is: " + mFileName);
		
		mCardDatabase = getDatabase(mFileName);
		
		FragmentManager fm = getSupportFragmentManager();
		
		Log.d(TAG, "getSupportFragmentManager ok");
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			
			@Override
			public int getCount() {
				return mCardDatabase.getSize();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				FlashCard card = mCardDatabase.get(arg0);
				return ShowCardsFragment.newInstance(card.getId(), mCardDatabase);
			}
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		UUID cardID = (UUID)getIntent().getSerializableExtra(ShowCardsFragment.EXTRA_CARD_ID);
		for(int i = 0; i < mCardDatabase.getSize(); i++) {
			if (mCardDatabase.get(i).getId().equals(cardID)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}

	}
	
	private FlashCardDatabase getDatabase(String fileName) {
		FlashCardDatabase database = null;
		
		FileInputStream fis;
		ObjectInputStream ois;
		
		Log.d(TAG, "getting database");
		
		try {
			fis = openFileInput(fileName);
			ois = new ObjectInputStream(fis);
			Log.d(TAG, "getting streams");
			
			database = (FlashCardDatabase) ois.readObject();
			
			Log.d(TAG, "database ok");
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return database;
	}
}
