package com.example.flashcard;

import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ShowCardsFragment extends Fragment {
	private TextView mTextView;
	public static final String EXTRA_CARD_ID = "com.example.flashcards.card_id";
	public static final String EXTRA_CARD_DATABASE = "com.example.flashcards.card_database";
	private FlashCard card;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID cardID = (UUID) getArguments().getSerializable(EXTRA_CARD_ID);
		FlashCardDatabase mCardDatabase = (FlashCardDatabase) getArguments()
				.getSerializable(EXTRA_CARD_DATABASE);

		card = mCardDatabase.getCard(cardID);
	}

	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup parent,
			Bundle savedInstanceState) {

		View v = inflator.inflate(R.layout.card_fragment, parent, false);

		mTextView = (TextView) v.findViewById(R.id.textView);
		mTextView.setText(card.getQuestion());

		return v;
	}

	public static ShowCardsFragment newInstance(UUID crimeID,
			FlashCardDatabase database) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CARD_ID, crimeID);
		args.putSerializable(EXTRA_CARD_DATABASE, database);

		ShowCardsFragment fragment = new ShowCardsFragment();
		fragment.setArguments(args);

		return fragment;
	}

}
