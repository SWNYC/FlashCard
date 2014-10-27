package com.example.flashcard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CardCounterFragment extends Fragment {
	private TextView cardCounterTextView;
	private static final String TAG = "COUNTER";

	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup parent,
			Bundle savedInstanceState) {

		View v = inflator
				.inflate(R.layout.card_counter_fragment, parent, false);

		cardCounterTextView = (TextView) v
				.findViewById(R.id.cardCounterTextView);
		
		if (cardCounterTextView != null) {
			Log.d(TAG, "textViewnotNull");
		}
		
		return v;
	}

	public void updateCount(int position, int size) {
		if (cardCounterTextView != null) {
			cardCounterTextView.setText(position + " of " + size);
		}
	}
}
