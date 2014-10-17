package com.example.flashcard;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

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
	private ViewPager mViewPager;
	private String mFileName;
	private FlashCardDatabase mCardDatabase;
	private ShowCardsFragment mCardFrag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_pager_fragment);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);

		mFileName = (String) getIntent().getSerializableExtra(
				CardsChooserListFragment.EXTRA_FILENAME);

		mCardDatabase = getDatabase(mFileName);
		mCardDatabase.printCardsInLog();

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
		transaction.commit();
		mViewPager.setCurrentItem(1);

	}

	private FlashCardDatabase getDatabase(String fileName) {
		FlashCardDatabase database = null;

		FileInputStream fis;
		ObjectInputStream ois;

		try {
			fis = openFileInput(fileName);
			ois = new ObjectInputStream(fis);

			database = (FlashCardDatabase) ois.readObject();

			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		database.setForCircularScrolling();

		return database;
	}

	@Override
	public void onButtonClicked() {
		mCardFrag.showAnswer();
	}
}
