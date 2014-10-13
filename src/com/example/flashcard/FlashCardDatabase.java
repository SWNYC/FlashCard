package com.example.flashcard;

import java.io.Serializable;
import java.util.ArrayList;

public class FlashCardDatabase implements Serializable{
	
	private int mCardIndex = 0;
	private ArrayList<FlashCard> mDatabase = new ArrayList<FlashCard>();

	public FlashCardDatabase() {
	}

	public void add(FlashCard card) {
		mDatabase.add(card);
	}

	public boolean contains(FlashCard card) {
		return mDatabase.contains(card);
	}

	public void replace(FlashCard card, FlashCard replacementCard) {
		mDatabase.set(mDatabase.indexOf(card), replacementCard);
	}

	public FlashCard getNextCard() {

		mCardIndex = (mCardIndex + 1) % mDatabase.size();
		FlashCard nextCard = mDatabase.get(mCardIndex);
		
		return nextCard;
	}

	public FlashCard getPreviousCard() {

		if (mCardIndex == 0) {
			mCardIndex = mDatabase.size() - 1;
		} else {
			mCardIndex = mCardIndex - 1;
		}

		return mDatabase.get(mCardIndex);

	}

	public int getSize() {
		return mDatabase.size();
	}

	public ArrayList<FlashCard> getArrayList() {
		return mDatabase;
	}

	public boolean isEmpty() {
		return mDatabase.isEmpty();
	}

	public String toString() {
		return "mDatabase has" + mDatabase.size() + " elements: ";
	}

}
