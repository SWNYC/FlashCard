package com.example.flashcard;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class CreateCardsFragment extends Fragment {
	private static final String TAG = "FILES";
	private FlashCardDatabase mDatabase;
	private FlashCard mCurrentCard;
	private EditText mQuestionEditText;
	private EditText mAnswerEditText;
	private ImageButton mPreviousButton;
	private ImageButton mNextButton;
	private Button mAddButton;
	private Button mSaveButton;
	private Button mNewButton;
	private String mFileName;
	private static final int REQUEST_FILENAME = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mDatabase = new FlashCardDatabase();
		mCurrentCard = new FlashCard("!New", "");
	}

	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup parent,
			Bundle savedInstanceState) {

		View v = inflator
				.inflate(R.layout.create_cards_fragment, parent, false);

		mQuestionEditText = (EditText) v.findViewById(R.id.questionEditText);
		mAnswerEditText = (EditText) v.findViewById(R.id.answerEditText);

		mPreviousButton = (ImageButton) v.findViewById(R.id.previousButton);
		mPreviousButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mDatabase.isEmpty()) {
					Toast.makeText(getActivity(), R.string.empty_database,
							Toast.LENGTH_SHORT).show();
				} else {
					mCurrentCard = mDatabase.getPreviousCard();
					mQuestionEditText.setText(mCurrentCard.getQuestion());
					mAnswerEditText.setText(mCurrentCard.getAnswer());
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
					mQuestionEditText.setText(mCurrentCard.getQuestion());
					mAnswerEditText.setText(mCurrentCard.getAnswer());
				}
			}
		});

		mAddButton = (Button) v.findViewById(R.id.addButton);
		mAddButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mQuestionEditText.getText().toString().equals("")
						&& !mAnswerEditText.getText().toString().equals("")) {
					if (mCurrentCard.getQuestion().equals("!New")) {
						mCurrentCard = new FlashCard(mQuestionEditText
								.getText().toString(), mAnswerEditText
								.getText().toString());
						mDatabase.add(mCurrentCard);
						mCurrentCard = new FlashCard("!New", "");
					} else if (mDatabase.contains(mCurrentCard)) {
						FlashCard replacementCard = new FlashCard(
								mQuestionEditText.getText().toString(),
								mAnswerEditText.getText().toString());
						mDatabase.replace(mCurrentCard, replacementCard);
					}
					setNewCard();
					Toast.makeText(getActivity(), R.string.add_card_confirmation,
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), R.string.empty_fields_toast,
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		mNewButton = (Button) v.findViewById(R.id.newButton);
		mNewButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setNewCard();
			}
		});

		mSaveButton = (Button) v.findViewById(R.id.saveButton);
		mSaveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mDatabase.getSize() > 0) {
				SaveFileDialogFragment newFragment = new SaveFileDialogFragment();
				newFragment.setTargetFragment(CreateCardsFragment.this,
						REQUEST_FILENAME);
				newFragment.show(getFragmentManager(), "savefile");
				} else {
					Toast.makeText(getActivity(), R.string.saving_empty_database, Toast.LENGTH_SHORT).show();
				}
			}
		});

		return v;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		if (requestCode == REQUEST_FILENAME) {
			mFileName = (String) data
					.getSerializableExtra(SaveFileDialogFragment.EXTRA_FILENAME);
		}
		
		saveToFile(mFileName);
	}
	
	private void saveToFile(String fileName) {
		
		FileOutputStream fos;
		ObjectOutputStream os;

		try {
			fos = getActivity().openFileOutput(fileName,
					Context.MODE_PRIVATE);
			os = new ObjectOutputStream(fos);
			os.writeObject(mDatabase);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for (String file: getActivity().fileList()) {
			Log.d(TAG, file);
		}
	}
	
	private void setNewCard(){
		mQuestionEditText.setText("");
		mAnswerEditText.setText("");
		mCurrentCard = new FlashCard("!New", "");
	}
}
