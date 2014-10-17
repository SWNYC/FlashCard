package com.example.flashcard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import android.util.Log;

public class FlashCardDatabase implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3925878147421480547L;
	private ArrayList<FlashCard> mDatabase = new ArrayList<FlashCard>();
	private static final String TAG = "DATA";

	public FlashCardDatabase() {
	}

	public ArrayList<FlashCard> getArrayList() {
		return mDatabase;
	}

	public FlashCard getCard(UUID id) {
		for (FlashCard card : mDatabase) {
			if (card.getId().equals(id)) {
				return card;
			}
		}

		return null;
	}

	/**
	 * Creates two copies of the first element to enable ViewPager to have the
	 * illusion of circular scrolling.
	 */
	public void setForCircularScrolling() {
		mDatabase.add(mDatabase.get(0));
		mDatabase.add(0, mDatabase.get(mDatabase.size() - 2));
	}

	public void printCardsInLog() {
		for (FlashCard card : mDatabase) {
			Log.d(TAG, card.toString());
		}
	}
}
