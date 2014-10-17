package com.example.flashcard;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.SparseIntArray;
import android.view.ViewGroup;
import android.widget.Toast;

public class CreateCardsPagerActivity extends FragmentActivity implements
		CreateCardsButtonBarFragment.OnButtonsClickedListener,
		SaveFileDialogFragment.SaveFileDialogListener {

	private ViewPager mViewPager;
	private FlashCardDatabase mCardDatabase;
	private CreateCardsFragment mCardFrag;
	private SparseIntArray pageIndexTracker = new SparseIntArray();
	public static final String EXTRA_FILENAME = "com.example.flashcard.createcards_filename";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_pager_fragment);
		mViewPager = (ViewPager) findViewById(R.id.viewPager);

		mCardDatabase = new FlashCardDatabase();
		
		// Create initial empty FlashCard for mViewPager to show.
		mCardDatabase.getArrayList().add(new FlashCard("", ""));
		mCardDatabase.setForCircularScrolling();

		FragmentManager fm = getSupportFragmentManager();

		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {

			@Override
			public int getCount() {
				return mCardDatabase.getArrayList().size();
			}

			@Override
			public Fragment getItem(int pos) {
				FlashCard card = mCardDatabase.getArrayList().get(pos);
				return CreateCardsFragment.newInstance(card.getId(),
						mCardDatabase);
			}

			@Override
			public void setPrimaryItem(ViewGroup container, int position,
					Object object) {
				if (mCardFrag != object) {
					mCardFrag = (CreateCardsFragment) object;
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
					}
				});
		CreateCardsButtonBarFragment buttonBar = new CreateCardsButtonBarFragment();

		FragmentTransaction transaction = fm.beginTransaction();
		transaction.add(R.id.buttonBarFragmentContainer, buttonBar);
		transaction.commit();
		mViewPager.setCurrentItem(1);
	}

	@Override
	public void onAddButtonClicked() {
		FlashCard currentCard = mCardFrag.getCard();
		String question = currentCard.getQuestion();
		String answer = currentCard.getAnswer();

		if (!question.equals("") && !answer.equals("")) {
			// Replaces card with new data if same card is added again
			int value = pageIndexTracker.get(mViewPager.getCurrentItem(), -1);
			
			if (value != -1) {
				mCardDatabase.getArrayList().set(value, currentCard);

				Toast.makeText(this, "duplicate", Toast.LENGTH_SHORT).show();
			} else {
				mCardDatabase.getArrayList().add(currentCard);
				pageIndexTracker.put(mViewPager.getCurrentItem(), mCardDatabase.getArrayList().indexOf(currentCard));
				Toast.makeText(this, R.string.add_card_confirmation,
						Toast.LENGTH_SHORT).show();
			}
			mViewPager.getAdapter().notifyDataSetChanged();
		}

		else {
			Toast.makeText(this, R.string.empty_fields_toast,
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onSaveButtonClicked() {
		if (mCardDatabase.getArrayList().size() > 3) {
			SaveFileDialogFragment newFragment = new SaveFileDialogFragment();
			newFragment.show(getSupportFragmentManager(), "savefile");
		} else {
			Toast.makeText(this, R.string.saving_empty_database,
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDialogPositiveClick(String fileName) {
		Iterator<FlashCard> itr = mCardDatabase.getArrayList().iterator();
		while (itr.hasNext()) {
			FlashCard card = itr.next();
			if (card.getQuestion().equals("")) {
				itr.remove();
				mViewPager.getAdapter().notifyDataSetChanged();
			}
		}
		
		saveToFile(fileName);
		
		Intent data = new Intent();
		data.putExtra(EXTRA_FILENAME, fileName);
		setResult(RESULT_OK, data);
	}

	private void saveToFile(String fileName) {

		FileOutputStream fos;
		ObjectOutputStream os;

		try {
			fos = openFileOutput(fileName, Context.MODE_PRIVATE);
			os = new ObjectOutputStream(fos);
			os.writeObject(mCardDatabase);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
