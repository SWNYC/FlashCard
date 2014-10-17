package com.example.flashcard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ShowCardsButtonBarFragment extends Fragment {
	private Button showAnswerButton;
	OnShowAnswerButtonClickedListener mCallback;

	public interface OnShowAnswerButtonClickedListener {
		public void onButtonClicked();
	}

	@Override
	public View onCreateView(LayoutInflater inflator, ViewGroup parent,
			Bundle savedInstanceState) {

		View v = inflator.inflate(R.layout.show_cards_button_bar_fragment,
				parent, false);

		showAnswerButton = (Button) v.findViewById(R.id.showAnswerButton);
		showAnswerButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mCallback.onButtonClicked();
			}
		});

		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		try {
			mCallback = (OnShowAnswerButtonClickedListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnShowAnswerButtonClickedListener");
		}
	}

}
