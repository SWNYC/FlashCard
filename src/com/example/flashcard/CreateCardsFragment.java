package com.example.flashcard;

import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class CreateCardsFragment extends Fragment {
	private EditText mQuestionEditText;
	private EditText mAnswerEditText;
	public static final String EXTRA_CARD_ID = "com.example.flashcards.card_id";
	public static final String EXTRA_CARD_DATABASE = "com.example.flashcards.card_database";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup parent,
			Bundle savedInstanceState) {

		View v = inflator
				.inflate(R.layout.create_cards_fragment, parent, false);

		mQuestionEditText = (EditText) v.findViewById(R.id.CreateCardQuestionEditText);
		mAnswerEditText = (EditText) v.findViewById(R.id.CreateCardAnswerEditText);
		
		return v;
	}

	public static CreateCardsFragment newInstance(UUID crimeID,
			FlashCardDatabase database) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CARD_ID, crimeID);
		args.putSerializable(EXTRA_CARD_DATABASE, database);

		CreateCardsFragment fragment = new CreateCardsFragment();
		fragment.setArguments(args);

		return fragment;
	}
	
	public FlashCard getCard(){
		return new FlashCard(mQuestionEditText.getText().toString(), mAnswerEditText.getText().toString());
	}
}
