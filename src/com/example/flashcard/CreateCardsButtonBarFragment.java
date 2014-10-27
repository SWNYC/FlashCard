package com.example.flashcard;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CreateCardsButtonBarFragment extends Fragment {
	private Button addCardButton;
	private Button saveCardsButton;
	OnButtonsClickedListener mCallback;

	public interface OnButtonsClickedListener {
		public void onAddButtonClicked();

		public void onSaveButtonClicked();
	}

	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup parent,
			Bundle savedInstanceState) {

		View v = inflator.inflate(R.layout.create_cards_button_bar_fragment,
				parent, false);

		addCardButton = (Button) v.findViewById(R.id.addCardButton);
		addCardButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCallback.onAddButtonClicked();
			}
		});

		saveCardsButton = (Button) v.findViewById(R.id.saveCardsButton);
		saveCardsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCallback.onSaveButtonClicked();
			}
		});

		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (OnButtonsClickedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnButtonsClickedListener");
		}
	}

}
