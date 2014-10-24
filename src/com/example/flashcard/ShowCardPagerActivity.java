package com.example.flashcard;

import java.util.Iterator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;

public class ShowCardPagerActivity extends FragmentActivity implements
		ShowCardsButtonBarFragment.OnShowAnswerButtonClickedListener {
	private static final String CURRENT_ITEM = "current_item";
	private static final String ANSWER_VISIBLE = "answer_visible";
	private ViewPager mViewPager;
	private String mFileName;
	private FlashCardDatabase mCardDatabase;
	private ShowCardsFragment mCardFrag;
	private boolean answerVisible = false;
	private static final String TAG = "SHOW";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		setContentView(R.layout.card_pager_fragment);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);

		mFileName = (String) getIntent().getSerializableExtra(
				CardsChooserListFragment.EXTRA_FILENAME);

		mCardDatabase = FlashCardDatabase.getDatabase(getApplicationContext(), mFileName);
		
		FragmentManager fm = getSupportFragmentManager();

		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

			@Override
			public int getCount() {
				return mCardDatabase.getArrayList().size();
			}

			@Override
			public Fragment getItem(int pos) {
				FlashCard card = mCardDatabase.getArrayList().get(pos);
				return ShowCardsFragment.newInstance(card.getId(),
						mCardDatabase);
			}

			@Override
			public void setPrimaryItem(ViewGroup container, int position,
					Object object) {
				if (mCardFrag != object) {
					mCardFrag = (ShowCardsFragment) object;
					if (answerVisible) {
						mCardFrag.showAnswer();
					}
					Log.d(TAG, "setPrimaryItem");
				}
				super.setPrimaryItem(container, position, object);
			}

		});

		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
					}

					@Override
					public void onPageScrollStateChanged(int state) {
						answerVisible = false;
						if (state == ViewPager.SCROLL_STATE_IDLE) {
							final int lastPosition = mViewPager.getAdapter()
									.getCount() - 1;
							if (mViewPager.getCurrentItem() == lastPosition) {
								mViewPager.setCurrentItem(1, false);
							} else if (mViewPager.getCurrentItem() == 0) {
								mViewPager.setCurrentItem(lastPosition - 1,
										false);
							}
						}
						mCardFrag.resetQuestion();
					}
				});
		ShowCardsButtonBarFragment buttonBar = new ShowCardsButtonBarFragment();

		FragmentTransaction transaction = fm.beginTransaction();
		transaction.add(R.id.buttonBarFragmentContainer, buttonBar);
		Log.d(TAG, "fm transactions");
		transaction.commit();
		
		if (savedInstanceState == null) {
//			Iterator<FlashCard> itr = mCardDatabase.getArrayList().iterator();
//			while (itr.hasNext()) {
//				FlashCard card = itr.next();
//				if (card.getQuestion().equals("")) {
//					itr.remove();
//				}
//			}
			
			mCardDatabase.setForCircularScrolling();
			mViewPager.getAdapter().notifyDataSetChanged();
			
			mViewPager.setCurrentItem(1);
			Log.d(TAG, "currentItemNullState: " + mViewPager.getCurrentItem());
		}
		
		if (savedInstanceState != null) {
			mViewPager.setCurrentItem(savedInstanceState.getInt(CURRENT_ITEM));
			Log.d(TAG, "currentItemSavedState: " + mViewPager.getCurrentItem());
			if (savedInstanceState.getBoolean(ANSWER_VISIBLE)) {
				Log.d(TAG, "savedStateAnswerVisible: " + savedInstanceState.getBoolean(ANSWER_VISIBLE));
				answerVisible = true;
			}
		}
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		Log.d(TAG, "onSaveInstanceState");
		Log.d(TAG, "currentItemOnSaveInstance: " + mViewPager.getCurrentItem());
		savedInstanceState.putBoolean(ANSWER_VISIBLE, mCardFrag.answerVisible());
		savedInstanceState.putInt(CURRENT_ITEM, mViewPager.getCurrentItem());
		Log.d(TAG, "answer visibile: " + mCardFrag.answerVisible());
	}


	@Override
	public void onButtonClicked() {
		mCardFrag.showAnswer();
	}
	
	
}
