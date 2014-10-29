package com.example.flashcard;

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
	private static final String DATABASE = "card_database";
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

		mCardDatabase = FlashCardDatabase.getDatabase(getApplicationContext(),
				mFileName);

		FragmentManager fm = getSupportFragmentManager();

		ShowCardsButtonBarFragment buttonBar = new ShowCardsButtonBarFragment();
		CardCounterFragment cardCounter = new CardCounterFragment();

		FragmentTransaction transaction = fm.beginTransaction();
		transaction.add(R.id.cardCountFragmentContainer, cardCounter);
		transaction.add(R.id.buttonBarFragmentContainer, buttonBar);
		transaction.commit();

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
							mCardFrag.resetQuestion();

							updateCounter();
						}
					}
				});

		if (savedInstanceState == null) {
			Log.d(TAG, "nullState");
			mCardDatabase.setForCircularScrolling();
			mViewPager.setCurrentItem(1);
		}

		if (savedInstanceState != null) {
			mViewPager.setCurrentItem(savedInstanceState.getInt(CURRENT_ITEM));
			Log.d(TAG,
					"savedInstanceStateItem#: " + mViewPager.getCurrentItem());
			if (savedInstanceState.getBoolean(ANSWER_VISIBLE)) {
				answerVisible = true;
			}
			mCardDatabase = (FlashCardDatabase) savedInstanceState
					.getSerializable(DATABASE);
		}

		mViewPager.getAdapter().notifyDataSetChanged();
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		Log.d(TAG, "onSaveInstanceState");
		Log.d(TAG, "currentItemOnSaveInstance: " + mViewPager.getCurrentItem());
		savedInstanceState
				.putBoolean(ANSWER_VISIBLE, mCardFrag.answerVisible());
		savedInstanceState.putInt(CURRENT_ITEM, mViewPager.getCurrentItem());
		savedInstanceState.putSerializable(DATABASE, mCardDatabase);
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d(TAG, "onStart: arraylistsize: "
				+ mCardDatabase.getArrayList().size());
		Log.d(TAG, "onStart: currentItem: " + mViewPager.getCurrentItem());
		updateCounter();

	}

	@Override
	public void onButtonClicked() {
		mCardFrag.showAnswer();
	}

	private void updateCounter() {
		CardCounterFragment counter = (CardCounterFragment) getSupportFragmentManager()
				.findFragmentById(R.id.cardCountFragmentContainer);
		if (counter != null) {
			counter.updateCount(mViewPager.getCurrentItem(), mCardDatabase
					.getArrayList().size() - 2);
		}
	}

}
