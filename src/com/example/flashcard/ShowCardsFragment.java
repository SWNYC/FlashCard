package com.example.flashcard;

import java.util.UUID;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ShowCardsFragment extends Fragment {
	private static final String TAG = "SHOWCARDS";
	private TextView mQuestionTextView;
	private TextView mAnswerTextView;
	private ImageButton mPreviousButton;
	private ImageButton mNextButton;
	private Button mShowAnswerButton;
	public static final String EXTRA_CARD_ID = "com.example.flashcards.card_id";
	public static final String EXTRA_CARD_DATABASE = "com.example.flashcards.card_database";
	private FlashCard card;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID cardID = (UUID)getArguments().getSerializable(EXTRA_CARD_ID);
		FlashCardDatabase mCardDatabase = (FlashCardDatabase)getArguments().getSerializable(EXTRA_CARD_DATABASE);
		
		card = mCardDatabase.getCard(cardID);
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
				mAnswerTextView.setText(card.getAnswer());
			}
		});
		

		mPreviousButton = (ImageButton) v.findViewById(R.id.previousButton);

		mNextButton = (ImageButton) v.findViewById(R.id.nextButton);
		
		mQuestionTextView.setText(card.getQuestion());
		
		return v;
	}
	
	public static ShowCardsFragment newInstance(UUID crimeID, FlashCardDatabase database) {
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_CARD_ID, crimeID);
		args.putSerializable(EXTRA_CARD_DATABASE, database);
		
		ShowCardsFragment fragment = new ShowCardsFragment();
		fragment.setArguments(args);
		
		return fragment;
	}
	
}
