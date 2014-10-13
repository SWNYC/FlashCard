package com.example.flashcard;

import java.io.FileInputStream;
import java.io.ObjectInputStream;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ShowCardsFragment extends Fragment {
	private static final String TAG = "SHOWCARDS";
	private FlashCardDatabase mDatabase;
	private FlashCard mCurrentCard;
	private TextView mQuestionTextView;
	private TextView mAnswerTextView;
	private ImageButton mPreviousButton;
	private ImageButton mNextButton;
	private Button mShowAnswerButton;
	private String mFileName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mFileName = (String) getActivity().getIntent()
				.getSerializableExtra(CardsChooserListFragment.EXTRA_FILENAME);

		getDatabase(mFileName);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup parent,
			Bundle savedInstanceState) {

		View v = inflator
				.inflate(R.layout.show_cards_fragment, parent, false);

		mQuestionTextView = (TextView) v.findViewById(R.id.questionTextView);
		mAnswerTextView = (TextView) v.findViewById(R.id.answerTextView);
		
		mShowAnswerButton = (Button) v.findViewById(R.id.showAnswerButton);
		mShowAnswerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAnswerTextView.setText(mCurrentCard.getAnswer());
			}
		});
		

		mPreviousButton = (ImageButton) v.findViewById(R.id.previousButton);
		mPreviousButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mDatabase.isEmpty()) {
					Toast.makeText(getActivity(), R.string.empty_database,
							Toast.LENGTH_SHORT).show();
				} else {
					mCurrentCard = mDatabase.getPreviousCard();
					mQuestionTextView.setText(mCurrentCard.getQuestion());
					mAnswerTextView.setText("");
				}
			}
		});

		mNextButton = (ImageButton) v.findViewById(R.id.nextButton);
		mNextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mDatabase.isEmpty()) {
					Toast.makeText(getActivity(), R.string.empty_database,
							Toast.LENGTH_SHORT).show();
				} else {
					mCurrentCard = mDatabase.getNextCard();
					mQuestionTextView.setText(mCurrentCard.getQuestion());
					mAnswerTextView.setText("");
				}
			}
		});
		
		mCurrentCard = mDatabase.getArrayList().get(0);
		mQuestionTextView.setText(mCurrentCard.getQuestion());
		
		return v;
	}
	
	private void getDatabase(String fileName) {
		FileInputStream fis;
		ObjectInputStream ois;

		try {
			Log.d(TAG,"Getting streams");
			fis = getActivity().openFileInput(fileName);
			ois = new ObjectInputStream(fis);
			
			Log.d(TAG,"Streams OK");
			
			mDatabase = (FlashCardDatabase) ois.readObject();
			
			Log.d(TAG,"Database success: " + mDatabase.toString());
			ois.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
