package com.example.flashcard;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

public class CreateCardsPagerActivity extends FragmentActivity implements
		CreateCardsButtonBarFragment.OnButtonsClickedListener,
		SaveFileDialogFragment.SaveFileDialogListener {

	private ViewPager mViewPager;
	private FlashCardDatabase mCardDatabase;
	private CreateCardsFragment mCardFrag;
	private boolean mFirstSave = true;
	private String mFileName;
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Integer> pageIndexTracker = new HashMap<Integer, Integer>();
	private static final String CURRENT_ITEM = "current_item";
	private static final String PAGE_TRACKER = "page_index_tracker";
	private static final String DATABASE = "card_database";
	private static final String IS_FIRST_SAVE = "is_first_save";
	private static final String FILENAME = "file_name";
	public static final String EXTRA_FILENAME = "com.example.flashcard.createcards_filename";
	private static final String TAG = "CREATE";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.card_pager_fragment);
		

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (NavUtils.getParentActivityName(this) != null) {
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}

		mViewPager = (ViewPager) findViewById(R.id.viewPager);

		mCardDatabase = new FlashCardDatabase();
		mCardDatabase.getArrayList().add(new FlashCard("", ""));

		FragmentManager fm = getSupportFragmentManager();
		
		CreateCardsButtonBarFragment buttonBar = new CreateCardsButtonBarFragment();
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
							Log.d(TAG, "pos: " + mViewPager.getCurrentItem());
						}
						updateCounter();
					}
				});

		if (savedInstanceState == null) {
			mViewPager.setCurrentItem(1);
		}
		

		if (savedInstanceState != null) {
			Log.d(TAG, "saveInstanceCurrentItem: " + savedInstanceState.getInt(CURRENT_ITEM));
			mViewPager.setCurrentItem(savedInstanceState.getInt(CURRENT_ITEM));
			mCardDatabase = (FlashCardDatabase) savedInstanceState
					.getSerializable(DATABASE);
			pageIndexTracker = (HashMap<Integer, Integer>) savedInstanceState
					.getSerializable(PAGE_TRACKER);
			mFirstSave = savedInstanceState.getBoolean(IS_FIRST_SAVE);
			mFileName = savedInstanceState.getString(FILENAME);
		}
		
		mViewPager.getAdapter().notifyDataSetChanged();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		Log.d(TAG, "currentItemOnSaveInstance: " + mViewPager.getCurrentItem());
		savedInstanceState.putInt(CURRENT_ITEM, mViewPager.getCurrentItem());
		savedInstanceState.putSerializable(PAGE_TRACKER, pageIndexTracker);
		savedInstanceState.putSerializable(DATABASE, mCardDatabase);
		savedInstanceState.putBoolean(IS_FIRST_SAVE, mFirstSave);
		savedInstanceState.putString(FILENAME, mFileName);
	}

	@Override
	public void onAddButtonClicked() {
		FlashCard currentCard = mCardFrag.getCard();
		String question = currentCard.getQuestion();
		String answer = currentCard.getAnswer();

		if (!question.equals("") && !answer.equals("")) {
			// Replaces card with new data if same card is added again
			Integer value = pageIndexTracker.get(mViewPager.getCurrentItem());

			if (value != null) {
				mCardDatabase.getArrayList().set(value, currentCard);

				Toast.makeText(this, R.string.card_update_confirmation,
						Toast.LENGTH_SHORT).show();
			} else {
				mCardDatabase.getArrayList().add(mViewPager.getCurrentItem(),
						currentCard);
				pageIndexTracker.put(mViewPager.getCurrentItem(), mCardDatabase
						.getArrayList().indexOf(currentCard));
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
		if (!mFirstSave) {
			saveToFile(mFileName);
		} else {
			SaveFileDialogFragment newFragment = new SaveFileDialogFragment();
			newFragment.show(getSupportFragmentManager(), "savefile");
		}

	}

	@Override
	public void onDialogPositiveClick(String fileName) {
		saveToFile(fileName);

		mFileName = fileName;
		mFirstSave = false;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		updateCounter();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.d(TAG, "onPause: currentItem: " + mViewPager.getCurrentItem());
		if (isFinishing() && !mFirstSave) {
			Log.d(TAG, "finishing");
			Iterator<FlashCard> itr = mCardDatabase.getArrayList().iterator();
			while (itr.hasNext()) {
				FlashCard card = itr.next();
				if (card.getQuestion().equals("")) {
					itr.remove();
				}
			}

			if (mCardDatabase.getArrayList().size() != 0) {
				saveToFile(mFileName);
			}

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (NavUtils.getParentActivityName(this) != null) {
				NavUtils.navigateUpFromSameTask(this);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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

		if (mFirstSave) {
			Toast.makeText(getApplicationContext(), R.string.cards_saved,
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), R.string.cards_saved,
					Toast.LENGTH_SHORT).show();
		}

		Intent data = new Intent();
		data.putExtra(EXTRA_FILENAME, fileName);
		setResult(RESULT_OK, data);
	}

	private void updateCounter() {
		CardCounterFragment counter = (CardCounterFragment) getSupportFragmentManager()
				.findFragmentById(R.id.cardCountFragmentContainer);
		if (counter != null) {
			counter.updateCount(mViewPager.getCurrentItem() + 1, mCardDatabase
					.getArrayList().size());
		}
	}

}
